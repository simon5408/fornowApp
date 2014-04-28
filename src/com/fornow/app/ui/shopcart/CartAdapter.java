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

import com.fornow.app.model.ShopCart;
import com.fornow.app.ui.loadimg.AsyncImgLoader;
import com.fornow.app.ui.loadimg.AsyncImgLoader.ImageCallback;
import com.fornow.app.ui.shopcart.ShopCartActivity.ListStatus;
import com.fornow.app.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Simon Lv 2013-10-30
 */
public class CartAdapter extends BaseAdapter {

	private List<ShopCart> cart;
	private Context mContext;
	private List<ListStatus> listStatus;
	private Handler mHandler;
	TranslateAnimation mShowAction, mHiddenAction;
	private AsyncImgLoader imgLoader;

	public CartAdapter(List<ShopCart> cart, List<ListStatus> listStatus,
			Context context, Handler handler) {
		this.cart = cart;
		this.mContext = context;
		this.listStatus = listStatus;
		this.mHandler = handler;
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
			holder.getCartItemCheckBox().setBackgroundDrawable(
					mContext.getResources()
							.getDrawable(R.drawable.checkbox_yes));
		} else {
			holder.getCartItemCheckBox()
					.setBackgroundDrawable(
							mContext.getResources().getDrawable(
									R.drawable.checkbox_no));
		}

		holder.getCartItemName().setText(getItem(position).getName());
		holder.getCartIntroduction().setText(
				getItem(position).getIntroduction());
		holder.getCartItemPrice().setText(
				getItem(position).getCurrent_price() + "");
		holder.getCartItemCount().setText(getItem(position).getCount() + "");
		holder.getCartItemCount().setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						

						Message updateViewMsg = mHandler
								.obtainMessage(ShopCartActivity.CAER_COUNT_EDIT);
						updateViewMsg.getData().putString("position",
								position + "");
						mHandler.sendMessage(updateViewMsg);
					}
				});
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

		holder.getCartItemSelect().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (!listStatus.get(position).isChecked()) {
					holder.getCartItemCheckBox().setBackgroundDrawable(
							mContext.getResources().getDrawable(
									R.drawable.checkbox_yes));
					listStatus.get(position).setChecked(true);
				} else {
					holder.getCartItemCheckBox().setBackgroundDrawable(
							mContext.getResources().getDrawable(
									R.drawable.checkbox_no));
					listStatus.get(position).setChecked(false);
				}
				notifyDataSetChanged();
			}
		});

		boolean flag = false;
		Float totalPrice = 0.0f;
		for (int i = 0; i < listStatus.size(); i++) {
			if (listStatus.get(i).isChecked()) {
				flag = true;
				totalPrice += Float.valueOf(getItem(i).getCurrent_price() + "")
						* Integer.valueOf(getItem(i).getCount());
			}
		}
		if (!flag) {
			Message updateViewMsg = mHandler
					.obtainMessage(ShopCartActivity.CART_OVERVIEW_HIDE);
			mHandler.sendMessage(updateViewMsg);
		} else {
			Message updateViewMsg = mHandler
					.obtainMessage(ShopCartActivity.CART_OVERVIEW_SHOW);
			updateViewMsg.getData().putString("data", totalPrice + "");
			mHandler.sendMessage(updateViewMsg);
		}

		return rowView;
	}

	public String itemTotalPrice(String price, int count) {
		Float unitPrice = Float.valueOf(price);
		return new BigDecimal(unitPrice * count).setScale(1,
				BigDecimal.ROUND_HALF_UP) + "";
	}

	public class ViewHolder {
		private View baseView;
		private LinearLayout cartItemSelect;
		private ImageView cartItemCheckBox, cartItemImg;
		private TextView cartItemName, cartIntroduction, cartItemPrice,
				cartItemCount;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public LinearLayout getCartItemSelect() {
			if (cartItemSelect == null) {
				cartItemSelect = (LinearLayout) baseView
						.findViewById(R.id.cart_select);
			}
			return cartItemSelect;
		}

		public ImageView getCartItemCheckBox() {
			if (cartItemCheckBox == null) {
				cartItemCheckBox = (ImageView) baseView
						.findViewById(R.id.cart_checkbox);
			}
			return cartItemCheckBox;
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

		public TextView getCartItemPrice() {
			if (cartItemPrice == null) {
				cartItemPrice = (TextView) baseView
						.findViewById(R.id.TextItemPrice);
			}
			return cartItemPrice;
		}

		public TextView getCartIntroduction() {
			if (cartIntroduction == null) {
				cartIntroduction = (TextView) baseView
						.findViewById(R.id.cart_introduction);
			}
			return cartIntroduction;
		}

		public TextView getCartItemCount() {
			if (cartItemCount == null) {
				cartItemCount = (TextView) baseView
						.findViewById(R.id.cart_item_count);
			}
			return cartItemCount;
		}
	}

}
