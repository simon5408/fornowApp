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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Simon Lv 2013-12-22
 */
public class CheckUtils {
	public static final String EMAIL_PATTERN = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	public static final String MOBILE_PATTERN = "^((13[0-9])|(15[^4,\\D])|(18[0,1,5-9]))\\d{8}$";
	public static final String NUMBER_PATTERN = "^[0-9]{5}$";
	public static final String POST_PATTERN = "^[1-9]\\d{5}(?!\\d)$";

	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		Pattern regex = Pattern.compile(EMAIL_PATTERN);
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
		Pattern p = Pattern.compile(MOBILE_PATTERN);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isNum(String number) {
		Pattern p = Pattern.compile(NUMBER_PATTERN);
		Matcher m = p.matcher(number);
		return m.matches();
	}

	/**
	 * 验证邮政编码
	 * 
	 * @param post
	 * @return
	 */
	public static boolean checkPost(String post) {
		Pattern p = Pattern.compile(POST_PATTERN);
		Matcher m = p.matcher(post);
		return m.matches();
		// if (post.matches("[1-9]\\d{5}(?!\\d)")) {
		// return true;
		// } else {
		// return false;
		// }
	}
}