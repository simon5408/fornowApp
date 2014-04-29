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
package com.fornow.app.utils.captcha;

/**
 * @author Jiafa Lv
 * @email simon-jiafa@126.com
 * @date Apr 29, 2014 9:40:02 AM
 */
public interface ICaptchaCheck {
	public static final int PTEDE_TIME = 2000;
	public static final int POINT_NUM = 80;
	public static final int LINE_NUM = 2;
	

	public void setCheckNum(int[] chenckNum);

	public int[] getCheckNum();

	public void invaliChenkNum();
}
