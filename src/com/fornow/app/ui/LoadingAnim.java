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
package com.fornow.app.ui;

import com.fornow.app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class LoadingAnim extends Dialog {
//	private Context mContext;

	public LoadingAnim(Context context) {
		super(context);
//		this.mContext = context;
	}

	public LoadingAnim(Context context, int theme) {
		super(context, theme);
//		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_loading);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ImageView imgLoading = (ImageView) findViewById(R.id.loading_img);
		imgLoading.setBackgroundResource(R.anim.loading_anim);
		AnimationDrawable ad = (AnimationDrawable) imgLoading.getBackground();
		ad.stop();
		ad.start();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

}
