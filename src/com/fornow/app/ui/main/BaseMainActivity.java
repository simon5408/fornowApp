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
package com.fornow.app.ui.main;

import android.app.Activity;

/**
 * @author Simon Lv 2013-12-4
 * 
 */
public class BaseMainActivity extends Activity {
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// AlertDialog.Builder builder = new Builder(BaseMainActivity.this);
	// builder.setMessage("确定要退出吗?");
	// builder.setTitle("提示");
	// builder.setPositiveButton("确认",
	// new android.content.DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// BaseMainActivity.this.finish();
	// android.os.Process.killProcess(android.os.Process
	// .myPid());
	// }
	// });
	// builder.setNegativeButton("取消",
	// new android.content.DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	// builder.create().show();
	// }
	// return true;
	// }

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// 
	// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	// CustomDialog.Builder customBuilder = new CustomDialog.Builder(
	// BaseMainActivity.this);
	// customBuilder
	// .setTitle("Custom title")
	// .setMessage("Custom body")
	// .setNegativeButton("Cancel",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int which) {
	// dialog.dismiss();
	// }
	// })
	// .setPositiveButton("Confirm",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int which) {
	// dialog.dismiss();
	// BaseMainActivity.this.finish();
	// }
	// });
	// Dialog dialog = customBuilder.create();
	// dialog.show();
	// Log.d("TAG", "================finish0");
	// return false;
	// } else
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	public void finish() {
		
		super.finish();
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

}
