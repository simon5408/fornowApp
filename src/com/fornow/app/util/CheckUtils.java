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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class CheckUtils {
	private static final String EMAIL_RULE = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	private static final String MOBILE_RULE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	private static final String NUMBER_RULE = "^[0-9]{5}$";

	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		Pattern regex = Pattern.compile(EMAIL_RULE);
		Matcher matcher = regex.matcher(email);

		return matcher.matches();
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return [0-9]{5,9}
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile(MOBILE_RULE);
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	/**
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNum(String number) {
		Pattern p = Pattern.compile(NUMBER_RULE);
		Matcher m = p.matcher(number);
		
		return m.matches();
	}
}