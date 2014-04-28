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
package com.fornow.app.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * @author Simon Lv 2013-9-5
 */
public class RegionList extends Dialog {
	@SuppressWarnings("unused")
	private Context mContext;

	public RegionList(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public RegionList(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
}
