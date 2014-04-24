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
package com.fornow.app.ui;

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.CacheData;
import com.fornow.app.net.dao.DaoManager;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class AppClass extends Application {
	private static Context mContext;
	public static Handler globalHandler;
	public static final int UPDATE_CART_COUNT = 0xff;
	public static final int LOGOUT = 0xfe;

	public static Context getContext() {
		return mContext;
	}

	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getMywmParams() {
		return wmParams;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this.getApplicationContext();
		ControllerManager.getInstance().init();
		DaoManager.getInstance().init();
		CacheData.getInstance().init();
		// AnimationsManager.getInstance().init();
		Log.d("TAG", "================on onCreate0");
	}
}
