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

import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.ui.AppClass;
import com.fornow.app.ui.loadimg.AsyncImgLoader;
import com.fornow.app.ui.loadimg.AsyncImgLoader.ImageCallback;
import com.fornow.app.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Simon Lv 2013-11-17
 */
public class JiesuanAdapter extends BaseAdapter {
	private List<GoodsDetailData> mList;
	private Context mContext;
	private Handler mHandler;
	private AsyncImgLoader imgLoader;
	TranslateAnimation mShowAction, mHiddenAction;

	public JiesuanAdapter(List<GoodsDetailData> list, Context context,
			Handler handler) {
		this.mList = list;
		this.mContext = context;
		this.mHandler = handler;
		imgLoader = new AsyncImgLoader();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public GoodsDetailData getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public static String itemTotalPrice(String price, int count) {
		Float unitPrice = Float.valueOf(price);
		return new BigDecimal(unitPrice * count).setScale(1,
				BigDecimal.ROUND_HALF_UP) + "";
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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
		holder.getJiesuanGoodsName().setText(getItem(position).getName() + "");
		holder.getJiesuanGoodsPrice().setText(
				mContext.getResources().getString(R.string.goods_unit)
						+ getItem(position).getCurrent_price());
		holder.getJiesuanGoodsCount().setText(
				getItem(position).getSelect_count() + "");
		holder.getJiesuanGoodsIntro().setText(
				getItem(position).getIntroduction());
		holder.getJiesuanGoodsCount().setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Message updateViewMsg = mHandler
								.obtainMessage(JieSuanActivity.COUNT_EDIT);
						updateViewMsg.getData().putString("position",
								position + "");
						mHandler.sendMessage(updateViewMsg);
					}
				});

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
							// TODO Auto-generated method stub
							imageView.post(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
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

		return rowView;
	}

	public class ViewHolder {
		private View baseView;
		private ImageView jiesuanGoodsImg;
		private TextView jiesuanGoodsName, jiesuanGoodsPrice,
				jiesuanGoodsIntro, jiesuanGoodsCount;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
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

		public TextView getJiesuanGoodsIntro() {
			if (jiesuanGoodsIntro == null) {
				jiesuanGoodsIntro = (TextView) baseView
						.findViewById(R.id.jiesuan_goods_introduction);
			}
			return jiesuanGoodsIntro;
		}

	}

}
