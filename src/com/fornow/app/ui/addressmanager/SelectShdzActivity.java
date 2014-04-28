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

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.ShipAddressData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.shopcart.JieSuanActivity;
import com.fornow.app.utils.GsonTool;
import com.google.gson.reflect.TypeToken;
import com.fornow.app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Simon Lv 2014-3-15
 */
public class SelectShdzActivity extends Activity {
	private Handler mHandler;
	public static final int GET_DATA_SUCCESS = 0x00, NET_ERROR = 0x03;
	private ListView listView;
	private List<ShipAddressData> addressData;
	private ShdzListAdapter mAdapter;
	private Context mContext;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_shdz);
		mContext = this.getApplicationContext();
		dialog = new LoadingAnim(SelectShdzActivity.this, R.style.my_dialog);
		listView = (ListView) findViewById(R.id.addressList);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int pos = 0;
				switch (msg.what) {
				case GET_DATA_SUCCESS:
					dialog.dismiss();
					String data = msg.getData().getString("data");
					loadList(data);
					break;
				case NET_ERROR:
					dialog.dismiss();
					View view = getLayoutInflater().inflate(R.layout.my_toast,
							null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					Toast toast = new Toast(SelectShdzActivity.this);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				default:
					break;
				}
			}
		};

	}

	public void getAddress() {
		dialog.show();
		ControllerManager.getInstance().getAddressManageController()
				.unRegisterAll();
		ControllerManager.getInstance().getAddressManageController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						// TODO Auto-generated method stub
						Message updateViewMsg;
						if (obj.getCode() == 200) {
							updateViewMsg = mHandler
									.obtainMessage(GET_DATA_SUCCESS);
							updateViewMsg.getData().putString("data",
									obj.getData());
							mHandler.sendMessage(updateViewMsg);
						} else {
							updateViewMsg = mHandler.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
						}

					}
				});
		ControllerManager.getInstance().getAddressManageController()
				.getShippingAddress();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getAddress();
	}

	public void loadList(String data) {
		if (data != null) {
			try {
				addressData = GsonTool.getGsonTool().fromJson(data,
						new TypeToken<List<ShipAddressData>>() {
						}.getType());
				mAdapter = new ShdzListAdapter(addressData, mContext);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						try {
							String selectAddress = GsonTool.getGsonTool()
									.toJson(addressData.get(arg2));
							Intent inB = new Intent(SelectShdzActivity.this,
									JieSuanActivity.class);
							inB.putExtra("addressData", selectAddress);
							setResult(RESULT_OK, inB);
							SelectShdzActivity.this.finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void softBack(View v) {
		this.finish();
	}

	public void manageAddress(View v) {
		Intent intent = new Intent(SelectShdzActivity.this,
				ShdzActivity.class);
		startActivityForResult(intent, 0);
	}
}
