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

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @author Simon Lv 2013-8-11
 */

public class ImageAdapter extends BaseAdapter {

	Context context;
	ArrayList<Drawable> drawables;

	public ImageAdapter(Context context, ArrayList<Drawable> drawables) {
		this.context = context;
		this.drawables = drawables;
	}

	public Context getContext() {
		return context;
	}

	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}

	@Override
	public int getCount() {
		
		return drawables.size();
	}

	@Override
	public Object getItem(int position) {
		
		return drawables.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		if (convertView == null) {
			convertView = new ImageView(context);
			Gallery.LayoutParams params = new Gallery.LayoutParams(
					Gallery.LayoutParams.FILL_PARENT,
					Gallery.LayoutParams.FILL_PARENT);
			convertView.setLayoutParams(params);
			((ImageView) convertView).setScaleType(ScaleType.FIT_XY);
		}
		if (drawables.size() > 0) {
			Drawable item = drawables.get(position % drawables.size());
			((ImageView) convertView).setImageDrawable(item);
		}
		return convertView;
	}
}
