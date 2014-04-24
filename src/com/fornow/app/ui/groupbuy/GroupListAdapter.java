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
package com.fornow.app.ui.groupbuy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fornow.app.R;
import com.fornow.app.model.GroupListData;
import com.fornow.app.ui.AppClass;
import com.fornow.app.ui.loadImg.AsyncImgLoader;
import com.fornow.app.ui.loadImg.AsyncImgLoader.ImageCallback;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class GroupListAdapter extends BaseAdapter {
	private List<GroupListData> groupList;
	private Context mContext;
	private AsyncImgLoader imgLoader;
	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public GroupListAdapter(List<GroupListData> groupList, Context context) {
		this.groupList = groupList;
		this.mContext = context;
		imgLoader = new AsyncImgLoader();
	}

	@Override
	public int getCount() {
		return groupList.size();
	}

	@Override
	public GroupListData getItem(int position) {
		return groupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			rowView = inflater.inflate(R.layout.group_list_item, null);
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		holder.getGoodName().setText(
				AppClass.getContext().getResources()
						.getString(R.string.goods_name_tag)
						+ getItem(position).getName());
		holder.getGoodPrice().setText(
				AppClass.getContext().getResources()
						.getString(R.string.goods_unit)
						+ getItem(position).getCurrent_price() + "");
		String dateAfterFormat = format.format(new Date(Long.parseLong(getItem(
				position).getEnd_time())));
		holder.getGroupEndTime().setText(
				mContext.getResources().getString(
						R.string.str_groupbuy_end_time_tag)
						+ dateAfterFormat);

		if (getItem(position).getIcon().getUrl() != null
				&& getItem(position).getIcon().getId() != null) {
			final ImageView imageView = holder.getGoodIcon();
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
		private ImageView goodIcon;
		private TextView goodName, goodPrice, groupEndTime;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public ImageView getGoodIcon() {
			if (goodIcon == null) {
				goodIcon = (ImageView) baseView.findViewById(R.id.good_icon);
			}
			return goodIcon;
		}

		public TextView getGoodName() {
			if (goodName == null) {
				goodName = (TextView) baseView.findViewById(R.id.good_name);
			}
			return goodName;
		}

		public TextView getGoodPrice() {
			if (goodPrice == null) {
				goodPrice = (TextView) baseView.findViewById(R.id.good_price);
			}
			return goodPrice;
		}

		public TextView getGroupEndTime() {
			if (groupEndTime == null) {
				groupEndTime = (TextView) baseView
						.findViewById(R.id.group_end_time);
			}
			return groupEndTime;
		}
	}
}
