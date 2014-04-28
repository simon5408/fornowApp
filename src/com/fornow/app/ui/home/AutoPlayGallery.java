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

import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

/**
 * @author Simon Lv 2013-8-11
 */

public class AutoPlayGallery extends RelativeLayout implements
		OnItemClickListener {
	private final static int BASE_BACKGROUND_COLOR = 0x33000000;
	private int duration = 10000; // switch duration
	private MyGallery mGallery;
	private RadioGroup radioGroup;
	private Bitmap pointBg;
	private Bitmap pointPressedBg;
	private int height = 30; // base height ,can be modify by setHeight
	private boolean flag = false; // switch for playing
	private Thread autoPlayThread;
	private ViewListener callBack;

	public AutoPlayGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setupContentView(context);
	}

	public AutoPlayGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setupContentView(context);
	}

	public AutoPlayGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setupContentView(context);
	}

	private void setupContentView(Context context) {
		pointBg = BitmapFactory.decodeResource(getResources(),
				R.drawable.point_pressed);
		pointPressedBg = BitmapFactory.decodeResource(getResources(),
				R.drawable.point);

		LinearLayout indicator = new LinearLayout(context);
		int heightPX = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, height, getResources()
						.getDisplayMetrics()); // change inch to pix
		indicator
				.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.FILL_PARENT,
						heightPX));
		indicator.setBackgroundColor(BASE_BACKGROUND_COLOR);
		indicator.setGravity(Gravity.CENTER);
		radioGroup = new RadioGroup(context);
		radioGroup.setOrientation(RadioGroup.HORIZONTAL);
		indicator.addView(radioGroup);
		mGallery = new MyGallery(context);
		Gallery.LayoutParams paramGallery = new Gallery.LayoutParams(
				Gallery.LayoutParams.FILL_PARENT,
				Gallery.LayoutParams.FILL_PARENT);
		mGallery.setLayoutParams(paramGallery);
		mGallery.setSpacing(1); // set the spacing between items
		mGallery.setUnselectedAlpha(1.0f); // make all the items light
		mGallery.setHorizontalFadingEdgeEnabled(false); // take off the fading
		mGallery.setOnItemClickListener(this); // effect
		addView(mGallery, new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.FILL_PARENT,
				android.widget.RelativeLayout.LayoutParams.FILL_PARENT));

		android.widget.RelativeLayout.LayoutParams param = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.FILL_PARENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addView(indicator, param);
	}

	/**
	 * set adapter to start the gallery
	 * 
	 * @param adapter
	 */
	public void setAdapter(ImageAdapter adapter) {
		ArrayList<Drawable> drawables = adapter.getDrawables();
		if (drawables != null && drawables.size() > 0) {
			setIndicator(adapter.getContext(), drawables.size());
			mGallery.setAdapter(adapter);
			mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
					indicatePoint(position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
			flag = true;// make the switch true
			if (drawables.size() > 1) {
				play(drawables.size());
			}

		}
	}

	/**
	 * stop the play thread
	 */
	public void stop() {
		flag = false;
		autoPlayThread.interrupt();
		mGallery.setAdapter(new ImageAdapter(getContext(),
				new ArrayList<Drawable>()));
		radioGroup.removeAllViews();
	}

	/**
	 * is play?
	 * 
	 * @return
	 */
	public boolean isPlaying() {
		return flag;
	}

	/**
	 * set duration
	 * 
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * set layout height
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	private void setIndicator(Context context, int size) {
		radioGroup.removeAllViews();
		for (int i = 0; i < size; i++) {
			ImageView point = new ImageView(context);
			point.setImageResource(R.drawable.point_pressed);
			radioGroup.addView(point);
		}
	}

	private void indicateImage(int position) {
		mGallery.setSelection(position);
		imageViewInAniamtion(0);
		indicatePoint(position);
	}

	/**
	 * setAnimation for auto play item
	 * 
	 * @param position
	 */
	private void imageViewInAniamtion(int position) {
		ImageView img = (ImageView) mGallery.getChildAt(position);
		if (img != null) {
			img.startAnimation(getTranslateAnimation());
		}
	}

	/**
	 * a tween animation
	 * 
	 * @return
	 */
	private Animation getTranslateAnimation() {
		AnimationSet as = new AnimationSet(true);
		TranslateAnimation ta = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
				-1.0f, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);
		ta.setDuration(500);
		as.addAnimation(ta);
		return as;
	}

	/**
	 * light the specified point
	 * 
	 * @param position
	 */
	private void indicatePoint(int position) {
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			((ImageView) radioGroup.getChildAt(i)).setImageBitmap(pointBg);
		}
		((ImageView) radioGroup.getChildAt(position))
				.setImageBitmap(pointPressedBg);
	}

	private int count;

	/**
	 * play by a thread
	 * 
	 * @param size
	 */
	@SuppressLint("HandlerLeak")
	private void play(final int size) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (mGallery.isTouched()) {
					count = mGallery.getFirstVisiblePosition() + 1;
					mGallery.setTouched(false);
				} else {
					count++;
				}
				indicateImage(count % size);
			}
		};
		if (autoPlayThread != null) {
			synchronized (autoPlayThread) {
				autoPlayThread.interrupt();
				autoPlayThread = null;
			}
		}
		autoPlayThread = new Thread(new Runnable() {

			@Override
			public void run() {
				do {
					handler.sendEmptyMessage(0);
					try {
						Thread.sleep(duration);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
				} while (flag);
			}
		});
		autoPlayThread.start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO
		ViewUpdateObj viewObj = new ViewUpdateObj();
		viewObj.setData(Integer.toString(position));
		this.callBack.updateView(viewObj);
	}

	public void setCallBack(ViewListener callBack) {
		this.callBack = callBack;
	}
}

class MyGallery extends Gallery {
	public MyGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// Convert the dips to pixels
	float scale = getResources().getDisplayMetrics().density;
	int FLINGTHRESHOLD = (int) (20.0f * scale + 0.5f);
	int SPEED = 600;

	private boolean isTouched = false;

	public void setTouched(boolean isTouched) {
		this.isTouched = isTouched;
	}

	public boolean isTouched() {
		return isTouched;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// cap the velocityX to scroll only one page
		setTouched(true);
		if (velocityX > FLINGTHRESHOLD) {
			return super.onFling(e1, e2, SPEED, velocityY);
		} else if (velocityX < -FLINGTHRESHOLD) {
			return super.onFling(e1, e2, -SPEED, velocityY);
		} else {
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}
}