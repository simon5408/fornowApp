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
package com.fornow.app.utils;

import android.util.Log;

/**
 * @author Jiafa Lv
 * @email simon-jiafa@126.com
 * @date Apr 29, 2014 9:40:02 AM
 */
public class LogUtils {
	private static boolean isDebug = true;

	/**
	 * Display the verbose message
	 * 
	 * @param tag
	 * @param message
	 */
	public static void v(String tag, String message) {
		if (isDebug) {
			Log.v(tag, message);
		}
	}

	/**
	 * Display the error message
	 * 
	 * @param tag
	 * @param message
	 */
	public static void e(String tag, String message) {
		if (isDebug) {
			Log.e(tag, message);
		}
	}

	/**
	 * Display the info message
	 * 
	 * @param tag
	 * @param message
	 */
	public static void i(String tag, String message) {
		if (isDebug) {
			Log.i(tag, message);
		}
	}

	/**
	 * Display the debug message
	 * 
	 * @param tag
	 * @param message
	 */
	public static void d(String tag, String message) {
		if (isDebug) {
			Log.d(tag, message);
		}
	}

	/**
	 * Display the warn message
	 * 
	 * @param tag
	 * @param message
	 */
	public static void w(String tag, String message) {
		if (isDebug) {
			Log.w(tag, message);
		}
	}
}
