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
package com.fornow.app.util;

import android.util.Log;

/**
 * @author Jiafa Lv
 * @email simon-jiafa@126.com
 * @date Apr 25, 2014 2:05:57 PM
 */
public class LogUtils {
	private static final boolean isLog = true;

	/**
	 * Display Verbose logs
	 * 
	 * @param tag
	 * @param message
	 */
	public static void v(String tag, String message) {
		if (isLog) {
			Log.v(tag, message);
		}
	}

	/**
	 * Display Debug logs
	 * 
	 * @param tag
	 * @param message
	 */
	public static void d(String tag, String message) {
		if (isLog) {
			Log.d(tag, message);
		}
	}

	/**
	 * Display Error logs
	 * 
	 * @param tag
	 * @param message
	 */
	public static void e(String tag, String message) {
		if (isLog) {
			Log.e(tag, message);
		}
	}

	/**
	 * Display Info logs
	 * 
	 * @param tag
	 * @param message
	 */
	public static void i(String tag, String message) {
		if (isLog) {
			Log.i(tag, message);
		}
	}

	/**
	 * Display Warning logs
	 * 
	 * @param tag
	 * @param message
	 */
	public static void w(String tag, String message) {
		if (isLog) {
			Log.w(tag, message);
		}
	}
}
