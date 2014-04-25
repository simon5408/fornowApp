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
package com.fornow.app.ui.setting;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.fornow.app.R;
import com.fornow.app.net.NetRequest;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.NetworkManager;
import com.fornow.app.service.IDataCallback;
import com.fornow.app.util.LogUtils;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class SettingActivity extends Activity {
	private static final String TAG = SettingActivity.class.getName();

	private TextView myName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.settings);
		myName = (TextView) findViewById(R.id.my_name);

		NetRequest netGetReq = NetRequest.createGetRequest("http://httpbin.org/ip");
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(final NetResponse netRes) {
				
				LogUtils.d(TAG,"Code:"+netRes.code);
				LogUtils.d(TAG,"Response:"+netRes.res);
				
				myName.post(new Runnable() {
					@Override
					public void run() {
						try {
							JSONObject json = new JSONObject(netRes.res);
							String origin = json.getString("origin");
							myName.setText(origin);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
			}
		});	
		
		
		try {
			JSONObject json = new JSONObject();
			json.put("name", "value");
			NetRequest netPostReq = NetRequest.createPostRequest("http://httpbin.org/post", json.toString().getBytes());
			NetworkManager.sendPostReq(netPostReq, new IDataCallback() {
				@Override
				public void updateData(NetResponse netRes) {
					LogUtils.d(TAG,"Code:"+netRes.code);
					LogUtils.d(TAG,"Response:"+netRes.res);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
