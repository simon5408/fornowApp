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
package com.fornow.app.ui.addressmanager;

import java.util.List;

import com.fornow.app.model.RegionData;
import com.fornow.app.ui.MyExpandableListView;
import com.fornow.app.ui.addressmanager.RegionAdapter.ChildViewHolder;
import com.fornow.app.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupExpandListener;

/**
 * @author Simon Lv 2014-3-28
 */
public class ProvinceAdapter extends BaseExpandableListAdapter {
	private List<RegionData> mGoup;
	private Context mContext;

	public ProvinceAdapter(Context context, List<RegionData> group) {
		this.mGoup = group;
		this.mContext = context;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGoup.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mGoup.get(groupPosition).getSons().size();
	}

	@Override
	public RegionData getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mGoup.get(groupPosition);
	}

	@Override
	public RegionData getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mGoup.get(groupPosition).getSons().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;
		final GroupViewHolder holder;
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			rowView = inflater.inflate(R.layout.province_item, null);
			holder = new GroupViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (GroupViewHolder) rowView.getTag();
		}
		holder.getProvinceName().setText(
				getGroup(groupPosition).getName().toString());
		return rowView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final MyExpandableListView SecondLevelexplv = new MyExpandableListView(
				mContext);
		if (!isLastChild) {
			CitiesAdapter carStyleAdapter = new CitiesAdapter(mContext, mGoup
					.get(groupPosition).getSons());
			SecondLevelexplv.setAdapter(carStyleAdapter);
			SecondLevelexplv.setGroupIndicator(null);
			SecondLevelexplv.setDivider(null);
			SecondLevelexplv
					.setOnGroupExpandListener(new OnGroupExpandListener() {

						@Override
						public void onGroupExpand(int groupPosition) {
							for (int i = 0; i < mGoup.get(groupPosition)
									.getSons().size(); i++) {
								if (groupPosition != i) {
									SecondLevelexplv.collapseGroup(i);
								}
							}

						}

					});
		}
		return SecondLevelexplv;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public class GroupViewHolder {
		private View baseView;
		private TextView provinceName;

		public GroupViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public TextView getProvinceName() {
			if (provinceName == null) {
				provinceName = (TextView) baseView
						.findViewById(R.id.region_text);
			}
			return provinceName;
		}
	}

	public class ChildViewHolder {
		private View baseView;
		private TextView cityName;

		public ChildViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public TextView getCityName() {
			if (cityName == null) {
				cityName = (TextView) baseView.findViewById(R.id.region_text);
			}
			return cityName;
		}
	}
}
