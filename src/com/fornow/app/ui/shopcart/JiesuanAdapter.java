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

import com.fornow.app.R;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.ui.AppClass;
import com.fornow.app.ui.loadImg.AsyncImgLoader;
import com.fornow.app.ui.loadImg.AsyncImgLoader.ImageCallback;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class JiesuanAdapter extends BaseAdapter {
	private List<GoodsDetailData> mList;
	private Context mContext;
	private Handler mHandler;
	private Button jiesuanDel;
	private float x, ux;
	private AsyncImgLoader imgLoader;
	TranslateAnimation mShowAction, mHiddenAction;

	public JiesuanAdapter(List<GoodsDetailData> list, Context context,
			Handler handler) {
		this.mList = list;
		this.mContext = context;
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
		return mList.size();
	}

	@Override
	public GoodsDetailData getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static String itemTotalPrice(String price, int count) {
		Float unitPrice = Float.valueOf(price);
		return new BigDecimal(unitPrice * count).setScale(1,
				BigDecimal.ROUND_HALF_UP) + "";
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			rowView = inflater.inflate(R.layout.jiesuanitem, null);
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		holder.getJiesuanGoodsName().setText(
				mContext.getResources().getString(R.string.goods_name_tag)
						+ getItem(position).getName());
		holder.getJiesuanGoodsInput().setText(
				getItem(position).getSelect_count() + "");
		holder.getJiesuanGoodsPrice().setText(
				mContext.getResources().getString(R.string.goods_unit)
						+ getItem(position).getCurrent_price());
		holder.getJiesuanGoodsCount().setText(
				"*" + getItem(position).getSelect_count());
		holder.getJiesuanGoodsTotalPrice().setText(
				itemTotalPrice(getItem(position).getCurrent_price() + "",
						getItem(position).getSelect_count()));

		if (getItem(position).getIcon().getUrl() != null
				&& getItem(position).getIcon().getId() != null) {
			final ImageView imageView = holder.getJiesuanGoodsImg();
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
												.setImageDrawable(AppClass
														.getContext()
														.getResources()
														.getDrawable(
																R.drawable.cart_img01));
									}
								}
							});
						}
					});
		}

		holder.getJiesuanGoodsCountMinus().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						int currentCount = Integer.parseInt(holder
								.getJiesuanGoodsInput().getText().toString());
						if (currentCount > 1) {
							holder.getJiesuanGoodsInput().setText(
									--currentCount + "");
							holder.getJiesuanGoodsCount().setText(
									"*" + currentCount);
							holder.getJiesuanGoodsTotalPrice().setText(
									itemTotalPrice(getItem(position)
											.getCurrent_price() + "",
											currentCount));
							getItem(position).setSelect_count(currentCount);
							Message updateViewMsg = mHandler
									.obtainMessage(JieSuanActivity.JIESUAN_MINUS);
							updateViewMsg.getData().putString("data",
									getItem(position).getCurrent_price() + "");
							mHandler.sendMessage(updateViewMsg);
						}
					}
				});
		holder.getJiesuanGoodsCountAdd().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						int currentCount = Integer.parseInt(holder
								.getJiesuanGoodsInput().getText().toString());
						holder.getJiesuanGoodsInput().setText(
								++currentCount + "");
						holder.getJiesuanGoodsCount().setText(
								"*" + currentCount);
						holder.getJiesuanGoodsTotalPrice()
								.setText(
										itemTotalPrice(getItem(position)
												.getCurrent_price() + "",
												currentCount));
						getItem(position).setSelect_count(currentCount);
						Message updateViewMsg = mHandler
								.obtainMessage(JieSuanActivity.JIESUAN_ADD);
						updateViewMsg.getData().putString("data",
								getItem(position).getCurrent_price() + "");
						mHandler.sendMessage(updateViewMsg);
					}
				});
		rowView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					x = event.getX();
					if (jiesuanDel != null) {
						jiesuanDel.startAnimation(mHiddenAction);
						jiesuanDel.setVisibility(View.GONE);
						jiesuanDel = null;
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_MOVE) {// 松开处理
					ux = event.getX();
					if (holder.getJiesuanGoodsDel() != null) {
						if (Math.abs(x - ux) > 50) {
							holder.getJiesuanGoodsDel().startAnimation(
									mShowAction);
							holder.getJiesuanGoodsDel().setVisibility(
									View.VISIBLE);
							jiesuanDel = holder.getJiesuanGoodsDel();
						}
					}
				} else {
				}
				return true;
			}
		});

		holder.getJiesuanGoodsDel().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message updateViewMsg = mHandler
						.obtainMessage(JieSuanActivity.JIESUAN_DEL);
				updateViewMsg.getData()
						.putString(
								"data",
								holder.getJiesuanGoodsTotalPrice().getText()
										.toString());
				updateViewMsg.getData().putString("position", position + "");
				mHandler.sendMessage(updateViewMsg);
			}
		});
		return rowView;
	}

	public class ViewHolder {
		private View baseView;
		private ImageButton jiesuanGoodsCountMinus, jiesuanGoodsCountAdd;
		private ImageView jiesuanGoodsImg;
		private TextView jiesuanGoodsName, jiesuanGoodsInput,
				jiesuanGoodsPrice, jiesuanGoodsCount, jiesuanGoodsTotalPrice;
		private Button jiesuanGoodsDel;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public ImageButton getJiesuanGoodsCountMinus() {
			if (jiesuanGoodsCountMinus == null) {
				jiesuanGoodsCountMinus = (ImageButton) baseView
						.findViewById(R.id.jiesuan_goods_count_minus);
			}
			return jiesuanGoodsCountMinus;
		}

		public ImageButton getJiesuanGoodsCountAdd() {
			if (jiesuanGoodsCountAdd == null) {
				jiesuanGoodsCountAdd = (ImageButton) baseView
						.findViewById(R.id.jiesuan_goods_count_add);
			}
			return jiesuanGoodsCountAdd;
		}

		public ImageView getJiesuanGoodsImg() {
			if (jiesuanGoodsImg == null) {
				jiesuanGoodsImg = (ImageView) baseView
						.findViewById(R.id.jiesuan_goods_img);
			}
			return jiesuanGoodsImg;
		}

		public TextView getJiesuanGoodsName() {
			if (jiesuanGoodsName == null) {
				jiesuanGoodsName = (TextView) baseView
						.findViewById(R.id.jiesuan_goods_name);
			}
			return jiesuanGoodsName;
		}

		public TextView getJiesuanGoodsInput() {
			if (jiesuanGoodsInput == null) {
				jiesuanGoodsInput = (TextView) baseView
						.findViewById(R.id.jiesuan_goods_input_area);
			}
			return jiesuanGoodsInput;
		}

		public TextView getJiesuanGoodsPrice() {
			if (jiesuanGoodsPrice == null) {
				jiesuanGoodsPrice = (TextView) baseView
						.findViewById(R.id.jiesuan_goodsPrice);
			}
			return jiesuanGoodsPrice;
		}

		public TextView getJiesuanGoodsCount() {
			if (jiesuanGoodsCount == null) {
				jiesuanGoodsCount = (TextView) baseView
						.findViewById(R.id.jiesuan_goods_count);
			}
			return jiesuanGoodsCount;
		}

		public TextView getJiesuanGoodsTotalPrice() {
			if (jiesuanGoodsTotalPrice == null) {
				jiesuanGoodsTotalPrice = (TextView) baseView
						.findViewById(R.id.jiesuan_goods_total_price);
			}
			return jiesuanGoodsTotalPrice;
		}

		public Button getJiesuanGoodsDel() {
			if (jiesuanGoodsDel == null) {
				jiesuanGoodsDel = (Button) baseView
						.findViewById(R.id.jiesuan_goods_del);
			}
			return jiesuanGoodsDel;
		}

	}

}
