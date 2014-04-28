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

import com.google.gson.Gson;

/**
 * @author Simon Lv 2013-8-4
 */
public class GsonTool {
	private static Gson gson;

	public static Gson getGsonTool() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}
}
