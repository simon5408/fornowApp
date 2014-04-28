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
package com.fornow.app.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.fornow.app.ui.loadimg.AsyncImgLoader;
import com.fornow.app.ui.loadimg.AsyncImgLoader.ImageCallback;
import com.fornow.app.R;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Simon Lv 2013-10-27
 */
public class GridViewImgAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> list;
	private Context mContext;
	private AsyncImgLoader imgLoader;

	public GridViewImgAdapter(Context context,
			ArrayList<HashMap<String, Object>> imgList) {
		this.mContext = context;
		this.list = imgList;
		imgLoader = new AsyncImgLoader();
	}

	@Override
	public int getCount() {
		
		return this.list.size();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public HashMap getItem(int position) {
		
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		ViewHolder holder;
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			rowView = inflater.inflate(R.layout.goodsitem, null);
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		holder.getItemName().setText(
				getItem(position).get("GoodsName").toString());
		holder.getItemCurrentPrice().setText(
				getItem(position).get("GoodsCurrentPrice").toString());
		holder.getItemOriginPrice().setText(
				getItem(position).get("GoodsOriginPrice").toString());
		holder.getItemOriginPrice().getPaint()
				.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		if (getItem(position).get("ImgId") != null
				&& getItem(position).get("ImgUrl") != null) {
			final ImageView imageView = holder.getItemimg();
			final View mView = rowView;
			imageView.setTag(getItem(position).get("ImgId").toString());
			imgLoader.loadDrawable(getItem(position).get("ImgUrl").toString(),
					getItem(position).get("ImgId").toString(),
					new ImageCallback() {
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

		return rowView;
	}

	public class ViewHolder {
		private View baseView;
		private ImageView itemimg;
		private TextView itemName;
		private TextView itemCurrentPrice;
		private TextView itemOriginPrice;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public ImageView getItemimg() {
			if (itemimg == null) {
				itemimg = (ImageView) baseView.findViewById(R.id.ImageItemId);
			}
			return itemimg;
		}

		public TextView getItemName() {
			if (itemName == null) {
				itemName = (TextView) baseView.findViewById(R.id.TextItemName);
			}
			return itemName;
		}

		public TextView getItemCurrentPrice() {
			if (itemCurrentPrice == null) {
				itemCurrentPrice = (TextView) baseView
						.findViewById(R.id.TextItemPrice);
			}
			return itemCurrentPrice;
		}

		public TextView getItemOriginPrice() {
			if (itemOriginPrice == null) {
				itemOriginPrice = (TextView) baseView
						.findViewById(R.id.TextItemOriginPrice);
			}
			return itemOriginPrice;
		}
	}
}
