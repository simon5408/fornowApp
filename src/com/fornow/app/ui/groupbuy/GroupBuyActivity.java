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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.GroupListData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.MyListView;
import com.fornow.app.ui.main.BaseMainActivity;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase.OnRefreshListener;
import com.fornow.app.ui.pull2refresh.PullToRefreshScrollView;
import com.fornow.app.ui.search.GoodDetailActivity;
import com.fornow.app.util.GsonTool;
import com.google.gson.reflect.TypeToken;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class GroupBuyActivity extends BaseMainActivity implements ViewListener {
	private LinearLayout overContainerView, overView, fenleiView, sortView;
	private ImageView fenleiArrow, sortArrow;
	private boolean displayFenlei = false;
	private boolean displaySort = false;
	private int offset = 0, length = 10;
	private static final int LOADING_START = 0x00, LOADING_END = 0x01,
			NET_ERROR = 0x02;
	private List<GroupListData> groupData = null;
	private Handler mHandler;
	private Context mContext;
	private MyListView listView;
	private TextView fenleiText;
	private ListView fenleiList, sortList;
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
		overContainerView = (LinearLayout) findViewById(R.id.over_container_view);
		overView = (LinearLayout) findViewById(R.id.over_view);
		fenleiView = (LinearLayout) findViewById(R.id.fenlei_view);
		sortView = (LinearLayout) findViewById(R.id.sort_view);
		fenleiText = (TextView) findViewById(R.id.fenlei_text);
		// sortText = (TextView) findViewById(R.id.sort_text);
		fenleiArrow = (ImageView) findViewById(R.id.fenlei_arrow);
		sortArrow = (ImageView) findViewById(R.id.sort_arrow);
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
					// String detailData = GsonTool.getGsonTool().toJson(
					// groupData.get(arg2), GroupListData.class);
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

		initFenleiFilter();
		initSortFilter();
	}

	public void initFenleiFilter() {
		fenleiList = (ListView) findViewById(R.id.fenlei_list);
		List<HashMap<String, Object>> fenleiListData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.str_all));
		map.put("icon", R.drawable.fenlei_all);
		fenleiListData.add(map);
		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.str_fruit));
		map.put("icon", R.drawable.fenlei_fruit);
		fenleiListData.add(map);
		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.str_vegetable));
		map.put("icon", R.drawable.fenlei_vegetable);
		fenleiListData.add(map);
		SimpleAdapter adapter = new SimpleAdapter(this, fenleiListData,
				R.layout.group_filter_item, new String[] { "icon", "text" },
				new int[] { R.id.filter_icon, R.id.filter_text });
		fenleiList.setAdapter(adapter);
		fenleiList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					category = "all";
					fenleiText.setText(getResources().getString(
							R.string.str_all));
					break;
				case 1:
					category = "fruit";
					fenleiText.setText(getResources().getString(
							R.string.str_fruit));
					break;
				case 2:
					category = "vegetable";
					fenleiText.setText(getResources().getString(
							R.string.str_vegetable));
					break;
				default:
					break;
				}
				sendRequest(requestType.REFRESH);
				fenleiClick(null);
			}
		});
	}

	public void initSortFilter() {
		sortList = (ListView) findViewById(R.id.sort_list);
		List<HashMap<String, Object>> sortListData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.str_tgsysj));
		sortListData.add(map);
		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.str_group_price));
		sortListData.add(map);
		SimpleAdapter adapter = new SimpleAdapter(this, sortListData,
				R.layout.group_sort_filter_item, new String[] { "text" },
				new int[] { R.id.filter_text });
		sortList.setAdapter(adapter);
		sortList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:// 时间
					if (groupData != null && groupData.size() > 0) {
						Collections.sort(groupData,
								new Comparator<GroupListData>() {

									@Override
									public int compare(GroupListData args0,
											GroupListData args1) {
										return Long
												.valueOf(args0.getEnd_time())
												.compareTo(
														Long.valueOf(args1
																.getEnd_time()));
									}
								});
						mAdapter.notifyDataSetChanged();
					}
					break;
				case 1:// 价格
					if (groupData != null && groupData.size() > 0) {
						Collections.sort(groupData,
								new Comparator<GroupListData>() {

									@Override
									public int compare(GroupListData args0,
											GroupListData args1) {
										return args0
												.getCurrent_price()
												.compareTo(
														args1.getCurrent_price());
									}
								});
						mAdapter.notifyDataSetChanged();
					}
					break;
				default:
					break;
				}
				sortClick(null);
			}
		});
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

	public void fenleiClick(View v) {
		sortArrow.clearAnimation();
		if (!displayFenlei) {
			displayFenlei = true;
			overContainerView.setVisibility(View.VISIBLE);
			overView.setVisibility(View.VISIBLE);
			fenleiView.startAnimation(mShowAction);
			fenleiView.setVisibility(View.VISIBLE);
			fenleiArrow.startAnimation(clockwiseAnimation);
			if (displaySort) {
				displaySort = false;
				sortView.startAnimation(mHiddenAction);
				sortView.setVisibility(View.INVISIBLE);
				sortArrow.startAnimation(anticlockwiseAnimation);
			}
		} else {
			displayFenlei = false;
			fenleiView.startAnimation(mHiddenAction);
			fenleiView.setVisibility(View.INVISIBLE);
			overView.startAnimation(mHiddenAction);
			overView.setVisibility(View.GONE);
			overContainerView.setVisibility(View.GONE);
			fenleiArrow.startAnimation(anticlockwiseAnimation);
		}
	}

	public void sortClick(View v) {
		fenleiArrow.clearAnimation();
		if (!displaySort) {
			displaySort = true;
			overContainerView.setVisibility(View.VISIBLE);
			overView.setVisibility(View.VISIBLE);
			sortView.startAnimation(mShowAction);
			sortView.setVisibility(View.VISIBLE);
			sortArrow.startAnimation(clockwiseAnimation);
			if (displayFenlei) {
				displayFenlei = false;
				fenleiView.startAnimation(mHiddenAction);
				fenleiView.setVisibility(View.INVISIBLE);
				fenleiArrow.startAnimation(anticlockwiseAnimation);
			}
		} else {
			displaySort = false;
			sortView.startAnimation(mHiddenAction);
			sortView.setVisibility(View.INVISIBLE);
			overView.startAnimation(mHiddenAction);
			overView.setVisibility(View.GONE);
			overContainerView.setVisibility(View.GONE);
			sortArrow.startAnimation(anticlockwiseAnimation);
		}
	}

	public void fenleiAll(View v) {
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
