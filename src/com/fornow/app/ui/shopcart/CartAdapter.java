/*****************************************************************************
 *
 *                      FORNOW PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to ForNow
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from ForNow.
 *
 *            Copyright (c) 2014 by ForNow.  All rights reserved.
 *
 *****************************************************************************/
package com.fornow.app.ui.shopcart;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fornow.app.R;
import com.fornow.app.model.ShopCart;
import com.fornow.app.ui.loadImg.AsyncImgLoader;
import com.fornow.app.ui.loadImg.AsyncImgLoader.ImageCallback;
import com.fornow.app.ui.shopcart.ShopCartActivity.BoolShowDel;
import com.fornow.app.ui.shopcart.ShopCartActivity.ListStatus;
import com.fornow.app.util.GsonTool;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class CartAdapter extends BaseAdapter {

	private List<ShopCart> cart;
	private Context mContext;
	private List<ListStatus> listStatus;
	private Handler mHandler;
	private BoolShowDel boolShowDel;
	private float x, ux;
	private Button curDel_btn;
	TranslateAnimation mShowAction, mHiddenAction;
	private AsyncImgLoader imgLoader;

	public CartAdapter(List<ShopCart> cart, List<ListStatus> listStatus,
			Context context, BoolShowDel boolShowDel, Handler handler) {
		this.cart = cart;
		this.mContext = context;
		this.listStatus = listStatus;
		this.mHandler = handler;
		this.boolShowDel = boolShowDel;
		imgLoader = new AsyncImgLoader();
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		mHiddenAction.setDuration(500);
	}

	@Override
	public int getCount() {
		return cart.size();
	}

	@Override
	public ShopCart getItem(int position) {
		return cart.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			rowView = inflater.inflate(R.layout.shopcartitem, null);
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		if (listStatus.get(position).isChecked()) {
			holder.getCartItemSelect().setBackgroundDrawable(
					mContext.getResources()
							.getDrawable(R.drawable.checkbox_yes));
		} else {
			holder.getCartItemSelect()
					.setBackgroundDrawable(
							mContext.getResources().getDrawable(
									R.drawable.checkbox_no));
		}

		if (boolShowDel.isDisplayDel()) {
			holder.getCartDel().setVisibility(View.VISIBLE);
		} else {
			holder.getCartDel().setVisibility(View.GONE);
		}

		holder.getCartItemName().setText(
				mContext.getResources().getString(R.string.goods_name_tag)
						+ getItem(position).getName());
		holder.getCartItemInput().setText(getItem(position).getCount() + "");
		holder.getCartItemPrice().setText(
				mContext.getResources().getString(R.string.goods_unit)
						+ getItem(position).getCurrent_price());
		holder.getCartItemCount().setText("*" + getItem(position).getCount());
		holder.getCartItemTotalPrice().setText(
				itemTotalPrice(getItem(position).getCurrent_price() + "",
						getItem(position).getCount()));
		if (getItem(position).getIcon().getUrl() != null
				&& getItem(position).getIcon().getId() != null) {
			final ImageView imageView = holder.getCartItemImg();
			final View mView = rowView;
			imageView.setTag(getItem(position).getIcon().getId());
			imgLoader.loadDrawable(getItem(position).getIcon().getUrl(),
					getItem(position).getIcon().getId(), new ImageCallback() {
						@Override
						public void imageLoaded(final Drawable imageDrawable,
								final String Tag) {
							imageView.post(new Runnable() {
								@Override
								public void run() {
									ImageView imageViewByTag = (ImageView) mView
											.findViewWithTag(Tag);
									if (imageViewByTag != null
											&& imageDrawable != null) {
										imageViewByTag
												.setImageDrawable(imageDrawable);
									} else if (imageViewByTag != null
											&& imageDrawable == null) {
										imageViewByTag
												.setImageDrawable(mContext
														.getResources()
														.getDrawable(
																R.drawable.cart_img01));
									}
								}
							});
						}
					});
		}

		holder.getCartItemCountMinus().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						int currentCount = Integer.parseInt(holder
								.getCartItemInput().getText().toString());
						if (currentCount > 1) {
							holder.getCartItemInput().setText(
									--currentCount + "");
							holder.getCartItemCount().setText(
									"*" + currentCount);
							holder.getCartItemTotalPrice().setText(
									itemTotalPrice(getItem(position)
											.getCurrent_price() + "",
											currentCount));
							getItem(position).setCount(currentCount);
							CartDataHelper.updateCacheCart(cart);
							if (listStatus.get(position).isChecked()) {
								Message updateViewMsg = mHandler
										.obtainMessage(ShopCartActivity.CART_MINUS);
								updateViewMsg.getData().putString(
										"data",
										getItem(position).getCurrent_price()
												+ "");
								mHandler.sendMessage(updateViewMsg);
							}
						}
					}
				});
		holder.getCartItemCountAdd().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int currentCount = Integer.parseInt(holder.getCartItemInput()
						.getText().toString());
				holder.getCartItemInput().setText(++currentCount + "");
				holder.getCartItemCount().setText("*" + currentCount);
				holder.getCartItemTotalPrice().setText(
						itemTotalPrice(getItem(position).getCurrent_price()
								+ "", currentCount));
				getItem(position).setCount(currentCount);
				CartDataHelper.updateCacheCart(cart);
				if (listStatus.get(position).isChecked()) {
					Message updateViewMsg = mHandler
							.obtainMessage(ShopCartActivity.CART_ADD);
					updateViewMsg.getData().putString("data",
							getItem(position).getCurrent_price() + "");
					mHandler.sendMessage(updateViewMsg);
				}
			}
		});

		holder.getCartItemSelect().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!listStatus.get(position).isChecked()) {
					holder.getCartItemSelect().setBackgroundDrawable(
							mContext.getResources().getDrawable(
									R.drawable.checkbox_yes));
					listStatus.get(position).setChecked(true);

					Message updateViewMsg = mHandler
							.obtainMessage(ShopCartActivity.CART_SELECT);
					updateViewMsg.getData()
							.putString(
									"data",
									holder.getCartItemTotalPrice().getText()
											.toString());
					mHandler.sendMessage(updateViewMsg);
				} else {
					holder.getCartItemSelect().setBackgroundDrawable(
							mContext.getResources().getDrawable(
									R.drawable.checkbox_no));
					listStatus.get(position).setChecked(false);

					Message updateViewMsg = mHandler
							.obtainMessage(ShopCartActivity.CART_UNSELECT);
					updateViewMsg.getData()
							.putString(
									"data",
									holder.getCartItemTotalPrice().getText()
											.toString());
					mHandler.sendMessage(updateViewMsg);
				}
			}
		});
		rowView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					x = event.getX();
					if (curDel_btn != null) {
						curDel_btn.startAnimation(mHiddenAction);
						curDel_btn.setVisibility(View.GONE);
						curDel_btn = null;
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_MOVE) {// 松开处理
					ux = event.getX();
					if (holder.getCartDel() != null) {
						if (Math.abs(x - ux) > 50) {
							holder.getCartDel().startAnimation(mShowAction);
							holder.getCartDel().setVisibility(View.VISIBLE);
							curDel_btn = holder.getCartDel();
						}
					}
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					ux = event.getX();
					if (Math.abs(x - ux) < 50) {
						try {
							Message updateViewMsg = mHandler
									.obtainMessage(ShopCartActivity.CART_CLICKED);
							updateViewMsg.getData().putString(
									"data",
									GsonTool.getGsonTool().toJson(
											getItem(position)));
							mHandler.sendMessage(updateViewMsg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return true;
			}
		});

		holder.getCartDel().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message updateViewMsg = mHandler
						.obtainMessage(ShopCartActivity.CART_DEL);
				updateViewMsg.getData().putString("data",
						holder.getCartItemTotalPrice().getText().toString());
				updateViewMsg.getData().putString("position", position + "");
				mHandler.sendMessage(updateViewMsg);
			}
		});
		return rowView;
	}

	public String itemTotalPrice(String price, int count) {
		Float unitPrice = Float.valueOf(price);
		return new BigDecimal(unitPrice * count).setScale(1,
				BigDecimal.ROUND_HALF_UP) + "";
	}

	public class ViewHolder {
		private View baseView;
		private ImageButton cartItemSelect, cartItemCountMinus,
				cartItemCountAdd;
		private ImageView cartItemImg;
		private TextView cartItemName, cartItemInput, cartItemPrice,
				cartItemCount, cartItemTotalPrice;
		private Button cartDel;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public ImageButton getCartItemSelect() {
			if (cartItemSelect == null) {
				cartItemSelect = (ImageButton) baseView
						.findViewById(R.id.cart_select);
			}
			return cartItemSelect;
		}

		public ImageButton getCartItemCountMinus() {
			if (cartItemCountMinus == null) {
				cartItemCountMinus = (ImageButton) baseView
						.findViewById(R.id.cart_count_minus);
			}
			return cartItemCountMinus;
		}

		public ImageButton getCartItemCountAdd() {
			if (cartItemCountAdd == null) {
				cartItemCountAdd = (ImageButton) baseView
						.findViewById(R.id.cart_count_add);
			}
			return cartItemCountAdd;
		}

		public ImageView getCartItemImg() {
			if (cartItemImg == null) {
				cartItemImg = (ImageView) baseView.findViewById(R.id.cart_img);
			}
			return cartItemImg;
		}

		public TextView getCartItemName() {
			if (cartItemName == null) {
				cartItemName = (TextView) baseView.findViewById(R.id.cart_name);
			}
			return cartItemName;
		}

		public TextView getCartItemInput() {
			if (cartItemInput == null) {
				cartItemInput = (TextView) baseView
						.findViewById(R.id.cart_input_area);
			}
			return cartItemInput;
		}

		public TextView getCartItemPrice() {
			if (cartItemPrice == null) {
				cartItemPrice = (TextView) baseView
						.findViewById(R.id.TextItemPrice);
			}
			return cartItemPrice;
		}

		public TextView getCartItemCount() {
			if (cartItemCount == null) {
				cartItemCount = (TextView) baseView
						.findViewById(R.id.cart_goods_count);
			}
			return cartItemCount;
		}

		public TextView getCartItemTotalPrice() {
			if (cartItemTotalPrice == null) {
				cartItemTotalPrice = (TextView) baseView
						.findViewById(R.id.cart_item_total_price);
			}
			return cartItemTotalPrice;
		}

		public Button getCartDel() {
			if (cartDel == null) {
				cartDel = (Button) baseView.findViewById(R.id.cart_del);
			}
			return cartDel;
		}
	}

}
