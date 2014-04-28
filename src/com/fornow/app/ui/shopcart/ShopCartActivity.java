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
import android.text.Selection;
import android.text.Spannable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.MyListView;
import com.fornow.app.ui.customdialog.EditCountDialog;
import com.fornow.app.ui.customdialog.LoginDialog;
import com.fornow.app.ui.goodsdetail.GoodDetailActivity;
import com.fornow.app.ui.main.BaseMainActivity;
import com.fornow.app.utils.GsonTool;
import com.fornow.app.utils.pull2refresh.PullToRefreshBase;
import com.fornow.app.utils.pull2refresh.PullToRefreshBase.OnRefreshListener;
import com.fornow.app.utils.pull2refresh.PullToRefreshScrollView;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;

/**
 * @author Simon Lv 2013-8-4
 */
public class ShopCartActivity extends BaseMainActivity {

	private Context mContext;
	private MyListView mListView;
	private CartAdapter mAdapter;
	public static final int LOADING_START = 0x00, LOADING_END = 0x01,
			CART_OVERVIEW_SHOW = 0x02, CART_OVERVIEW_HIDE = 0x03,
			UUID_TIMEOUT = 0x04, NET_ERROR = 0x09, CAER_COUNT_EDIT = 0x0a,
			UPDATECARTDATA = 0x05;
	private List<ShopCart> cartData;
	private List<ListStatus> listStatus;
	private boolean selectAll = false;
//	private String jiesuanList;
	private Float totalPrice = 0.0f;
	private PullToRefreshScrollView mPullRefreshScrollView;
	@SuppressWarnings("unused")
	private ScrollView mScrollView;
	AnimateDismissAdapter<String> animateDismissAdapter;
	TextView cartTotalCount, cartOverviewTotalprice;
	ImageButton cart_overview_delete, cart_select_all;
	Button cart_jiesuan;
	RelativeLayout cartOverView;
	TranslateAnimation mShowAction, mHiddenAction;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String data;
			final int pos;
			switch (msg.what) {
			case LOADING_START:
				selectAll = false;
				totalPrice = 0.0f;
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
			case CART_OVERVIEW_SHOW:
				if (cartOverView.getVisibility() == View.GONE) {
					cartOverView.startAnimation(mShowAction);
					cartOverView.setVisibility(View.VISIBLE);
				}
				cartOverviewTotalprice.setText(msg.getData().getString("data"));
				break;
			case CART_OVERVIEW_HIDE:
				if (cartOverView.getVisibility() == View.VISIBLE) {
					cartOverView.startAnimation(mHiddenAction);
					cartOverView.setVisibility(View.GONE);
				}
				break;
			case UPDATECARTDATA:
				mAdapter.notifyDataSetChanged();
				break;
			case NET_ERROR:
				if (mPullRefreshScrollView != null) {
					// Call onRefreshComplete when the list has been
					// refreshed.
					mPullRefreshScrollView.onRefreshComplete();
				}
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
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

			case CAER_COUNT_EDIT:
				pos = Integer.valueOf(msg.getData().getString("position"));

				final EditCountDialog editDialogBuilder = new EditCountDialog(
						ShopCartActivity.this, R.style.Theme_Dialog);
				editDialogBuilder.setEditCount(cartData.get(pos).getCount()
						+ "");
				Spannable spanText = (Spannable) editDialogBuilder
						.getEditCount();
				Selection.setSelection(spanText, editDialogBuilder
						.getEditCount().length());
				editDialogBuilder.setOnCancelBtnListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						editDialogBuilder.dismiss();
					}
				});

				editDialogBuilder
						.setOnConfirmBtnListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								editDialogBuilder.dismiss();
								if (editDialogBuilder.getEditCount().length() != 0) {
									int count = Integer
											.parseInt(editDialogBuilder
													.getEditCount().toString());
									cartData.get(pos).setCount(count);
									ControllerManager.getInstance()
											.getShopCartController()
											.unRegisterAll();
									ControllerManager
											.getInstance()
											.getShopCartController()
											.registerNotification(
													new ViewListener() {

														@Override
														public void updateView(
																ViewUpdateObj obj) {
															Message updateViewMsg;
															switch (obj
																	.getCode()) {
															case 200:
																updateViewMsg = mHandler
																		.obtainMessage(UPDATECARTDATA);
																mHandler.sendMessage(updateViewMsg);
																break;
															case 408:
																updateViewMsg = mHandler
																		.obtainMessage(UUID_TIMEOUT);
																mHandler.sendMessage(updateViewMsg);
																break;
															default:
																updateViewMsg = mHandler
																		.obtainMessage(NET_ERROR);
																mHandler.sendMessage(updateViewMsg);
																break;
															}
														}
													});

									ControllerManager.getInstance()
											.getShopCartController()
											.updateCartData(cartData);
								}
							}
						});

				editDialogBuilder.show();
				break;
			case UUID_TIMEOUT:
				LoginDialog loginDialog = new LoginDialog(
						ShopCartActivity.this, mContext, mContext
								.getResources().getString(R.string.str_tishi),
						mContext.getResources().getString(
								R.string.str_uuid_timeout), 0);
				loginDialog.build();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_cart);
		mContext = this.getApplicationContext();
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(300);
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f);
		mHiddenAction.setDuration(300);
		prepareView();

	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void onStart() {
		super.onStart();

	}

	public void prepareView() {
		mListView = (MyListView) findViewById(R.id.listView01);
		cartTotalCount = (TextView) findViewById(R.id.cart_total_count);
		cart_select_all = (ImageButton) findViewById(R.id.cart_select_all);
		cartOverView = (RelativeLayout) this.getParent().findViewById(
				R.id.cart_overview);
		cartOverviewTotalprice = (TextView) this.getParent().findViewById(
				R.id.cart_overview_totalprice);
		cart_overview_delete = (ImageButton) this.getParent().findViewById(
				R.id.cart_overview_delete);
		cart_overview_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				delShopCart();
			}
		});
		cart_jiesuan = (Button) this.getParent()
				.findViewById(R.id.cart_jiesuan);
		cart_jiesuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmBuy();
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

						new GetDataTask().execute();
					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Message updateViewMsg = mHandler.obtainMessage(LOADING_START);
		mHandler.sendMessage(updateViewMsg);
		ControllerManager.getInstance().getShopCartController().unRegisterAll();
		ControllerManager.getInstance().getShopCartController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						Message updateViewMsg;
						switch (obj.getCode()) {
						case 200:
							updateViewMsg = mHandler.obtainMessage(LOADING_END);
							updateViewMsg.getData().putString("data",
									obj.getData());
							mHandler.sendMessage(updateViewMsg);
							break;
						case 408:
							updateViewMsg = mHandler
									.obtainMessage(UUID_TIMEOUT);
							mHandler.sendMessage(updateViewMsg);
							break;
						default:
							updateViewMsg = mHandler.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
							break;
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

	public void delShopCart() {
		for (int i = 0; i < listStatus.size(); i++) {
			if (listStatus.get(i).isChecked()) {
				animateDismissAdapter.animateDismiss(i);
			}
		}
	}

	public void getCartFromBackend() {

		Message updateViewMsg = mHandler.obtainMessage(LOADING_START);
		mHandler.sendMessage(updateViewMsg);
		ControllerManager.getInstance().getShopCartController().unRegisterAll();
		ControllerManager.getInstance().getShopCartController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						Message updateViewMsg;
						switch (obj.getCode()) {
						case 200:
							updateViewMsg = mHandler.obtainMessage(LOADING_END);
							updateViewMsg.getData().putString("data",
									obj.getData());
							mHandler.sendMessage(updateViewMsg);
							break;
						case 408:
							updateViewMsg = mHandler
									.obtainMessage(UUID_TIMEOUT);
							mHandler.sendMessage(updateViewMsg);
							break;
						default:
							updateViewMsg = mHandler.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
							break;
						}
					}
				});

		ControllerManager.getInstance().getShopCartController().getCartData();
	}

	public void updateList(String data) {
		if (data != null) {
			try {
				cartData = GsonTool.getGsonTool().fromJson(data,
						new TypeToken<List<ShopCart>>() {
						}.getType());
				cartTotalCount.setText(cartData.size() + "");
				listStatus = new ArrayList<ListStatus>();
				for (int i = 0; i < cartData.size(); i++) {
					ListStatus status = new ListStatus();
					listStatus.add(status);
				}
				mAdapter = new CartAdapter(cartData, listStatus, mContext,
						mHandler);
				animateDismissAdapter = new AnimateDismissAdapter<String>(
						mAdapter, new MyOnDismissCallback());
				animateDismissAdapter.setListView(mListView);
				mListView.setAdapter(animateDismissAdapter);

				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						go2detail(cartData.get(position));
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (cartData != null && listStatus != null) {
				cartData.clear();
				listStatus.clear();
			}
		}
		cart_select_all.setImageResource(R.drawable.checkbox_no);
		if (cartData != null) {
			cartTotalCount.setText(cartData.size() + "");
		}
	}

	private class MyOnDismissCallback implements OnDismissCallback {

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				cartData.remove(position);
				listStatus.remove(position);
			}
			ControllerManager.getInstance().getShopCartController()
					.unRegisterAll();
			ControllerManager.getInstance().getShopCartController()
					.registerNotification(new ViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							Message updateViewMsg;
							switch (obj.getCode()) {
							case 200:
								updateViewMsg = mHandler
										.obtainMessage(UPDATECARTDATA);
								mHandler.sendMessage(updateViewMsg);
								cartTotalCount.post(new Runnable() {

									@Override
									public void run() {
										cartTotalCount.setText(cartData.size()
												+ "");
									}
								});
								break;
							case 408:
								updateViewMsg = mHandler
										.obtainMessage(UUID_TIMEOUT);
								mHandler.sendMessage(updateViewMsg);
								break;
							default:
								updateViewMsg = mHandler
										.obtainMessage(NET_ERROR);
								mHandler.sendMessage(updateViewMsg);
								break;
							}
						}
					});

			ControllerManager.getInstance().getShopCartController()
					.updateCartData(cartData);

			Message updateViewMsg = mHandler
					.obtainMessage(ShopCartActivity.CART_OVERVIEW_HIDE);
			mHandler.sendMessage(updateViewMsg);
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
					cart_select_all.setImageResource(R.drawable.checkbox_yes);
				} else {
					for (i = 0; i < listStatus.size(); i++) {
						listStatus.get(i).setChecked(false);
					}
					totalPrice = 0.0f;
					selectAll = false;
					cart_select_all.setImageResource(R.drawable.checkbox_no);
				}
				Message updateViewMsg = mHandler.obtainMessage(UPDATECARTDATA);
				mHandler.sendMessage(updateViewMsg);
			}
		}
	}

	public void go2detail(ShopCart cart) {
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

		String strDetail = GsonTool.getGsonTool().toJson(detialData);
		Intent intent = new Intent(ShopCartActivity.this,
				GoodDetailActivity.class);
		intent.putExtra("data", strDetail);
		startActivity(intent);
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

	public void confirmBuy() {
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
				String strGoodsList = GsonTool.getGsonTool().toJson(goodsList);

				Intent intent = new Intent(ShopCartActivity.this,
						JieSuanActivity.class);
				intent.putExtra("data", strGoodsList);
				startActivity(intent);
			}
		}
		Message updateViewMsg = mHandler
				.obtainMessage(ShopCartActivity.CART_OVERVIEW_HIDE);
		mHandler.sendMessage(updateViewMsg);
	}
}
