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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.fornow.app.datapool.CacheData;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.ui.AppClass;
import com.fornow.app.ui.customdialog.CommonMsgDialog;
import com.fornow.app.R;

public class SettingActivity extends Activity {
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		mContext = this.getApplicationContext();
	}

	public void logout(View v) {
		final CommonMsgDialog dialogBuilder = new CommonMsgDialog(
				SettingActivity.this, R.style.Theme_Dialog);
		dialogBuilder.setDialogTitle(mContext.getResources().getString(
				R.string.str_tishi));
		dialogBuilder.setDialogMsg(mContext.getResources().getString(
				R.string.str_exit_msg));
		dialogBuilder.setOnCancelBtnListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				dialogBuilder.dismiss();
			}
		});

		dialogBuilder.setOnConfirmBtnListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				dialogBuilder.dismiss();
				ClientData.getInstance().recycle();
				CacheData.getInstance().setLoginPass(null);
				CacheData.getInstance().setAutologin(false);
				Message updateViewMsg = AppClass.globalHandler
						.obtainMessage(AppClass.LOGOUT);
				AppClass.globalHandler.sendMessage(updateViewMsg);
			}

		});
		dialogBuilder.show();
	}

	public void cleanCache(View v) {
		final CommonMsgDialog dialogBuilder = new CommonMsgDialog(
				SettingActivity.this, R.style.Theme_Dialog);
		dialogBuilder.setDialogTitle(mContext.getResources().getString(
				R.string.str_tishi));
		dialogBuilder.setDialogMsg(mContext.getResources().getString(
				R.string.str_clean_cache_msg));
		dialogBuilder.setOnCancelBtnListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				dialogBuilder.dismiss();
			}
		});

		dialogBuilder.setOnConfirmBtnListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				dialogBuilder.dismiss();
				CacheData.getInstance().cleanCache();
			}

		});
		dialogBuilder.show();
	}

	public void go2Yjfk(View v) {
		Intent intent = new Intent(SettingActivity.this, YjfkActivity.class);
		startActivity(intent);
	}

	public void softBack(View v) {
		this.finish();
	}
}
