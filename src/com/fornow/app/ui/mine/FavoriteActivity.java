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
import com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.MyListView;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase;
import com.fornow.app.ui.pull2refresh.PullToRefreshScrollView;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase.OnRefreshListener;
import com.fornow.app.ui.search.GoodDetailActivity;
import com.fornow.app.util.GsonTool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class FavoriteActivity extends Activity {
	private Handler mHandler;
	private PullToRefreshScrollView mPullRefreshScrollView;
	@SuppressWarnings("unused")
	private ScrollView mScrollView;
	private List<GoodsDetailData> favoritesData = null;
	public static final int LOADING_START = 0x00, LOADING_END = 0x01,
			NET_ERROR = 0x02, FAV_CLICKED = 0x03, FAV_DEL = 0x04,
			FAV_DEL_SUCCESS = 0x05;
	private MyListView listView;
	private FavoriteAdapter favoriteAdapter;
	private Context mContext;
	AnimateDismissAdapter<String> animateDismissAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this.getApplicationContext();
		setContentView(R.layout.favorite_list);
		initHandler();
		listView = (MyListView) findViewById(R.id.listView01);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@SuppressLint("SimpleDateFormat")
					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// TODO Auto-generated method stub
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
						default:
							break;
						}
					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
	}

	public void loadData() {
		String favData = ClientData.getInstance().getmFavorites();
		if (favData != null) {
			try {
				favoritesData = GsonTool.getGsonTool().fromJson(favData,
						new TypeToken<List<GoodsDetailData>>() {
						}.getType());
				Log.d("TAG", "===============favorite data:"
						+ GsonTool.getGsonTool().toJson(favoritesData));
				favoriteAdapter = new FavoriteAdapter(favoritesData, mHandler,
						mContext);
				animateDismissAdapter = new AnimateDismissAdapter<String>(
						favoriteAdapter, new MyOnDismissCallback());
				animateDismissAdapter.setListView(listView);
				listView.setAdapter(animateDismissAdapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String data;
				int pos = 0;
				switch (msg.what) {
				case LOADING_END:
					if (mPullRefreshScrollView != null) {
						// Call onRefreshComplete when the list has been
						// refreshed.
						mPullRefreshScrollView.onRefreshComplete();
					}
					data = msg.getData().getString("data");
					if (data != null) {
						try {
							List<GoodsDetailData> responseData = GsonTool
									.getGsonTool()
									.fromJson(
											data,
											new TypeToken<List<GoodsDetailData>>() {
											}.getType());
							favoritesData = responseData;
							Log.d("TAG",
									"===============favorite data:"
											+ GsonTool.getGsonTool().toJson(
													favoritesData));
							favoriteAdapter.notifyDataSetChanged();
							animateDismissAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

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
					Toast toast = new Toast(FavoriteActivity.this);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				case FAV_CLICKED:
					data = msg.getData().getString("data");
					go2detail(data);
					break;
				case FAV_DEL:
					pos = Integer.valueOf(msg.getData().getString("position"));
					animateDismissAdapter.animateDismiss(pos);
					delFavorite(favoritesData.get(pos).getId());
					break;
				case FAV_DEL_SUCCESS:
					//animateDismissAdapter.animateDismiss(pos);
					//sendRequest();
					break;
				default:
					break;
				}
			}
		};
	}

	public void go2detail(String detail) {
		if (detail != null) {
			Intent intent = new Intent(FavoriteActivity.this,
					GoodDetailActivity.class);
			intent.putExtra("data", detail);
			startActivity(intent);
		}
	}

	public void delFavorite(String favId) {
		ControllerManager.getInstance().getFavoritesController()
				.unRegisterAll();
		ControllerManager.getInstance().getFavoritesController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						// TODO Auto-generated method stub
						if (obj.getCode() == 200) {
							Message updateViewMsg = mHandler
									.obtainMessage(FAV_DEL_SUCCESS);
							mHandler.sendMessage(updateViewMsg);
						} else {
							Message updateViewMsg = mHandler
									.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
						}
					}
				});
		ControllerManager.getInstance().getFavoritesController()
				.delFromFav(favId);
	}

	private class MyOnDismissCallback implements OnDismissCallback {

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				 favoritesData.remove(position);
				 favoriteAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadData();
	}

	public void sendRequest() {
		ControllerManager.getInstance().getFavoritesController()
				.unRegisterAll();
		ControllerManager.getInstance().getFavoritesController()
				.registerNotification(new ViewListener() {
					@Override
					public void updateView(ViewUpdateObj obj) {
						// TODO Auto-generated method stub
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

		ControllerManager.getInstance().getFavoritesController().getData();
	}

	private class RefreshDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			sendRequest();
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
			sendRequest();
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

	public void softBack(View v) {
		this.finish();
	}

}
