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
package com.fornow.app.ui.groupbuy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.GroupListData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.MyListView;
import com.fornow.app.ui.goodsdetail.GoodDetailActivity;
import com.fornow.app.ui.main.BaseMainActivity;
import com.fornow.app.utils.GsonTool;
import com.fornow.app.utils.pull2refresh.PullToRefreshBase;
import com.fornow.app.utils.pull2refresh.PullToRefreshScrollView;
import com.fornow.app.utils.pull2refresh.PullToRefreshBase.OnRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.fornow.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Simon Lv 2013-11-11
 */
public class GroupBuyActivity extends BaseMainActivity implements ViewListener {
	private int offset = 0, length = 10;
	private static final int LOADING_START = 0x00, LOADING_END = 0x01,
			NET_ERROR = 0x02;
	private List<GroupListData> groupData = null;
	private Handler mHandler;
	private Context mContext;
	private MyListView listView;
	private GroupListAdapter mAdapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	@SuppressWarnings("unused")
	private ScrollView mScrollView;
	private requestType currentRequestType;
	private String category = "all";
	Animation clockwiseAnimation, anticlockwiseAnimation;
	TranslateAnimation mShowAction, mHiddenAction;

	private enum requestType {
		MORE, REFRESH
	};

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		mContext = this.getApplicationContext();
		prepareView();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LOADING_START:
					break;
				case LOADING_END:
					String data = msg.getData().getString("data");
					if (data != null) {
						try {
							List<GroupListData> responseData = GsonTool
									.getGsonTool()
									.fromJson(
											data,
											new TypeToken<List<GroupListData>>() {
											}.getType());
							if (currentRequestType == requestType.REFRESH) {
								groupData = responseData;
							} else if (currentRequestType == requestType.MORE) {
								int responseSize = responseData.size(), i;
								for (i = 0; i < responseSize; i++) {
									groupData.add(responseData.get(i));
								}
							}
							mAdapter = new GroupListAdapter(groupData, mContext);
							listView.setAdapter(mAdapter);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					if (mPullRefreshScrollView != null) {
						// Call onRefreshComplete when the list has been
						// refreshed.
						mPullRefreshScrollView.onRefreshComplete();
					}
					ControllerManager.getInstance().getSearchController()
							.unRegisterNotification(GroupBuyActivity.this);
					break;
				case NET_ERROR:
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
					Toast toast = new Toast(GroupBuyActivity.this);
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

		sendRequest(requestType.REFRESH);
	}

	public void prepareView() {
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f);
		mHiddenAction.setDuration(500);
		clockwiseAnimation = AnimationUtils.loadAnimation(this,
				R.anim.clockwise_anim);
		clockwiseAnimation.setFillAfter(true);
		anticlockwiseAnimation = AnimationUtils.loadAnimation(this,
				R.anim.anticlockwise_anim);
		anticlockwiseAnimation.setFillAfter(true);
		listView = (MyListView) findViewById(R.id.listView01);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				try {
					GroupListData data = groupData.get(arg2);
					GoodsDetailData detail = new GoodsDetailData();
					if (data.getId() != null) {
						detail.setId(data.getId());
					}
					if (data.getName() != null) {
						detail.setName(data.getName());
					}
					if (data.getCategory() != null) {
						detail.setCategory(data.getCategory());
					}
					if (data.getIcon() != null) {
						detail.setIcon(data.getIcon());
					}
					if (data.getImage() != null) {
						detail.setImage(data.getImage());
					}
					if (data.getOriginal_price() != null) {
						detail.setOriginal_price(data.getOriginal_price());
					}
					if (data.getCurrent_price() != null) {
						detail.setCurrent_price(data.getCurrent_price());
					}
					if (data.getIntroduction() != null) {
						detail.setIntroduction(data.getIntroduction());
					}
					if (data.getStart_time() != null) {
						detail.setStart_time(data.getStart_time());
					}
					if (data.getEnd_time() != null) {
						detail.setEnd_time(data.getEnd_time());
					}
					if (data.getDeliver_area() != null) {
						detail.setDeliver_area(data.getDeliver_area());
					}
					if (data.getMax_count() != 0) {
						detail.setMax_count(data.getMax_count());
					}

					String strDetail = GsonTool.getGsonTool().toJson(detail);
					Intent intent = new Intent(GroupBuyActivity.this,
							GoodDetailActivity.class);
					intent.putExtra("data", strDetail);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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

		mScrollView = mPullRefreshScrollView.getRefreshableView();
	}


	@Override
	protected void onStart() {
		
		super.onStart();

	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	public void sendRequest(requestType type) {
		currentRequestType = type;
		Message updateViewMsg = mHandler.obtainMessage(LOADING_START);
		mHandler.sendMessage(updateViewMsg);
		ControllerManager.getInstance().getSearchController().unRegisterAll();
		ControllerManager.getInstance().getSearchController()
				.registerNotification(GroupBuyActivity.this);
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
		ControllerManager.getInstance().getSearchController()
				.getGroupList(offset, length, category);
	}



	private class RefreshDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			sendRequest(requestType.REFRESH);
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
			sendRequest(requestType.MORE);
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
	public void updateView(ViewUpdateObj obj) {
		
		if (obj.getCode() == 200) {
			if (obj.getData() != null) {
				Message updateViewMsg = mHandler.obtainMessage(LOADING_END);
				updateViewMsg.getData().putString("data", obj.getData());
				mHandler.sendMessage(updateViewMsg);
			}
		} else {
			Message updateViewMsg = mHandler.obtainMessage(NET_ERROR);
			mHandler.sendMessage(updateViewMsg);
		}
	}
}
