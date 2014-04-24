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
import com.fornow.app.model.OrderList;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.MyListView;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase;
import com.fornow.app.ui.pull2refresh.PullToRefreshScrollView;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase.OnRefreshListener;
import com.fornow.app.util.GsonTool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class OrderListActivity extends Activity {
	private static final int LOADING_START = 0x00, LOADING_END = 0x01,
			NET_ERROR = 0x02;
	private Handler mHandler;
	private MyListView orderListView;
	private PullToRefreshScrollView mPullRefreshScrollView;
//	private ScrollView mScrollView;
	private List<OrderList> orderList;
	private Context context;
	private TextView orderTitle;
	public int status = 4; // default is get all order
	private requestType currentRequestType;
	private int offset = 0, length = 10;

	private enum requestType {
		MORE, REFRESH
	};

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		dialog = new LoadingAnim(OrderListActivity.this, R.style.my_dialog);
		context = this.getApplicationContext();
		orderListView = (MyListView) findViewById(R.id.listView02);
		orderTitle = (TextView) findViewById(R.id.order_title);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@SuppressLint("SimpleDateFormat")
					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						Date date = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy/MM/dd HH:mm");
						String label = getResources().getString(
								R.string.last_update_time);
						label += formatter.format(date);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						switch (refreshView.getCurrentMode()) {
						case PULL_FROM_START:
							new RefreshDataTask().execute();
							break;
						case PULL_FROM_END:
							new GetMoreDataTask().execute();
							break;
						default:
							break;
						}
					}
				});

//		mScrollView = mPullRefreshScrollView.getRefreshableView();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LOADING_START:
					break;
				case LOADING_END:
					String data = msg.getData().getString("data");
					if (data != null) {
						List<OrderList> responseData = GsonTool.getGsonTool()
								.fromJson(data,
										new TypeToken<List<OrderList>>() {
										}.getType());
						if (currentRequestType == requestType.REFRESH) {
							orderList = responseData;
						} else if (currentRequestType == requestType.MORE) {
							int responseSize = responseData.size(), i;
							for (i = 0; i < responseSize; i++) {
								orderList.add(responseData.get(i));
							}
						}
						OrderAdapter adapter = new OrderAdapter(orderList,
								context);
						orderListView.setAdapter(adapter);
					}

					if (mPullRefreshScrollView != null) {
						// Call onRefreshComplete when the list has been
						// refreshed.
						mPullRefreshScrollView.onRefreshComplete();
					}
					dialog.dismiss();
					break;
				case NET_ERROR:
					dialog.dismiss();
					if (mPullRefreshScrollView != null) {
						// Call onRefreshComplete when the list has been
						// refreshed.
						mPullRefreshScrollView.onRefreshComplete();
					}
					View view = getLayoutInflater().inflate(R.layout.my_toast,
							null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					Toast toast = new Toast(OrderListActivity.this);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				default:
					break;
				}
			}
		};
		if (getIntent().getExtras() != null) {
			status = getIntent().getExtras().getInt("status");
			switch (status) {
			case 0:
				orderTitle.setText(getResources().getString(
						R.string.str_yxd_order));
				break;
			case 1:
				orderTitle.setText(getResources().getString(
						R.string.str_ysl_order));
				break;
			case 2:
				orderTitle.setText(getResources().getString(
						R.string.str_psz_order));
				break;
			case 3:
				orderTitle.setText(getResources().getString(
						R.string.str_finish_order));
				break;
			case 4:
				orderTitle.setText(getResources().getString(
						R.string.str_all_order));
				break;
			case -1: // 取消订单
				break;
			default:
				break;
			}
			dialog.show();
			getOrder(requestType.REFRESH, status);
		}
	}

	public void getOrder(requestType type, int status) {
		currentRequestType = type;
		Message updateViewMsg = mHandler.obtainMessage(LOADING_START);
		mHandler.sendMessage(updateViewMsg);
		ControllerManager.getInstance().getOrderController().unRegisterAll();
		ControllerManager.getInstance().getOrderController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						if (obj.getCode() == 200) {
							Message updateViewMsg = mHandler
									.obtainMessage(LOADING_END);
							updateViewMsg.getData().putString("data",
									obj.getData());
							mHandler.sendMessage(updateViewMsg);
						} else {
							Message updateViewMsg = mHandler
									.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
						}

					}
				});

		switch (type) {
		case REFRESH:
			offset = 0;
			break;
		case MORE:
			offset += length;
			break;
		default:
			break;
		}
		ControllerManager.getInstance().getOrderController()
				.getOrder(offset, length, status);
	}

	@Override
	protected void onStart() {
		super.onStart();
		orderListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("TAG", "---------------position:" + position);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private class RefreshDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			getOrder(requestType.REFRESH, status);
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			// mPullRefreshScrollView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	private class GetMoreDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			getOrder(requestType.MORE, status);
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			// mPullRefreshScrollView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void softBack(View v) {
		this.finish();
	}

}
