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

import com.fornow.app.model.ShipAddressData;
import com.fornow.app.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Simon Lv 2013-11-6
 */
public class ShdzListAdapter extends BaseAdapter {
	private List<ShipAddressData> addressData;
	private Context mContext;

	public ShdzListAdapter(List<ShipAddressData> addressData, Context context) {
		this.addressData = addressData;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		
		return addressData.size();
	}

	@Override
	public ShipAddressData getItem(int position) {
		
		return addressData.get(position);
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
			rowView = inflater.inflate(R.layout.shdz_list_item, null);
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		holder.getNameView().setText(getItem(position).getName() + "");
		holder.getPhoneView().setText(getItem(position).getPhone() + "");
		holder.getAddressView().setText(getItem(position).getAddress() + "");
		if (getItem(position).isIsdefault()) {
			holder.getDefaultCheck().setVisibility(View.VISIBLE);
			holder.getNameView().setTextColor(Color.parseColor("#ff5000"));
			holder.getPhoneView().setTextColor(Color.parseColor("#ff5000"));
			holder.getAddressView().setTextColor(Color.parseColor("#ff5000"));
		} else {
			holder.getDefaultCheck().setVisibility(View.GONE);
			holder.getNameView().setTextColor(Color.parseColor("#5e5e5e"));
			holder.getPhoneView().setTextColor(Color.parseColor("#5e5e5e"));
			holder.getAddressView().setTextColor(Color.parseColor("#5e5e5e"));
		}
		return rowView;
	}

	public class ViewHolder {
		private View baseView;
		private TextView nameView, phoneView, addressView;
		private ImageView defaultCheck;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public TextView getNameView() {
			if (nameView == null) {
				nameView = (TextView) baseView.findViewById(R.id.shdz_name);
			}
			return nameView;
		}

		public TextView getPhoneView() {
			if (phoneView == null) {
				phoneView = (TextView) baseView.findViewById(R.id.shdz_phone);
			}
			return phoneView;
		}

		public TextView getAddressView() {
			if (addressView == null) {
				addressView = (TextView) baseView
						.findViewById(R.id.shdz_address);
			}
			return addressView;
		}

		public ImageView getDefaultCheck() {
			if (defaultCheck == null) {
				defaultCheck = (ImageView) baseView
						.findViewById(R.id.default_check);
			}
			return defaultCheck;
		}
	}

}
