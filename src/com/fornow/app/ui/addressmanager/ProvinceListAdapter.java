/**
 * 
 */
package com.fornow.app.ui.addressmanager;

import java.util.List;

import com.fornow.app.model.RegionData;
import com.fornow.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Simon Lv
 * 
 */
public class ProvinceListAdapter extends BaseAdapter {
	private Context mContext;
	private List<RegionData> list;

	public ProvinceListAdapter(Context context, List<RegionData> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		
		return this.list.size();
	}

	@Override
	public RegionData getItem(int position) {
		
		return this.list.get(position);
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
			rowView = inflater.inflate(R.layout.province_item, null);
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		holder.getProvinceName()
				.setText(getItem(position).getName().toString());
		return rowView;
	}

	public class ViewHolder {
		private View baseView;
		private TextView provinceName;

		public ViewHolder(View baseView) {
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

}
