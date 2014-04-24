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
package com.fornow.app.ui.captcha;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class CheckGetUtil {
	public static int[] getCheckNum() {
		int[] tempCheckNum = { 0, 0, 0, 0 };
		for (int i = 0; i < 4; i++) {
			tempCheckNum[i] = (int) (Math.random() * 10);
		}
		return tempCheckNum;
	}

	public static int[] getLine(int height, int width) {
		int[] tempCheckNum = { 0, 0, 0, 0 };
		for (int i = 0; i < 4; i += 2) {
			tempCheckNum[i] = (int) (Math.random() * width);
			tempCheckNum[i + 1] = (int) (Math.random() * height);
		}
		return tempCheckNum;
	}

	public static int[] getPoint(int height, int width) {
		int[] tempCheckNum = { 0, 0, 0, 0 };
		tempCheckNum[0] = (int) (Math.random() * width);
		tempCheckNum[1] = (int) (Math.random() * height);
		return tempCheckNum;
	}

	public static boolean checkNum(String userCheck, int[] checkNum) {
		if (userCheck.length() != 4) {
			System.out.println("te.checkNum()return falsess");
			return false;
		}
		String checkString = "";
		for (int i = 0; i < 4; i++) {
			checkString += checkNum[i];
		}
		if (userCheck.equals(checkString)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getPositon(int height) {
		int tempPositoin = (int) (Math.random() * height);
		if (tempPositoin < 15) {
			tempPositoin += 15;
		}
		return tempPositoin;
	}
}
