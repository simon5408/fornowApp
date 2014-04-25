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
package com.fornow.app.ui.preload;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.CacheData;
import com.fornow.app.model.LoginData;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IViewListener;
import com.fornow.app.ui.home.HomeActivity.BoolLoadComplete;
import com.fornow.app.ui.main.MainActivity;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class PreLoad extends Activity {
//	private static final String TAG = "PreLoad";
	private BoolLoadComplete boolLoadComplete;
	private Handler mHandler;
	private static final int LOADING_END = 0x00, NET_ERROR = 0x01;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pre_load);
		final Intent intent = new Intent(PreLoad.this, MainActivity.class);
		// startActivity(intent);
		// PreLoad.this.finish();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LOADING_END:
					intent.putExtra("success", true);
					startActivity(intent);
					PreLoad.this.finish();
					break;
				case NET_ERROR:
					View view = getLayoutInflater().inflate(R.layout.my_toast,
							null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					Toast toast = new Toast(PreLoad.this);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					intent.putExtra("success", false);
					startActivity(intent);
					PreLoad.this.finish();
					break;
				default:
					break;
				}
			}
		};

		boolLoadComplete = new BoolLoadComplete();
		ControllerManager.getInstance().getSearchController().unRegisterAll();
		ControllerManager.getInstance().getSearchController()
				.registerNotification(new IViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						if (obj.getCode() == 200) {
							switch (obj.getNotifyId()) {
							case HOME_BANNER:
								boolLoadComplete.setGetBanner(true);
								break;
							case HOME_PRIVILEGE:
								boolLoadComplete.setGetPrivilege(true);
								break;
							case VERSION:
								boolLoadComplete.setGetVersion(true);
								break;
							case MIN_LIMIT:
								boolLoadComplete.setGetMinLimit(true);
								break;
							default:
								break;
							}
							if (boolLoadComplete.boolComplete()) {
								Message updateViewMsg = mHandler
										.obtainMessage(LOADING_END);
								mHandler.sendMessage(updateViewMsg);
							}
						} else {
							Message updateViewMsg = mHandler
									.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
						}

					}
				});

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ControllerManager.getInstance().getSearchController()
						.getHomeData();
				boolean isAutoLogin = CacheData.getInstance().getAutologin();
				if (isAutoLogin) {
					String userName = CacheData.getInstance().getLoginName();
					String password = CacheData.getInstance().getLoginPass();
					if (userName != null && password != null) {
						LoginData loginData = new LoginData();
						loginData.setUsername(userName);
						loginData.setPassword(password);
						ControllerManager.getInstance().getLoginController()
								.unRegisterAll();
						ControllerManager.getInstance().getLoginController()
								.registerNotification(new IViewListener() {

									@Override
									public void updateView(ViewUpdateObj obj) {
									}
								});
						ControllerManager.getInstance().getLoginController()
								.login(loginData);
					}
				}
			}
		}, 1500);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ImageView imgLoading = (ImageView) findViewById(R.id.preload_img);
		imgLoading.setBackgroundResource(R.anim.pre_load_anim);
		AnimationDrawable ad = (AnimationDrawable) imgLoading.getBackground();
		ad.stop();
		ad.start();
	}

}
