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
package com.fornow.app.ui.mine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.OrderList;
import com.fornow.app.model.UserInfo;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.AppClass;
import com.fornow.app.ui.MyListView;
import com.fornow.app.ui.main.BaseMainActivity;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase;
import com.fornow.app.ui.pull2refresh.PullToRefreshScrollView;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase.OnRefreshListener;
import com.fornow.app.util.GsonTool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class MineActivity extends BaseMainActivity {
	private TextView mineNameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine);
		mineNameView = (TextView) findViewById(R.id.mine_name);

		if (ClientData.getInstance().getUser() != null) {
			String user = ClientData.getInstance().getUser();
			UserInfo userInfo = GsonTool.getGsonTool().fromJson(user,
					UserInfo.class);
			if (userInfo.getUser_name() != null) {
				mineNameView.setText(userInfo.getUser_name());
			} else {
				mineNameView.setText(userInfo.getUser_account());
			}
		}
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
	}

	// 收货地址
	public void getInShdz(View v) {
		Intent intent = new Intent(MineActivity.this, ShdzActivity.class);
		startActivity(intent);
	}

	// 个人信息
	public void getInGrxx(View v) {
		Intent intent = new Intent(MineActivity.this, GrxxActivity.class);
		startActivity(intent);
	}

	// 我的收藏
	public void getInWdsc(View v) {
		Intent intent = new Intent(MineActivity.this, FavoriteActivity.class);
		startActivity(intent);
	}

	// 意见反馈
	public void getInYjfk(View v) {
		Intent intent = new Intent(MineActivity.this, YjfkActivity.class);
		startActivity(intent);
	}

	// 全部订单
	public void getAllOrder(View v) {
		Intent intent = new Intent(MineActivity.this, OrderListActivity.class);
		intent.putExtra("status", 4);
		startActivity(intent);
	}

	// 已下单
	public void getYxdOrder(View v) {
		Intent intent = new Intent(MineActivity.this, OrderListActivity.class);
		intent.putExtra("status", 0);
		startActivity(intent);
	}

	// 已受理
	public void getYslOrder(View v) {
		Intent intent = new Intent(MineActivity.this, OrderListActivity.class);
		intent.putExtra("status", 1);
		startActivity(intent);
	}

	// 配送中
	public void getPszOrder(View v) {
		Intent intent = new Intent(MineActivity.this, OrderListActivity.class);
		intent.putExtra("status", 2);
		startActivity(intent);
	}

	// 已完成
	public void getYwcOrder(View v) {
		Intent intent = new Intent(MineActivity.this, OrderListActivity.class);
		intent.putExtra("status", 3);
		startActivity(intent);
	}

	public void logout(View v) {
		AlertDialog.Builder builder = new Builder(MineActivity.this);
		builder.setMessage("确定要退出当前用户吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Message updateViewMsg = AppClass.globalHandler
								.obtainMessage(AppClass.LOGOUT);
						AppClass.globalHandler.sendMessage(updateViewMsg);
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
}
