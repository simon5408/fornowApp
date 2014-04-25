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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.ShipAddressData;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IViewListener;
import com.fornow.app.util.CheckUtils;
import com.fornow.app.util.GsonTool;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class EditShipAddressActivity extends Activity {
	private EditText userNameView, phoneView, addressView;
	private ShipAddressData address = null;
	private ImageButton isDefaultCheck;
	private boolean isDefault = false;
	private Handler mHandler;
	private static final int PHONE_NUM_ERROR = 0x00, NET_ERROR = 0x01;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_address);
		userNameView = (EditText) findViewById(R.id.editUserName);
		phoneView = (EditText) findViewById(R.id.editPhone);
		addressView = (EditText) findViewById(R.id.editAddress);
		isDefaultCheck = (ImageButton) findViewById(R.id.isDefaultCheck);
		Intent intent = getIntent();
		if (intent.getExtras() != null
				&& intent.getExtras().get("addressData") != null) {
			try {
				address = GsonTool.fromJson(
						intent.getExtras().get("addressData").toString(),
						ShipAddressData.class);
				if (address.getName() != null) {
					userNameView.setText(address.getName());
				}
				if (address.getPhone() != null) {
					phoneView.setText(address.getPhone());
				}
				if (address.getAddress() != null) {
					addressView.setText(address.getAddress());
				}

				if (address.isIsdefault()) {
					isDefault = true;
					isDefaultCheck
							.setBackgroundResource(R.drawable.checkbox_yes);
				} else {
					isDefault = false;
					isDefaultCheck
							.setBackgroundResource(R.drawable.checkbox_no);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
				TextView toastText = (TextView) view
						.findViewById(R.id.toast_text);
				Toast toast = new Toast(EditShipAddressActivity.this);
				switch (msg.what) {
				case NET_ERROR:
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				case PHONE_NUM_ERROR:
					toastText.setText(getResources().getString(
							R.string.str_phone_no_error));
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

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void softBack(View v) {
		this.finish();
	}

	public void clearUserName(View v) {
		userNameView.setText(null);
	}

	public void clearPhone(View v) {
		phoneView.setText(null);
	}

	public void clearAddress(View v) {
		addressView.setText(null);
	}

	public void boolDefault(View v) {
		if (isDefault) {
			isDefault = false;
			v.setBackgroundResource(R.drawable.checkbox_no);

		} else {
			isDefault = true;
			v.setBackgroundResource(R.drawable.checkbox_yes);
		}
	}

	public void saveAddress(View v) {
		if (userNameView.getText() != null
				&& !userNameView.getText().equals("")
				&& phoneView.getText() != null
				&& !phoneView.getText().equals("")
				&& addressView.getText() != null
				&& !addressView.getText().equals("")) {

			ShipAddressData newAddress = (address == null) ? new ShipAddressData()
					: address;
			if (CheckUtils.isMobileNO(phoneView.getText().toString())) {
				newAddress.setPhone(phoneView.getText().toString());
				newAddress.setName(userNameView.getText().toString());
				newAddress.setAddress(addressView.getText().toString());
				newAddress.setIsdefault(isDefault);
				ControllerManager.getInstance().getAddressManageController()
						.unRegisterAll();
				ControllerManager.getInstance().getAddressManageController()
						.registerNotification(new IViewListener() {

							@Override
							public void updateView(ViewUpdateObj obj) {
								if (obj.getCode() == 200) {
									Intent intent = new Intent(
											EditShipAddressActivity.this,
											ShdzActivity.class);
									setResult(RESULT_OK, intent);
									EditShipAddressActivity.this.finish();
								} else {
									Message updateViewMsg = mHandler
											.obtainMessage(NET_ERROR);
									mHandler.sendMessage(updateViewMsg);
								}

							}
						});
				ControllerManager.getInstance().getAddressManageController()
						.updateShippingAddress(newAddress);
			} else {
				Message updateViewMsg = mHandler.obtainMessage(PHONE_NUM_ERROR);
				mHandler.sendMessage(updateViewMsg);
			}
		}
	}

}
