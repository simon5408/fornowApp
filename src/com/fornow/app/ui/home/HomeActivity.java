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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.GoodsListData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.GridViewImgAdapter;
import com.fornow.app.ui.MyGridView;
import com.fornow.app.ui.goodsdetail.GoodDetailActivity;
import com.fornow.app.ui.loadimg.AsyncImgLoader;
import com.fornow.app.ui.loadimg.AsyncImgLoader.ImageCallback;
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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Simon Lv 2013-8-4
 */
public class HomeActivity extends BaseMainActivity implements
		OnItemClickListener, ViewListener {

	private static final String TAG = "HOMEACTIVITY";
	private LinearLayout homeContainer;
	private MyGridView gridview;
	private AutoPlayGallery banner;
	private ArrayList<Drawable> drawables;
	private PullToRefreshScrollView mPullRefreshScrollView;
	@SuppressWarnings("unused")
	private ScrollView mScrollView;
	@SuppressWarnings("unused")
	private Context mContext;
	private BoolLoadComplete boolLoadComplete;
	private Handler mHandler;
	private static final int BANNER_COMPLETED = 0x00,
			PRIVILEGE_COMPLETED = 0x01, LOADING_START = 0x02,
			LOADING_END = 0x03, NET_ERROR = 0x04, BIG_IMG_LOADED = 0x05;
	private List<GoodsDetailData> bannerData = null;
	private List<GoodsListData> privilegeData = null;
	private ImageAdapter imgAdapter;
	private int offset = 0, length = 10;
	private AsyncImgLoader imgLoader = new AsyncImgLoader();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		mContext = this.getApplication();
		initHandler();
		onPrepareView();
		if (getIntent().getBooleanExtra("success", false)) {
			homeContainer.setVisibility(View.VISIBLE);
			makeBannerView(ClientData.getInstance().getmBanner());
			makeGridView(ClientData.getInstance().getmPrivilege());
		} else {
			homeContainer.setVisibility(View.GONE);
		}
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@SuppressLint("HandlerLeak")
	public void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String data;
				switch (msg.what) {
				case BANNER_COMPLETED:
					data = msg.getData().getString("data");
					makeBannerView(data);
					break;
				case PRIVILEGE_COMPLETED:
					data = msg.getData().getString("data");
					makeGridView(data);
					break;
				case LOADING_START:
					break;
				case LOADING_END:
					if (mPullRefreshScrollView != null) {
						// Call onRefreshComplete when the list has been
						// refreshed.
						mPullRefreshScrollView.onRefreshComplete();
					}
					homeContainer.setVisibility(View.VISIBLE);
					break;
				case BIG_IMG_LOADED:
					imgAdapter.notifyDataSetChanged();
					banner.setAdapter(imgAdapter);
					break;
				case NET_ERROR:
					if (mPullRefreshScrollView != null) {
						// Call onRefreshComplete when the list has been
						// refreshed.
						mPullRefreshScrollView.onRefreshComplete();
					}
					homeContainer.setVisibility(View.GONE);
					View view = getLayoutInflater().inflate(R.layout.my_toast,
							null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					Toast toast = new Toast(HomeActivity.this);
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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void makeBannerView(String data) {
		try {
			bannerData = GsonTool.getGsonTool().fromJson(data,
					new TypeToken<List<GoodsDetailData>>() {
					}.getType());
			Log.v(TAG, "TODO banner data: " + data);
			drawables = new ArrayList<Drawable>();
			imgAdapter = new ImageAdapter(this, drawables);

			for (GoodsDetailData d : bannerData) {
				imgLoader.loadDrawable(d.getImage()[0].getUrl(),
						d.getImage()[0].getId(), new ImageCallback() {
							@Override
							public void imageLoaded(
									final Drawable imageDrawable,
									final String Tag) {
								// TODO Auto-generated method stub

								if (imageDrawable != null) {
									drawables.add(imageDrawable);
								} else {
									drawables.add(getResources().getDrawable(
											R.drawable.home_banner01));
								}

								Message updateViewMsg = mHandler
										.obtainMessage(BIG_IMG_LOADED);
								mHandler.sendMessage(updateViewMsg);
							}
						});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		banner.setCallBack(new ViewListener() {

			@Override
			public void updateView(ViewUpdateObj obj) {
				// TODO Auto-generated method stub
				String pos = obj.getData();
				if (bannerData != null) {
					Log.v(TAG, "TODO login complete code is: " + pos);
					try {
						GoodsDetailData data = bannerData.get(Integer
								.parseInt(pos));
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
						String strDetail = GsonTool.getGsonTool()
								.toJson(detail);
						Intent intent = new Intent(HomeActivity.this,
								GoodDetailActivity.class);
						intent.putExtra("data", strDetail);
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void makeGridView(String data) {
		try {
			privilegeData = GsonTool.getGsonTool().fromJson(data,
					new TypeToken<List<GoodsListData>>() {
					}.getType());
			Log.v(TAG, "TODO privilege data: " + data);
			ArrayList<HashMap<String, Object>> tuijianList = new ArrayList<HashMap<String, Object>>();
			for (GoodsListData d : privilegeData) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("GoodsName", d.getName());
				map.put("GoodsCurrentPrice", d.getCurrent_price());
				map.put("GoodsOriginPrice",
						getResources().getString(R.string.str_origin_price)
								+ d.getCurrent_price());
				// map.put("ItemImage", R.drawable.tuijian_sample);
				map.put("ImgId", d.getIcon().getId());
				map.put("ImgUrl", d.getIcon().getUrl());
				tuijianList.add(map);
			}
			GridViewImgAdapter adapter = new GridViewImgAdapter(
					HomeActivity.this, tuijianList);
			gridview.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		gridview.setOnItemClickListener(HomeActivity.this);
	}

	private void onPrepareView() {
		homeContainer = (LinearLayout) findViewById(R.id.home_container);
		banner = (AutoPlayGallery) findViewById(R.id.homeBanner);
		gridview = (MyGridView) findViewById(R.id.homeMenuGridView);
		gridview.getLayoutParams().height = gridview.getWidth();
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
							new GetDataTask().execute();
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

		banner.setFocusable(true);
		banner.setFocusableInTouchMode(true);
		banner.requestFocus();
	}

	public void onPrepareData() {
		offset = 0;
		boolLoadComplete = new BoolLoadComplete();
		ControllerManager.getInstance().getSearchController().unRegisterAll();
		ControllerManager.getInstance().getSearchController()
				.registerNotification(HomeActivity.this);
		ControllerManager.getInstance().getSearchController().getHomeData();
	}

	public void getMoreData() {
		ControllerManager.getInstance().getSearchController().unRegisterAll();
		ControllerManager.getInstance().getSearchController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						// TODO Auto-generated method stub
						if (obj.getCode() == 200) {
							Message updateViewMsg = mHandler
									.obtainMessage(LOADING_END);
							mHandler.sendMessage(updateViewMsg);
						} else {
							Message updateViewMsg = mHandler
									.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
						}
					}
				});
		offset += length;
		ControllerManager.getInstance().getSearchController()
				.getPrivilege(offset, length);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (privilegeData != null) {
			try {
				String detailData = GsonTool.getGsonTool().toJson(
						privilegeData.get(position), GoodsListData.class);
				GoodsListData data = privilegeData.get(position);
				GoodsDetailData detail = new GoodsDetailData();
				if (data.getId() != null) {
					detail.setId(data.getId());
				}
				if (data.getName() != null) {
					detail.setName(data.getName());
				}
				if (data.getId() != null) {
					detail.setCategory(data.getId());
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
				String strDetail = GsonTool.getGsonTool().toJson(detail);
				Log.v(TAG, "TODO jinrituijian detail: " + detailData);
				Intent intent = new Intent(HomeActivity.this,
						GoodDetailActivity.class);
				intent.putExtra("data", strDetail);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			onPrepareData();
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
			getMoreData();
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

	public static class BoolLoadComplete {
		private boolean getBanner = false;
		private boolean getPrivilege = false;
		private boolean getVersion = false;
		private boolean getMinLimit = false;

		public boolean isGetBanner() {
			return getBanner;
		}

		public void setGetBanner(boolean getBanner) {
			this.getBanner = getBanner;
		}

		public boolean isGetPrivilege() {
			return getPrivilege;
		}

		public void setGetPrivilege(boolean getPrivilege) {
			this.getPrivilege = getPrivilege;
		}

		public boolean isGetVersion() {
			return getVersion;
		}

		public void setGetVersion(boolean getVersion) {
			this.getVersion = getVersion;
		}

		public boolean isGetMinLimit() {
			return getMinLimit;
		}

		public void setGetMinLimit(boolean getMinLimit) {
			this.getMinLimit = getMinLimit;
		}

		public boolean boolComplete() {
			if (isGetBanner() && isGetPrivilege() && isGetMinLimit()
					&& isGetVersion()) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public void updateView(ViewUpdateObj obj) {
		// TODO Auto-generated method stub
		if (obj.getCode() == 200) {
			if (obj.getData() != null) {
				Message updateViewMsg = null;
				switch (obj.getNotifyId()) {
				case HOME_BANNER:
					boolLoadComplete.setGetBanner(true);
					updateViewMsg = mHandler.obtainMessage(BANNER_COMPLETED);
					updateViewMsg.getData().putString("data", obj.getData());
					mHandler.sendMessage(updateViewMsg);
					break;
				case HOME_PRIVILEGE:
					boolLoadComplete.setGetPrivilege(true);
					updateViewMsg = mHandler.obtainMessage(PRIVILEGE_COMPLETED);
					updateViewMsg.getData().putString("data", obj.getData());
					mHandler.sendMessage(updateViewMsg);
					break;
				case VERSION:
					boolLoadComplete.setGetVersion(true);
					break;
				case MIN_LIMIT:
					boolLoadComplete.setGetMinLimit(true);
					break;
				default:
					break;
				}
				if (boolLoadComplete.boolComplete()) {
					updateViewMsg = mHandler.obtainMessage(LOADING_END);
					mHandler.sendMessage(updateViewMsg);
				}
			}
		} else {
			Message updateViewMsg = mHandler.obtainMessage(NET_ERROR);
			mHandler.sendMessage(updateViewMsg);
		}
	}
}
