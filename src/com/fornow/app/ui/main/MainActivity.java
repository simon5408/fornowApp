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
package com.fornow.app.ui.main;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.fornow.app.R;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.ui.AppClass;
import com.fornow.app.ui.customdialog.CommonMsgDialog;
import com.fornow.app.ui.home.HomeActivity;
import com.fornow.app.ui.login.LoginActivity;
import com.fornow.app.ui.mine.MineActivity;

/**
 * @author Simon Lv 2013-8-4
 */
@SuppressLint("ResourceAsColor")
public class MainActivity extends TabActivity implements OnClickListener {
	public static String TAB_TAG = "tabTag";
	private Context mContext;
	public static String TAB_TAG_HOME = "home";
	public static String TAB_TAG_GROUPBUY = "groupBuy";
	public static String TAB_TAG_SHOPPING_CART = "shopCart";
	public static String TAB_TAG_FAVORITE = "favorite";
	public static String TAB_TAG_MINE = "mine";
	private static TabHost mTabHost;
	ImageView mBut1, mBut2, mBut3, mBut4, mBut5;
	TextView mCateText1, mCateText2, mCateText3, mCateText4, mCateText5,
			cartCornerMark;
	Intent mHomeItent, mMineIntent;

	int mCurTabId = R.id.channel1;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = this.getApplicationContext();
		prepareIntent();
		prepareView();
		setUpIntent();
		AppClass.globalHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AppClass.UPDATE_CART_COUNT:
					break;
				case AppClass.LOGOUT:
					stayOnWhichTab(TAB_TAG_HOME);
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		Intent intent = getIntent();
		if (intent.getExtras() != null
				&& intent.getExtras().get(TAB_TAG) != null) {
			String tag = intent.getExtras().get(TAB_TAG).toString();
			stayOnWhichTab(tag);
		}
	}


	public void stayOnWhichTab(String tag) {
		// clean the effects
		clearFocusEffect();
		if (tag.equals(TAB_TAG_HOME)) {
			mCurTabId = R.id.channel1;
			mTabHost.setCurrentTabByTag(TAB_TAG_HOME);
			addTab1Effect();
		} else if (tag.equals(TAB_TAG_GROUPBUY)) {
			mCurTabId = R.id.channel2;
			mTabHost.setCurrentTabByTag(TAB_TAG_GROUPBUY);
			addTab2Effect();
		} else if (tag.equals(TAB_TAG_SHOPPING_CART)) {
			mCurTabId = R.id.channel3;
			mTabHost.setCurrentTabByTag(TAB_TAG_SHOPPING_CART);
			addTab3Effect();
		} else if (tag.equals(TAB_TAG_FAVORITE)) {
			mCurTabId = R.id.channel4;
			mTabHost.setCurrentTabByTag(TAB_TAG_FAVORITE);
			addTab4Effect();
		} else if (tag.equals(TAB_TAG_MINE)) {
			mCurTabId = R.id.channel5;
			mTabHost.setCurrentTabByTag(TAB_TAG_MINE);
			addTab5Effect();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case R.id.btn_channel3:
				stayOnWhichTab(TAB_TAG_SHOPPING_CART);
				break;
			case R.id.btn_channel4:
				stayOnWhichTab(TAB_TAG_FAVORITE);
				break;
			case R.id.btn_channel5:
				stayOnWhichTab(TAB_TAG_MINE);
				break;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		int checkedId = v.getId();
		if (mCurTabId == v.getId()) {
			return;
		}

		if (checkedId == R.id.btn_channel3 || checkedId == R.id.btn_channel4
				|| checkedId == R.id.btn_channel5) {

			if (ClientData.getInstance().getmUUID() == null) {

				Intent in = new Intent(MainActivity.this, LoginActivity.class);

				startActivityForResult(in, checkedId);
				return;

			}
		}
		clearFocusEffect();
		switch (checkedId) {
		case R.id.btn_channel1:
			mTabHost.setCurrentTabByTag(TAB_TAG_HOME);
			addTab1Effect();
			break;
		case R.id.btn_channel2:
			mTabHost.setCurrentTabByTag(TAB_TAG_GROUPBUY);
			addTab2Effect();
			break;
		case R.id.btn_channel3:
			mTabHost.setCurrentTabByTag(TAB_TAG_SHOPPING_CART);
			addTab3Effect();
			break;
		case R.id.btn_channel4:
			mTabHost.setCurrentTabByTag(TAB_TAG_FAVORITE);
			addTab4Effect();
			break;
		case R.id.btn_channel5:
			mTabHost.setCurrentTabByTag(TAB_TAG_MINE);
			addTab5Effect();
			break;
		default:
			break;
		}
		mCurTabId = checkedId;
	}

	private void prepareIntent() {
		mHomeItent = new Intent(this, HomeActivity.class);
		mHomeItent.putExtra("success",
				getIntent().getBooleanExtra("success", false));
		mMineIntent = new Intent(this, MineActivity.class);
	}

	private void clearFocusEffect() {
		mBut1.setImageResource(R.drawable.menu_home);
		mBut2.setImageResource(R.drawable.menu_group);
		mBut3.setImageResource(R.drawable.menu_cart);
		mBut4.setImageResource(R.drawable.menu_favorite);
		mBut5.setImageResource(R.drawable.menu_mine);
		mCateText1.setTextColor(R.color.green);
		mCateText2.setTextColor(R.color.green);
		mCateText3.setTextColor(R.color.green);
		mCateText4.setTextColor(R.color.green);
		mCateText5.setTextColor(R.color.green);
	}

	private void addTab1Effect() {
		mBut1.setImageResource(R.drawable.menu_home_select);
		mCateText1.setTextColor(R.color.manColor);
	}

	private void addTab2Effect() {
		mBut2.setImageResource(R.drawable.menu_group_select);
		mCateText2.setTextColor(R.color.manColor);
	}

	private void addTab3Effect() {
		mBut3.setImageResource(R.drawable.menu_cart_select);
		mCateText3.setTextColor(R.color.manColor);
	}

	private void addTab4Effect() {
		mBut4.setImageResource(R.drawable.menu_favorite_select);
		mCateText4.setTextColor(R.color.manColor);
	}

	private void addTab5Effect() {
		mBut5.setImageResource(R.drawable.menu_mine_select);
		mCateText5.setTextColor(R.color.manColor);
	}

	private void prepareView() {
		mBut1 = (ImageView) findViewById(R.id.imageView1);
		mBut2 = (ImageView) findViewById(R.id.imageView2);
		mBut3 = (ImageView) findViewById(R.id.imageView3);
		mBut4 = (ImageView) findViewById(R.id.imageView4);
		mBut5 = (ImageView) findViewById(R.id.imageView5);
		findViewById(R.id.btn_channel1).setOnClickListener(this);
		findViewById(R.id.btn_channel2).setOnClickListener(this);
		findViewById(R.id.btn_channel3).setOnClickListener(this);
		findViewById(R.id.btn_channel4).setOnClickListener(this);
		findViewById(R.id.btn_channel5).setOnClickListener(this);
		mCateText1 = (TextView) findViewById(R.id.textView1);
		mCateText2 = (TextView) findViewById(R.id.textView2);
		mCateText3 = (TextView) findViewById(R.id.textView3);
		mCateText4 = (TextView) findViewById(R.id.textView4);
		mCateText5 = (TextView) findViewById(R.id.textView5);
		cartCornerMark = (TextView) findViewById(R.id.cart_conner_mark);
		addTab1Effect();
	}

	private void setUpIntent() {
		mTabHost = getTabHost();
		mTabHost.addTab(buildTabSpec(TAB_TAG_HOME, R.string.category_home,
				R.drawable.menu_home, mHomeItent));
		mTabHost.addTab(buildTabSpec(TAB_TAG_MINE, R.string.category_mine,
				R.drawable.menu_mine, mMineIntent));
	}

	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return mTabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		ClientData.getInstance().recycle();
	}

	@Override
	public void finish() {
		
		super.finish();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {

				final CommonMsgDialog dialogBuilder = new CommonMsgDialog(
						MainActivity.this, R.style.Theme_Dialog);
				dialogBuilder.setDialogTitle(mContext.getResources().getString(
						R.string.str_tishi));
				dialogBuilder.setDialogMsg(mContext.getResources().getString(
						R.string.str_exit_msg));
				dialogBuilder.setOnCancelBtnListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						dialogBuilder.dismiss();
					}
				});

				dialogBuilder.setOnConfirmBtnListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						dialogBuilder.dismiss();
						MainActivity.this.finish();
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}

				});
				dialogBuilder.show();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
}
