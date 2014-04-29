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
package com.fornow.app.utils.captcha.impl;

import com.fornow.app.utils.captcha.CaptchaCore;
import com.fornow.app.utils.captcha.ICaptchaCheck;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Jiafa Lv
 * @email simon-jiafa@126.com
 * @date Apr 29, 2014 9:40:02 AM
 */
public class CaptchaCheckImpl extends View implements ICaptchaCheck {

	Context mContext;
	int[] CheckNum = { 0, 0, 0, 0 };
	Paint mTempPaint = new Paint();

	public CaptchaCheckImpl(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;
		mTempPaint.setAntiAlias(true);
		mTempPaint.setTextSize(25);
		mTempPaint.setStrokeWidth(3);
	}

	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLUE);

		final int height = getHeight();
		final int width = getWidth();
		int dx = 20;
		for (int i = 0; i < 4; i++) {
			canvas.drawText("" + CheckNum[i], dx,
					CaptchaCore.getPositon(height), mTempPaint);
			dx += width / 5;
		}
		int[] line;
		for (int i = 0; i < ICaptchaCheck.LINE_NUM; i++) {
			line = CaptchaCore.getLine(height, width);
			canvas.drawLine(line[0], line[1], line[2], line[3], mTempPaint);
		}
		int[] point;
		for (int i = 0; i < ICaptchaCheck.POINT_NUM; i++) {
			point = CaptchaCore.getPoint(height, width);
			canvas.drawCircle(point[0], point[1], 1, mTempPaint);
		}
	}

	public void setCheckNum(int[] chenckNum) {
		CheckNum = chenckNum;
	}

	public int[] getCheckNum() {
		return CheckNum;
	}

	public void invaliChenkNum() {
		invalidate();
	}

}
