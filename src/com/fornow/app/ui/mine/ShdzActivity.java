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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.ShipAddressData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.shopcart.JieSuanActivity;
import com.fornow.app.util.GsonTool;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class ShdzActivity extends Activity {
	private Handler mHandler;
	public static final int GET_DATA_SUCCESS = 0x00, DELETE_ADDRESS = 0x01,
			SELECT_ADDRESS = 0x02, NET_ERROR = 0x03, DELETE_ADDRESS_SUCCESS = 0x04;
	private ListView listView;
	private List<ShipAddressData> addressData;
	private AnimateDismissAdapter<String> animateDismissAdapter;
	private ShdzListAdapter mAdapter;
	private Context mContext;
	private Dialog dialog;

	public static enum CLICK_TYPE {
		EDIT, SELECT
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shdz);
		mContext = this.getApplicationContext();
		dialog = new LoadingAnim(ShdzActivity.this, R.style.my_dialog);
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
				case DELETE_ADDRESS:
					pos = Integer.valueOf(msg.getData().getString(
							"position"));
					deleteAddress(addressData.get(pos).getId());
					break;
				case DELETE_ADDRESS_SUCCESS:
					animateDismissAdapter.animateDismiss(pos);
					getAddress();
					break;
				case SELECT_ADDRESS:
					if (msg.getData().getString("position") != null) {
						int position = Integer.valueOf(msg.getData().getString(
								"position"));
						try {
							String selectAddress = GsonTool
									.toJson(addressData.get(position));
							Intent inA = getIntent();
							if (inA.getExtras() != null
									&& inA.getExtras().get("type") != null
									&& inA.getExtras().get("type") == CLICK_TYPE.SELECT) {
								Intent inB = new Intent(ShdzActivity.this,
										JieSuanActivity.class);
								inB.putExtra("addressData", selectAddress);
								setResult(RESULT_OK, inB);
								ShdzActivity.this.finish();
							} else {
								Intent inC = new Intent(ShdzActivity.this,
										EditShipAddressActivity.class);
								inC.putExtra("addressData", selectAddress);
								startActivityForResult(inC, 0);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				case NET_ERROR:
					dialog.dismiss();
					View view = getLayoutInflater().inflate(R.layout.my_toast,
							null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					Toast toast = new Toast(ShdzActivity.this);
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
						if (obj.getCode() == 200) {
							Message updateViewMsg = mHandler
									.obtainMessage(GET_DATA_SUCCESS);
							updateViewMsg.getData().putString("data",
									obj.getData());
							mHandler.sendMessage(updateViewMsg);
						} else {
							Message updateViewMsg = mHandler
									.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
						}

					}
				});
		ControllerManager.getInstance().getAddressManageController()
				.getShippingAddress();
	}
	
	public void deleteAddress(String addressId){
		ControllerManager.getInstance().getAddressManageController().unRegisterAll();
		ControllerManager.getInstance().getAddressManageController().registerNotification(new ViewListener() {
			
			@Override
			public void updateView(ViewUpdateObj obj) {
				if(obj.getCode() == 200){
					Message updateViewMsg = mHandler
							.obtainMessage(DELETE_ADDRESS_SUCCESS);
					mHandler.sendMessage(updateViewMsg);
				}else {
					Message updateViewMsg = mHandler
							.obtainMessage(NET_ERROR);
					mHandler.sendMessage(updateViewMsg);
				}
			}
		});
		ControllerManager.getInstance().getAddressManageController().deleteShipingAddress(addressId);
	}

	@Override
	protected void onStart() {
		super.onStart();
		getAddress();
	}

	public void loadList(String data) {
		if (data != null) {
			try {
				addressData = GsonTool.fromJson(data,
						new TypeToken<List<ShipAddressData>>() {
						});
				mAdapter = new ShdzListAdapter(addressData, mContext, mHandler);
				animateDismissAdapter = new AnimateDismissAdapter<String>(
						mAdapter, new MyOnDismissCallback());
				animateDismissAdapter.setListView(listView);
				listView.setAdapter(animateDismissAdapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class MyOnDismissCallback implements OnDismissCallback {

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
//			for (int position : reverseSortedPositions) {
//				addressData.remove(position);
//				mAdapter.notifyDataSetChanged();
//			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void softBack(View v) {
		this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:

			break;
		default:
			break;
		}
	}

	public void addAddress(View v) {
		Intent intent = new Intent(ShdzActivity.this,
				EditShipAddressActivity.class);
		startActivityForResult(intent, 0);
	}

}
