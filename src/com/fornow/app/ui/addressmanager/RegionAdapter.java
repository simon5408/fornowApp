/**
 * 
 */
package com.fornow.app.ui.addressmanager;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fornow.app.model.RegionData;
import com.fornow.app.ui.MyExpandableListView;
import com.fornow.app.R;

/**
 * @author Simon Lv
 * 
 */
public class RegionAdapter extends BaseExpandableListAdapter {
	private List<RegionData> mGoup;
	private Context mContext;

	public RegionAdapter(Context context, List<RegionData> group) {
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
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;
		final GroupViewHolder holder;
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			rowView = inflater.inflate(R.layout.regions_item, null);
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
//		LayoutInflater inflater = LayoutInflater.from(mContext);
//		View rowView = convertView;
//		final ChildViewHolder holder;
//		if (rowView == null) {
//			rowView = inflater.inflate(R.layout.areas_item, null);
//			holder = new ChildViewHolder(rowView);
//			rowView.setTag(holder);
//		} else {
//			holder = (ChildViewHolder) rowView.getTag();
//		}
//		holder.getCityName().setText(
//				getChild(groupPosition, childPosition).getName().toString());
//		return rowView;
		
		MyExpandableListView SecondLevelexplv = new MyExpandableListView(
				mContext);
		if (!isLastChild) {
			RegionAdapter carStyleAdapter = new RegionAdapter(mContext, mGoup
					.get(groupPosition).getSons());

			SecondLevelexplv.setAdapter(carStyleAdapter);
			SecondLevelexplv.setGroupIndicator(null);
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
