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
package com.fornow.app.utils.pull2refresh;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @author Simon Lv 2013-8-28
 */

public class ViewCompat {

	public static void postOnAnimation(View view, Runnable runnable) {
		view.postDelayed(runnable, 16);
	}

	public static void setBackground(View view, Drawable background) {
		view.setBackgroundDrawable(background);
	}

	public static void setLayerType(View view, int layerType) {
		// if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
		// SDK11.setLayerType(view, layerType);
		// }
	}

}
