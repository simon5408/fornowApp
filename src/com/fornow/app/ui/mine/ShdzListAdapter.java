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
package com.fornow.app.ui.mine;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fornow.app.R;
import com.fornow.app.model.ShipAddressData;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class ShdzListAdapter extends BaseAdapter {
	private List<ShipAddressData> addressData;
	private Context mContext;
	private Handler mHandler;
	TranslateAnimation mShowAction, mHiddenAction;
	private Button curDel_btn;
	private float x, ux;

	public ShdzListAdapter(List<ShipAddressData> addressData, Context context,
			Handler handler) {
		this.addressData = addressData;
		this.mContext = context;
		this.mHandler = handler;
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
		holder.getDeleteAddress().setVisibility(View.GONE);
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
		rowView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					x = event.getX();
					if (curDel_btn != null) {
						curDel_btn.startAnimation(mHiddenAction);
						curDel_btn.setVisibility(View.GONE);
						curDel_btn = null;
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_MOVE) {// 松开处理
					ux = event.getX();
					if (holder.getDeleteAddress() != null) {
						if (Math.abs(x - ux) > 50) {
							holder.getDeleteAddress().startAnimation(
									mShowAction);
							holder.getDeleteAddress().setVisibility(
									View.VISIBLE);
							curDel_btn = holder.getDeleteAddress();
						}
					}
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					ux = event.getX();
					if (Math.abs(x - ux) < 50) {
						Message updateViewMsg = mHandler
								.obtainMessage(ShdzActivity.SELECT_ADDRESS);
						updateViewMsg.getData().putString("position", position + "");
						mHandler.sendMessage(updateViewMsg);
					}
				}
				return true;
			}
		});

		holder.getDeleteAddress().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message updateViewMsg = mHandler
						.obtainMessage(ShdzActivity.DELETE_ADDRESS);
				updateViewMsg.getData().putString("position", position + "");
				mHandler.sendMessage(updateViewMsg);
			}
		});
		return rowView;
	}

	public class ViewHolder {
		private View baseView;
		private TextView nameView, phoneView, addressView;
		private ImageView defaultCheck;
		private Button deleteAddress;

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

		public Button getDeleteAddress() {
			if (deleteAddress == null) {
				deleteAddress = (Button) baseView
						.findViewById(R.id.address_del);
			}
			return deleteAddress;
		}

	}

}
