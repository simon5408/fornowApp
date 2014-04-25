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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.GoodsListData;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IViewListener;
import com.fornow.app.ui.GridViewImgAdapter;
import com.fornow.app.ui.MyGridView;
import com.fornow.app.ui.loadImg.AsyncImgLoader;
import com.fornow.app.ui.loadImg.AsyncImgLoader.ImageCallback;
import com.fornow.app.ui.main.BaseMainActivity;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase.OnRefreshListener;
import com.fornow.app.ui.pull2refresh.PullToRefreshScrollView;
import com.fornow.app.ui.search.GoodDetailActivity;
import com.fornow.app.ui.search.GoodsListActivity;
import com.fornow.app.util.GsonTool;
import com.fornow.app.util.LogUtils;
import com.google.gson.reflect.TypeToken;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class HomeActivity extends BaseMainActivity implements
		OnItemClickListener, IViewListener {

	private static final String TAG = HomeActivity.class.getName();
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
	private Intent fruitIntent, vegetableIntent;
	private ImageAdapter imgAdapter;
	private AsyncImgLoader imgLoader = new AsyncImgLoader();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		super.onStart();
	}

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
		super.onResume();
	}

	private void makeBannerView(String data) {
		try {
			bannerData = GsonTool.fromJson(data,
					new TypeToken<List<GoodsDetailData>>() {
					});
			LogUtils.v(TAG,
					"banner data: "
							+ GsonTool.toJson(bannerData.get(0),
									GoodsDetailData.class));
			drawables = new ArrayList<Drawable>();
			imgAdapter = new ImageAdapter(this, drawables);

			for (GoodsDetailData d : bannerData) {
				imgLoader.loadDrawable(d.getImage()[0].getUrl(),
						d.getImage()[0].getId(), new ImageCallback() {
							@Override
							public void imageLoaded(
									final Drawable imageDrawable,
									final String Tag) {
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
		banner.setCallBack(new IViewListener() {

			@Override
			public void updateView(ViewUpdateObj obj) {
				String pos = obj.getData();
				if (bannerData != null) {
					LogUtils.v(TAG, "login complete code is: " + pos);
					try {
//						String detailData = GsonTool.getGsonTool().toJson(
//								bannerData.get(Integer.parseInt(pos)),
//								GoodsDetailData.class);
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
						String strDetail = GsonTool
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
			privilegeData = GsonTool.fromJson(data,
					new TypeToken<List<GoodsListData>>() {
					});
			LogUtils.v(TAG,
					"privilege data: "
							+ GsonTool.toJson(
									privilegeData.get(0), GoodsListData.class));
			ArrayList<HashMap<String, Object>> tuijianList = new ArrayList<HashMap<String, Object>>();
			for (GoodsListData d : privilegeData) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("GoodsName",
						getResources().getString(R.string.goods_name_tag)
								+ d.getName());
				map.put("GoodsCurrentPrice",
						getResources().getString(R.string.goods_unit)
								+ d.getCurrent_price());
				map.put("GoodsOriginPrice",
						getResources().getString(R.string.goods_unit)
								+ d.getCurrent_price()
								+ getResources().getString(R.string.sell_unit));
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
						Date date = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy/MM/dd HH:mm");
						String label = getResources().getString(
								R.string.last_update_time);
						label += formatter.format(date);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						new GetDataTask().execute();
					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();

		banner.setFocusable(true);
		banner.setFocusableInTouchMode(true);
		banner.requestFocus();
	}

	public void onPrepareData() {
		boolLoadComplete = new BoolLoadComplete();
		ControllerManager.getInstance().getSearchController().unRegisterAll();
		ControllerManager.getInstance().getSearchController()
				.registerNotification(HomeActivity.this);
		ControllerManager.getInstance().getSearchController().getHomeData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (privilegeData != null) {
			try {
				String detailData = GsonTool.toJson(
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
				String strDetail = GsonTool.toJson(detail);
				LogUtils.v(TAG, "jinrituijian detail: " + detailData);
				Intent intent = new Intent(HomeActivity.this,
						GoodDetailActivity.class);
				intent.putExtra("data", strDetail);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * public void showRegionList(View v) { Dialog dialog = new
	 * RegionList(HomeActivity.this, R.style.my_dialog);
	 * dialog.setContentView(R.layout.region_list);
	 * dialog.setCanceledOnTouchOutside(true); dialog.show(); WindowManager
	 * windowManager = getWindowManager(); Display display =
	 * windowManager.getDefaultDisplay(); WindowManager.LayoutParams lp =
	 * dialog.getWindow().getAttributes(); lp.width = (int) (display.getWidth()
	 * * 0.9); lp.height = (int) (display.getHeight() * 0.9);
	 * dialog.getWindow().setAttributes(lp); }
	 */

	@SuppressLint("NewApi")
	public void getInFruit(View v) {
		Bundle bundle = new Bundle();
		bundle.putString("type", "fruit");
		fruitIntent = new Intent(HomeActivity.this, GoodsListActivity.class);
		fruitIntent.putExtras(bundle);
		startActivity(fruitIntent);
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		if (version >= 5) {
			// overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		}
	}

	@SuppressLint("NewApi")
	public void getInVegetable(View v) {
		Bundle bundle = new Bundle();
		bundle.putString("type", "vegetable");
		vegetableIntent = new Intent(HomeActivity.this, GoodsListActivity.class);
		vegetableIntent.putExtras(bundle);
		startActivity(vegetableIntent);
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		if (version >= 5) {
			// overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
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
					LogUtils.d(TAG, "---------------version:"
							+ ClientData.getInstance().getVersion());
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
