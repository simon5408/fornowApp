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
package com.fornow.app.ui.shopcart;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.ShipAddressData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IViewListener;
import com.fornow.app.ui.MyListView;
import com.fornow.app.ui.main.BaseMainActivity;
import com.fornow.app.ui.mine.LoginActivity;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase;
import com.fornow.app.ui.pull2refresh.PullToRefreshBase.OnRefreshListener;
import com.fornow.app.ui.pull2refresh.PullToRefreshScrollView;
import com.fornow.app.ui.search.GoodDetailActivity;
import com.fornow.app.util.GsonTool;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class ShopCartActivity extends BaseMainActivity {

	private Context mContext;
	private MyListView mListView;
	private CartAdapter mAdapter;
	private Handler mHandler;
	public static final int LOADING_START = 0x00, LOADING_END = 0x01,
			CART_SELECT = 0x02, CART_UNSELECT = 0x03, CART_ADD = 0x04,
			CART_MINUS = 0x05, CART_DEL = 0x06, GET_ADDRESS_SUCCESS = 0x07,
			CART_CLICKED = 0x08, NET_ERROR = 0x09;
	private List<ShopCart> cartData;
	private List<ListStatus> listStatus;
	private BoolShowDel boolShowDel;
	private boolean selectAll = false;
	private TextView totalPriceView;
	private String jiesuanList;
	private Float totalPrice = 0.0f;
	private PullToRefreshScrollView mPullRefreshScrollView;
//	private ScrollView mScrollView;
	AnimateDismissAdapter<String> animateDismissAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_cart);
		mContext = this.getApplicationContext();
		totalPriceView = (TextView) findViewById(R.id.total_price);
		mListView = (MyListView) findViewById(R.id.listView01);
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

//		mScrollView = mPullRefreshScrollView.getRefreshableView();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String data;
				switch (msg.what) {
				case GET_ADDRESS_SUCCESS:
					data = msg.getData().getString("data");
					String strDefaultAddress = null;
					if (data != null) {
						try {
							ShipAddressData defaultAddress = null;
							List<ShipAddressData> addressData = GsonTool
									.fromJson(
											data,
											new TypeToken<List<ShipAddressData>>() {
											});
							for (ShipAddressData d : addressData) {
								if (d.isIsdefault()) {
									defaultAddress = d;
									break;
								}
							}
							if (defaultAddress != null) {
								strDefaultAddress = GsonTool.toJson(defaultAddress);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					Intent intent = new Intent(ShopCartActivity.this,
							JieSuanActivity.class);
					intent.putExtra("data", jiesuanList);
					intent.putExtra("defaultAddress", strDefaultAddress);
					startActivity(intent);
					break;
				case LOADING_START:
					selectAll = false;
					totalPrice = 0.0f;
					totalPriceView.setText(totalPrice + "元");
					break;
				case LOADING_END:
					if (mPullRefreshScrollView != null) {
						// Call onRefreshComplete when the list has been
						// refreshed.
						mPullRefreshScrollView.onRefreshComplete();
					}
					data = msg.getData().getString("data");
					updateList(data);
					break;
				case CART_SELECT:
				case CART_ADD:
					data = msg.getData().getString("data");
					totalPrice += Float.valueOf(data);
					totalPriceView.setText(new BigDecimal(totalPrice).setScale(1,
							BigDecimal.ROUND_HALF_UP) + "元");
					break;
				case CART_DEL:
					int pos = Integer.valueOf(msg.getData().getString(
							"position"));
					animateDismissAdapter.animateDismiss(pos);
					if (listStatus.get(pos).isChecked()) {
						data = msg.getData().getString("data");
						totalPrice -= Float.valueOf(data);
						totalPriceView.setText(new BigDecimal(totalPrice).setScale(1,
								BigDecimal.ROUND_HALF_UP) + "元");
					}
					break;
				case CART_UNSELECT:
				case CART_MINUS:
					data = msg.getData().getString("data");
					totalPrice -= Float.valueOf(data);
					totalPriceView.setText(new BigDecimal(totalPrice).setScale(1,
							BigDecimal.ROUND_HALF_UP) + "元");
					break;
				case CART_CLICKED:
					data = msg.getData().getString("data");
					go2detail(data);
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
					Toast toast = new Toast(ShopCartActivity.this);
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

	@SuppressLint("HandlerLeak")
	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
		Message updateViewMsg = mHandler.obtainMessage(LOADING_START);
		mHandler.sendMessage(updateViewMsg);
		ControllerManager.getInstance().getShopCartController().unRegisterAll();
		ControllerManager.getInstance().getShopCartController()
				.registerNotification(new IViewListener() {

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

		ControllerManager.getInstance().getShopCartController()
				.getCartDataFromCache();
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			getCartFromBackend();
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

	public void getCartFromBackend() {

		Message updateViewMsg = mHandler.obtainMessage(LOADING_START);
		mHandler.sendMessage(updateViewMsg);
		ControllerManager.getInstance().getShopCartController().unRegisterAll();
		ControllerManager.getInstance().getShopCartController()
				.registerNotification(new IViewListener() {

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

		ControllerManager.getInstance().getShopCartController().getCartData();
	}

	public void updateList(String data) {
		if (data != null) {
			try {
				cartData = GsonTool.fromJson(data,
						new TypeToken<List<ShopCart>>(){});
				listStatus = new ArrayList<ListStatus>();
				boolShowDel = new BoolShowDel();
				for (int i = 0; i < cartData.size(); i++) {
					ListStatus status = new ListStatus();
					listStatus.add(status);
				}
				mAdapter = new CartAdapter(cartData, listStatus, mContext,
						boolShowDel, mHandler);
				animateDismissAdapter = new AnimateDismissAdapter<String>(
						mAdapter, new MyOnDismissCallback());
				animateDismissAdapter.setListView(mListView);
				mListView.setAdapter(animateDismissAdapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (cartData != null && listStatus != null) {
				cartData.clear();
				listStatus.clear();
				mAdapter.notifyDataSetChanged();
				animateDismissAdapter.notifyDataSetChanged();
			}
		}
	}

	private class MyOnDismissCallback implements OnDismissCallback {

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				cartData.remove(position);
				listStatus.remove(position);
				mAdapter.notifyDataSetChanged();
				CartDataHelper.updateCacheCart(cartData);
			}
		}
	}

	public void selectAll(View v) {
		int i;
		if (listStatus != null && cartData != null) {
			if (listStatus.size() > 0 && cartData.size() > 0) {
				if (!selectAll) {
					for (i = 0; i < listStatus.size(); i++) {
						listStatus.get(i).setChecked(true);
					}
					totalPrice = 0.0f;
					for (i = 0; i < cartData.size(); i++) {
						totalPrice += Float.valueOf(cartData.get(i)
								.getCurrent_price() + "")
								* Integer.valueOf(cartData.get(i).getCount());
					}
					selectAll = true;
				} else {
					for (i = 0; i < listStatus.size(); i++) {
						listStatus.get(i).setChecked(false);
					}
					totalPrice = 0.0f;
					selectAll = false;
				}
				totalPriceView.setText(totalPrice + "元");
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	public void go2detail(String detail) {
		if (detail != null) {
			try {
				ShopCart cart = GsonTool.fromJson(detail,
						ShopCart.class);
				GoodsDetailData detialData = new GoodsDetailData();
				if (cart.getGoods_id() != null) {
					detialData.setId(cart.getGoods_id());
				}
				if (cart.getName() != null) {
					detialData.setName(cart.getName());
				}
				if (cart.getCategory() != null) {
					detialData.setCategory(cart.getCategory());
				}
				if (cart.getIcon() != null) {
					detialData.setIcon(cart.getIcon());
				}
				if (cart.getImage() != null) {
					detialData.setImage(cart.getImage());
				}
				if (cart.getOriginal_price() != null) {
					detialData.setOriginal_price(cart.getOriginal_price());
				}
				if (cart.getCurrent_price() != null) {
					detialData.setCurrent_price(cart.getCurrent_price());
				}
				if (cart.getIntroduction() != null) {
					detialData.setIntroduction(cart.getIntroduction());
				}
				if (cart.getDeliver_area() != null) {
					detialData.setDeliver_area(cart.getDeliver_area());
				}

				String strDetail = GsonTool.toJson(detialData);
				Intent intent = new Intent(ShopCartActivity.this,
						GoodDetailActivity.class);
				intent.putExtra("data", strDetail);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void disPlayDelBtn(View v) {
		if (boolShowDel.isDisplayDel()) {
			boolShowDel.setDisplayDel(false);
		} else {
			boolShowDel.setDisplayDel(true);
		}
		mAdapter.notifyDataSetChanged();
	}

	public class ListStatus {
		private boolean checked = false;

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}

	public class BoolShowDel {
		private boolean displayDel = false;

		public boolean isDisplayDel() {
			return displayDel;
		}

		public void setDisplayDel(boolean displayDel) {
			this.displayDel = displayDel;
		}
	}

	public void confirmBuy(View v) {
		if (cartData != null && cartData.size() > 0) {
			int i, length = cartData.size();
			List<GoodsDetailData> goodsList = new ArrayList<GoodsDetailData>();
			GoodsDetailData detialData = null;
			for (i = 0; i < length; i++) {
				if (listStatus.get(i).isChecked()) {
					detialData = new GoodsDetailData();
					if (cartData.get(i).getGoods_id() != null) {
						detialData.setId(cartData.get(i).getGoods_id());
					}
					if (cartData.get(i).getName() != null) {
						detialData.setName(cartData.get(i).getName());
					}
					if (cartData.get(i).getCategory() != null) {
						detialData.setCategory(cartData.get(i).getCategory());
					}
					if (cartData.get(i).getIcon() != null) {
						detialData.setIcon(cartData.get(i).getIcon());
					}
					if (cartData.get(i).getImage() != null) {
						detialData.setImage(cartData.get(i).getImage());
					}
					if (cartData.get(i).getOriginal_price() != null) {
						detialData.setOriginal_price(cartData.get(i)
								.getOriginal_price());
					}
					if (cartData.get(i).getCurrent_price() != null) {
						detialData.setCurrent_price(cartData.get(i)
								.getCurrent_price());
					}
					if (cartData.get(i).getIntroduction() != null) {
						detialData.setIntroduction(cartData.get(i)
								.getIntroduction());
					}
					if (cartData.get(i).getDeliver_area() != null) {
						detialData.setDeliver_area(cartData.get(i)
								.getDeliver_area());
					}
					detialData.setSelect_count(cartData.get(i).getCount());
					goodsList.add(detialData);
				}
			}
			if (goodsList.size() > 0) {
				try {
					jiesuanList = GsonTool.toJson(goodsList);
					if (ClientData.getInstance().getmUUID() == null) {
						Intent in = new Intent(ShopCartActivity.this,
								LoginActivity.class);
						startActivityForResult(in, 0);
						return;
					} else {
						jiesuan();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			jiesuan();
			break;
		default:
			break;
		}
	}

	public void jiesuan() {
		ControllerManager.getInstance().getAddressManageController()
				.unRegisterAll();
		ControllerManager.getInstance().getAddressManageController()
				.registerNotification(new IViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						if (obj.getCode() == 200) {
							Message updateViewMsg = mHandler
									.obtainMessage(GET_ADDRESS_SUCCESS);
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
		ControllerManager.getInstance().getAddressManageController()
				.getShippingAddress();
	}
}
