/**
 * 
 */
package com.fornow.app.ui.customdialog;

import com.fornow.app.ui.login.LoginActivity;
import com.fornow.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author Simon Lv
 * 
 */
public class LoginDialog {
	Activity activity;
	Context mContext;
	String title, msg;
	int resultcode;

	public LoginDialog(Activity activity, Context context, String title,
			String msg, int resultcode) {
		this.activity = activity;
		this.mContext = context;
		this.title = title;
		this.msg = msg;
		this.resultcode = resultcode;
	}

	public void build() {
		final CommonMsgDialog dialogBuilder = new CommonMsgDialog(activity,
				R.style.Theme_Dialog);
		dialogBuilder.setDialogTitle(mContext.getResources().getString(
				R.string.str_tishi));
		dialogBuilder.setDialogMsg(mContext.getResources().getString(
				R.string.str_uuid_timeout));
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
				Intent in = new Intent(mContext, LoginActivity.class);
				activity.startActivityForResult(in, resultcode);
			}

		});
		dialogBuilder.show();
	}
}
