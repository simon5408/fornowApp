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

import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.UserInfo;
import com.fornow.app.ui.addressmanager.ShdzActivity;
import com.fornow.app.ui.favorite.FavoriteActivity;
import com.fornow.app.ui.main.BaseMainActivity;
import com.fornow.app.ui.ordermanager.OrderListActivity;
import com.fornow.app.ui.setting.SettingActivity;
import com.fornow.app.ui.setting.YjfkActivity;
import com.fornow.app.utils.GsonTool;
import com.fornow.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @author Simon Lv 2013-8-4
 */
public class MineActivity extends BaseMainActivity {
	private TextView mineNameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine);
		mineNameView = (TextView) findViewById(R.id.mine_name);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	// 设置
	public void getInSettings(View v) {
		Intent intent = new Intent(MineActivity.this, SettingActivity.class);
		startActivity(intent);
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
}
