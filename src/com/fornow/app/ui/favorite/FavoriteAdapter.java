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
package com.fornow.app.ui.favorite;

import java.util.List;

import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.ui.AppClass;
import com.fornow.app.ui.loadimg.AsyncImgLoader;
import com.fornow.app.ui.loadimg.AsyncImgLoader.ImageCallback;
import com.fornow.app.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Simon Lv 2013-11-29
 * 
 */
public class FavoriteAdapter extends BaseAdapter {
	private List<GoodsDetailData> mDetailList;
	private Context mContext;
	private AsyncImgLoader imgLoader;
	TranslateAnimation mShowAction, mHiddenAction;

	public FavoriteAdapter(List<GoodsDetailData> detailList, Handler handler,
			Context context) {
		this.mDetailList = detailList;
		this.mContext = context;
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
		
		return mDetailList.size();
	}

	@Override
	public GoodsDetailData getItem(int position) {
		
		return mDetailList.get(position);
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
			rowView = inflater.inflate(R.layout.favorite_list_item, null);
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		holder.getFavGoodName().setText(getItem(position).getName());
		holder.getFavGoodPrice().setText(
				mContext.getResources().getString(R.string.goods_unit)
						+ getItem(position).getCurrent_price() + "");
		holder.getFavGoodIntro().setText(getItem(position).getIntroduction());
		if (getItem(position).getIcon().getUrl() != null
				&& getItem(position).getIcon().getId() != null) {
			final ImageView imageView = holder.getFavGoodIcon();
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
		return rowView;
	}

	public class ViewHolder {
		private View baseView;
		private ImageView favGoodIcon;
		private TextView favGoodName, favGoodPrice, favGoodIntro;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public ImageView getFavGoodIcon() {
			if (favGoodIcon == null) {
				favGoodIcon = (ImageView) baseView
						.findViewById(R.id.fav_good_icon);
			}
			return favGoodIcon;
		}

		public TextView getFavGoodName() {
			if (favGoodName == null) {
				favGoodName = (TextView) baseView
						.findViewById(R.id.fav_good_name);
			}
			return favGoodName;
		}

		public TextView getFavGoodPrice() {
			if (favGoodPrice == null) {
				favGoodPrice = (TextView) baseView
						.findViewById(R.id.fav_good_price);
			}
			return favGoodPrice;
		}

		public TextView getFavGoodIntro() {
			if (favGoodIntro == null) {
				favGoodIntro = (TextView) baseView
						.findViewById(R.id.fav_good_intro);
			}
			return favGoodIntro;
		}
	}

}
