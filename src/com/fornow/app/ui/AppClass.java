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

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.dao.DaoManager;
import com.fornow.app.datapool.CacheData;

/**
 * @author Simon Lv 2013-8-18
 */
public class AppClass extends Application {
	private static Context mContext;
	public static Handler globalHandler;
	public static final int UPDATE_CART_COUNT = 0xff, LOGOUT = 0xfe;

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
	}
}
