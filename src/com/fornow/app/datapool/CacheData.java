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
package com.fornow.app.datapool;

import com.fornow.app.ui.AppClass;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class CacheData {
	private static SharedPreferences share;
	private static CacheData cacheData;

	private final String BASE_URL = "base_url";
	private final String baseUrl = "http://1.93.28.198:8080";
	//private final String baseUrl = "http://192.168.1.8:8080";
	private static final String AUTOLOGIN = "isAutoLogin";
	private static final String LOGIN_NAME = "loginName";
	private static final String LOGIN_PASS = "loginPass";

	public CacheData() {
		share = PreferenceManager.getDefaultSharedPreferences(AppClass
				.getContext());
	}

	public static CacheData getInstance() {
		if (cacheData == null) {
			synchronized (CacheData.class) {
				if (cacheData == null) {
					cacheData = new CacheData();
				}
			}
		}
		return cacheData;
	}

	public void init() {
		setBaseUrl(baseUrl);
	}

	public String getBaseUrl() {
		return share.getString(BASE_URL, null);
	}

	public void setBaseUrl(String url) {
		share.edit().putString(BASE_URL, url).commit();
	}

	public boolean getAutologin() {
		return share.getBoolean(AUTOLOGIN, false);
	}

	public void setAutologin(boolean isAuto) {
		share.edit().putBoolean(AUTOLOGIN, isAuto).commit();
	}

	public String getLoginName() {
		return share.getString(LOGIN_NAME, null);
	}

	public void setLoginName(String data) {
		share.edit().putString(LOGIN_NAME, data).commit();
	}

	public String getLoginPass() {
		return share.getString(LOGIN_PASS, null);
	}

	public void setLoginPass(String data) {
		share.edit().putString(LOGIN_PASS, data).commit();
	}

}
