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
package com.fornow.app.ui.ordermanager;

import com.fornow.app.model.OrderGoodsList;
import com.fornow.app.ui.AppClass;
import com.fornow.app.ui.loadimg.AsyncImgLoader;
import com.fornow.app.ui.loadimg.AsyncImgLoader.ImageCallback;
import com.fornow.app.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Simon Lv 2013-11-21
 */
public class OrderGoodsAdapter extends BaseAdapter {
	private OrderGoodsList[] mGoodsList;
	private Context mContext;
	private AsyncImgLoader imgLoader;

	public OrderGoodsAdapter(OrderGoodsList[] goodsList, Context context) {
		this.mGoodsList = goodsList;
		this.mContext = context;
		imgLoader = new AsyncImgLoader();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mGoodsList.length;
	}

	@Override
	public OrderGoodsList getItem(int position) {
		// TODO Auto-generated method stub
		return mGoodsList[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;
		final ViewHolder holder;
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			rowView = inflater.inflate(R.layout.order_goods_item, null);
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		if (getItem(position).getName() != null) {
			holder.getOrderGoodsName().setText(
					mContext.getResources().getString(R.string.goods_name_tag)
							+ getItem(position).getName());
		}
		holder.getOrderGoodsCount().setText("*" + getItem(position).getCount());
		if (getItem(position).getUnit_price() != null) {
			Float xiaoji = Float.valueOf(getItem(position).getUnit_price() + "")
					* getItem(position).getCount();
			holder.getOrderUnitPrice().setText(
					mContext.getResources().getString(R.string.goods_unit)
							+ getItem(position).getUnit_price()
							+ mContext.getResources().getString(
									R.string.sell_unit));
			holder.getOrderPriceXiaoji().setText(
					mContext.getResources().getString(R.string.goods_unit)
							+ xiaoji);
		}

		if (getItem(position).getIcon().getUrl() != null
				&& getItem(position).getIcon().getId() != null) {
			final ImageView imageView = holder.getOrderGoodsImg();
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
		private TextView orderGoodsName, orderUnitPrice, orderGoodsCount,
				orderPriceXiaoji;
		private ImageView orderGoodsImg;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public TextView getOrderGoodsName() {
			if (orderGoodsName == null) {
				orderGoodsName = (TextView) baseView
						.findViewById(R.id.order_goods_name);
			}
			return orderGoodsName;
		}

		public TextView getOrderUnitPrice() {
			if (orderUnitPrice == null) {
				orderUnitPrice = (TextView) baseView
						.findViewById(R.id.order_unit_price);
			}
			return orderUnitPrice;
		}

		public TextView getOrderGoodsCount() {
			if (orderGoodsCount == null) {
				orderGoodsCount = (TextView) baseView
						.findViewById(R.id.order_goods_count);
			}
			return orderGoodsCount;
		}

		public TextView getOrderPriceXiaoji() {
			if (orderPriceXiaoji == null) {
				orderPriceXiaoji = (TextView) baseView
						.findViewById(R.id.order_price_xiaoji);
			}
			return orderPriceXiaoji;
		}

		public ImageView getOrderGoodsImg() {
			if (orderGoodsImg == null) {
				orderGoodsImg = (ImageView) baseView
						.findViewById(R.id.order_goods_img);
			}
			return orderGoodsImg;
		}

	}

}
