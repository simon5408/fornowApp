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

import java.util.List;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.fornow.app.R;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.ui.AppClass;
import com.fornow.app.ui.groupbuy.GroupBuyActivity;
import com.fornow.app.ui.home.HomeActivity;
import com.fornow.app.ui.mine.LoginActivity;
import com.fornow.app.ui.mine.MineActivity;
import com.fornow.app.ui.shopcart.CartDataHelper;
import com.fornow.app.ui.shopcart.ShopCartActivity;
import com.fornow.app.util.GsonTool;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class MainActivity extends TabActivity implements OnClickListener {
	public static String TAB_TAG = "tabTag";
	public static String TAB_TAG_HOME = "home";
	public static String TAB_TAG_GROUPBUY = "groupBuy";
	public static String TAB_TAG_SHOPPING_CART = "shopCart";
	public static String TAB_TAG_MINE = "mine";
	private static TabHost mTabHost;
	ImageView mBut1, mBut2, mBut3, mBut4;
	TextView mCateText1, mCateText2, mCateText3, mCateText4, cartCornerMark;
	Intent mHomeItent, mGroupBuyIntent, mShopCartIntent, mMineIntent;

	int mCurTabId = R.id.channel1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		prepareIntent();
		prepareView();
		setUpIntent();
		AppClass.globalHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AppClass.UPDATE_CART_COUNT:
					updateCartCorner();
					break;
				case AppClass.LOGOUT:
					CartDataHelper.syncCart();
					ClientData.getInstance().recycle();
					updateCartCorner();
					stayOnWhichTab(TAB_TAG_HOME);
				default:
					break;
				}
			}
		};
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = getIntent();
		if (intent.getExtras() != null
				&& intent.getExtras().get(TAB_TAG) != null) {
			String tag = intent.getExtras().get(TAB_TAG).toString();
			stayOnWhichTab(tag);
		}
		updateCartCorner();
	}

	public void updateCartCorner() {
		String cart = ClientData.getInstance().getmCart();
		if (cart != null) {
			try {
				List<ShopCart> cartObj = GsonTool.getGsonTool().fromJson(cart,
						new TypeToken<List<ShopCart>>() {
						}.getType());
				int count = cartObj.size();
				if (count > 0) {
					cartCornerMark.setText(count + "");
					cartCornerMark.setVisibility(View.VISIBLE);
				} else {
					cartCornerMark.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			cartCornerMark.setVisibility(View.GONE);
		}

	}

	public void stayOnWhichTab(String tag) {
		// clean the effects
		findViewById(R.id.channel1).setBackgroundResource(0);
		findViewById(R.id.channel2).setBackgroundResource(0);
		findViewById(R.id.channel3).setBackgroundResource(0);
		findViewById(R.id.channel4).setBackgroundResource(0);
		if (tag.equals(TAB_TAG_HOME)) {
			mCurTabId = R.id.channel1;
			mTabHost.setCurrentTabByTag(TAB_TAG_HOME);
			findViewById(R.id.channel1).setBackgroundResource(
					R.drawable.footer_left_act_bg);
		} else if (tag.equals(TAB_TAG_GROUPBUY)) {
			mCurTabId = R.id.channel2;
			mTabHost.setCurrentTabByTag(TAB_TAG_GROUPBUY);
			findViewById(R.id.channel2).setBackgroundResource(
					R.drawable.footer_middle);
		} else if (tag.equals(TAB_TAG_SHOPPING_CART)) {
			mCurTabId = R.id.channel3;
			mTabHost.setCurrentTabByTag(TAB_TAG_SHOPPING_CART);
			findViewById(R.id.channel3).setBackgroundResource(
					R.drawable.footer_middle);
		} else if (tag.equals(TAB_TAG_MINE)) {
			mCurTabId = R.id.channel4;
			mTabHost.setCurrentTabByTag(TAB_TAG_MINE);
			findViewById(R.id.channel4).setBackgroundResource(
					R.drawable.footer_right_act_bg);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			stayOnWhichTab(TAB_TAG_MINE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Log.v("1111", "TODO v.getId() is: " + v.getId());
		int checkedId = v.getId();
		if (mCurTabId == v.getId()) {
			return;
		}

		if (checkedId == R.id.btn_channel4) {

			if (ClientData.getInstance().getmUUID() == null) {

				Intent in = new Intent(MainActivity.this, LoginActivity.class);

				startActivityForResult(in, 0);

				return;

			}
		}

		// clean the effects
		findViewById(R.id.channel1).setBackgroundResource(0);
		findViewById(R.id.channel2).setBackgroundResource(0);
		findViewById(R.id.channel3).setBackgroundResource(0);
		findViewById(R.id.channel4).setBackgroundResource(0);

		switch (checkedId) {
		case R.id.btn_channel1:
			mTabHost.setCurrentTabByTag(TAB_TAG_HOME);
			findViewById(R.id.channel1).setBackgroundResource(
					R.drawable.footer_left_act_bg);
			break;
		case R.id.btn_channel2:
			mTabHost.setCurrentTabByTag(TAB_TAG_GROUPBUY);
			findViewById(R.id.channel2).setBackgroundResource(
					R.drawable.footer_middle);
			break;
		case R.id.btn_channel3:
			mTabHost.setCurrentTabByTag(TAB_TAG_SHOPPING_CART);
			findViewById(R.id.channel3).setBackgroundResource(
					R.drawable.footer_middle);
			break;
		case R.id.btn_channel4:
			mTabHost.setCurrentTabByTag(TAB_TAG_MINE);
			findViewById(R.id.channel4).setBackgroundResource(
					R.drawable.footer_right_act_bg);
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
		mGroupBuyIntent = new Intent(this, GroupBuyActivity.class);
		mShopCartIntent = new Intent(this, ShopCartActivity.class);
		mMineIntent = new Intent(this, MineActivity.class);
	}

	private void prepareView() {
		mBut1 = (ImageView) findViewById(R.id.imageView1);
		mBut2 = (ImageView) findViewById(R.id.imageView2);
		mBut3 = (ImageView) findViewById(R.id.imageView3);
		mBut4 = (ImageView) findViewById(R.id.imageView4);
		findViewById(R.id.btn_channel1).setOnClickListener(this);
		findViewById(R.id.btn_channel2).setOnClickListener(this);
		findViewById(R.id.btn_channel3).setOnClickListener(this);
		findViewById(R.id.btn_channel4).setOnClickListener(this);
		mCateText1 = (TextView) findViewById(R.id.textView1);
		mCateText2 = (TextView) findViewById(R.id.textView2);
		mCateText3 = (TextView) findViewById(R.id.textView3);
		mCateText4 = (TextView) findViewById(R.id.textView4);
		cartCornerMark = (TextView) findViewById(R.id.cart_conner_mark);
	}

	private void setUpIntent() {
		mTabHost = getTabHost();
		mTabHost.addTab(buildTabSpec(TAB_TAG_HOME, R.string.category_home,
				R.drawable.menu_home, mHomeItent));
		mTabHost.addTab(buildTabSpec(TAB_TAG_GROUPBUY,
				R.string.category_group_buy, R.drawable.menu_group,
				mGroupBuyIntent));
		mTabHost.addTab(buildTabSpec(TAB_TAG_SHOPPING_CART,
				R.string.category_shopcart, R.drawable.menu_cart,
				mShopCartIntent));
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
		// TODO Auto-generated method stub
		super.onDestroy();
		CartDataHelper.syncCart();
		ClientData.getInstance().recycle();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){  
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) { 
            	AlertDialog.Builder builder = new Builder(MainActivity.this);
    			builder.setMessage("确定要退出吗?");
    			builder.setTitle("提示");
    			builder.setPositiveButton("确认",
    					new android.content.DialogInterface.OnClickListener() {
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    							dialog.dismiss();
    							MainActivity.this.finish();
    							android.os.Process.killProcess(android.os.Process
    									.myPid());
    						}
    					});
    			builder.setNegativeButton("取消",
    					new android.content.DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int which) {
    							dialog.dismiss();
    						}
    					});
    			builder.create().show();
             }  
            return true;  
       }  
		return super.dispatchKeyEvent(event);
	}
}
