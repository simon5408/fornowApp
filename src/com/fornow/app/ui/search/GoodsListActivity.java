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
package com.fornow.app.ui.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.GoodsListData;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IViewListener;
import com.fornow.app.ui.GridViewImgAdapter;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.MyGridView;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase.OnRefreshListener;
import com.fornow.app.ui.pull2refresh.PullToRefreshScrollView;
import com.fornow.app.util.GsonTool;
import com.google.gson.reflect.TypeToken;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class GoodsListActivity extends Activity implements OnItemClickListener,
		IViewListener {
	private static final String TAG = "FruitList";
	private MyGridView gridview;
	private TextView channel;
	private Dialog dialog;
	private Handler mHandler;
	private PullToRefreshScrollView mPullRefreshScrollView;
	@SuppressWarnings("unused")
	private ScrollView mScrollView;
	private String category;
	private List<GoodsListData> goodsData = null;
	private static final int LOADING_START = 0x00, LOADING_END = 0x01,
			NET_ERROR = 0x02;
	private int offset = 0, length = 10;
	private requestType currentRequestType;

	private enum requestType {
		MORE, REFRESH
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_list);
		category = getIntent().getExtras().getString("type");
		channel = (TextView) findViewById(R.id.channel_category);
		gridview = (MyGridView) findViewById(R.id.homeMenuGridView);
		gridview.getLayoutParams().height = gridview.getWidth();
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

		dialog = new LoadingAnim(GoodsListActivity.this, R.style.my_dialog);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth() * 0.75);
		lp.height = (int) (display.getHeight() * 0.25);
		dialog.getWindow().setAttributes(lp);
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void onStart() {
		super.onStart();
		if (category.equals("fruit")) {
			channel.setText(R.string.fruit_channel);
		} else if (category.equals("vegetable")) {
			channel.setText(R.string.vegetable_channel);
		}
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LOADING_START:
					dialog.show();
					break;
				case LOADING_END:
					String data = msg.getData().getString("data");
					if (data != null) {
						try {
							List<GoodsListData> responseData = GsonTool
									.fromJson(
											data,
											new TypeToken<List<GoodsListData>>() {
											});
							if (currentRequestType == requestType.REFRESH) {
								goodsData = responseData;
							} else if (currentRequestType == requestType.MORE) {
								int responseSize = responseData.size(), i;
								for (i = 0; i < responseSize; i++) {
									goodsData.add(responseData.get(i));
								}
							}

							Log.v(TAG,
									"goodsData: "
											+ GsonTool.toJson(
													goodsData.get(0),
													GoodsListData.class));
							ArrayList<HashMap<String, Object>> tuijianList = new ArrayList<HashMap<String, Object>>();
							for (GoodsListData d : goodsData) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("GoodsName",
										getResources().getString(
												R.string.goods_name_tag)
												+ d.getName());
								map.put("GoodsCurrentPrice",
										getResources().getString(
												R.string.goods_unit)
												+ d.getCurrent_price());
								map.put("GoodsOriginPrice",
										getResources().getString(
												R.string.goods_unit)
												+ d.getCurrent_price()
												+ getResources().getString(
														R.string.sell_unit));
								// map.put("ItemImage",
								// R.drawable.tuijian_sample);
								map.put("ImgId", d.getIcon().getId());
								map.put("ImgUrl", d.getIcon().getUrl());
								tuijianList.add(map);
							}
							GridViewImgAdapter adapter = new GridViewImgAdapter(
									GoodsListActivity.this, tuijianList);
							gridview.setAdapter(adapter);
							gridview.setOnItemClickListener(GoodsListActivity.this);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					dialog.dismiss();
					if (mPullRefreshScrollView != null) {
						// Call onRefreshComplete when the list has been
						// refreshed.
						mPullRefreshScrollView.onRefreshComplete();
					}
					ControllerManager.getInstance().getSearchController()
							.unRegisterNotification(GoodsListActivity.this);
					break;
				case NET_ERROR:
					dialog.dismiss();
					if (mPullRefreshScrollView != null) {
						// Call onRefreshComplete when the list has been
						// refreshed.
						mPullRefreshScrollView.onRefreshComplete();
					}
					View view = getLayoutInflater().inflate(
							R.layout.my_toast, null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					Toast toast = new Toast(GoodsListActivity.this);
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
		ControllerManager.getInstance().getSearchController().unRegisterAll();
		sendRequest(category, requestType.REFRESH);
	}

	public void sendRequest(String category, requestType type) {
		currentRequestType = type;
		Message updateViewMsg = mHandler.obtainMessage(LOADING_START);
		mHandler.sendMessage(updateViewMsg);

		ControllerManager.getInstance().getSearchController()
				.registerNotification(GoodsListActivity.this);
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
				.getGoodsList(offset, length, category);
	}

	public void softBack(View v) {
		ControllerManager.getInstance().getSearchController().unRegisterAll();
		this.finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String detailData = GsonTool.toJson(
				goodsData.get(position), GoodsListData.class);
		if (detailData != null) {
			GoodsListData data = goodsData.get(position);
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
			if (data.getDeliver_area() != null) {
				detail.setDeliver_area(data.getDeliver_area());
			}

			detail.setSell_out(data.getSell_out());
			detail.setMax_count(data.getMax_count());
			String strDetail = GsonTool.toJson(detail);
			Log.v(TAG, "jinrituijian detail: " + detailData);
			Intent intent = new Intent(GoodsListActivity.this,
					GoodDetailActivity.class);
			intent.putExtra("data", strDetail);
			startActivity(intent);
		}
	}

	private class RefreshDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			sendRequest(category, requestType.REFRESH);
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
			sendRequest(category, requestType.MORE);
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
			Message updateViewMsg = mHandler.obtainMessage(LOADING_END);
			updateViewMsg.getData().putString("data", obj.getData());
			mHandler.sendMessage(updateViewMsg);
		} else {
			Message updateViewMsg = mHandler.obtainMessage(NET_ERROR);
			mHandler.sendMessage(updateViewMsg);
		}
	}

}
