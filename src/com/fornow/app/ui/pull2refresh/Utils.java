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
package com.fornow.app.ui.pull2refresh;

import com.fornow.app.util.LogUtils;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class Utils {

	static final String LOG_TAG = "PullToRefresh";

	public static void warnDeprecation(String depreacted, String replacement) {
		LogUtils.w(LOG_TAG, "You're using the deprecated " + depreacted
				+ " attr, please switch over to " + replacement);
	}

}
