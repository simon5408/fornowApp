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
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.ConfirmData;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.SettlementGoods;
import com.fornow.app.model.ShipAddressData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IViewListener;
import com.fornow.app.ui.mine.ShdzActivity;
import com.fornow.app.ui.mine.ShdzActivity.CLICK_TYPE;
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
public class JieSuanActivity extends Activity {
	private FrameLayout initShdz;
	private Button addShdz;
	private TextView shdzShr, shdzPhone, shdzAddress, jiesuanTotalPriceView;
	private EditText shdzLiuyan;
	private ListView listView;
	private Context mContext;
	AnimateDismissAdapter<String> animateDismissAdapter;
	private Handler mHandler;
	private List<GoodsDetailData> mList;
	private JiesuanAdapter mAdapter;
	private ShipAddressData address;
	public static final int JIESUAN_ADD = 0x00, JIESUAN_MINUS = 0x01,
			JIESUAN_DEL = 0x02, NET_ERROR = 0x03, JIESUAN_SUCCESS = 0x04,
			HAVE_NO_ADDRESS = 0x05, LIMIT_PRICE_ERROR = 0x06;
	private Float totalPrice = 0.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jiesuan);
		mContext = this.getApplicationContext();
		initShdz = (FrameLayout) findViewById(R.id.jiesuan_init_shdz);
		addShdz = (Button) findViewById(R.id.jiesuan_add_shdz);
		shdzShr = (TextView) findViewById(R.id.jiesuan_shdz_shr);
		shdzPhone = (TextView) findViewById(R.id.jiesuan_shdz_phone);
		shdzAddress = (TextView) findViewById(R.id.jiesuan_shdz_address);
		jiesuanTotalPriceView = (TextView) findViewById(R.id.jiesuan_total_price);
		shdzLiuyan = (EditText) findViewById(R.id.jiesuan_liuyan);
		listView = (ListView) findViewById(R.id.listView01);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String data;
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
				TextView toastText = (TextView) view
						.findViewById(R.id.toast_text);
				Toast toast = new Toast(JieSuanActivity.this);
				switch (msg.what) {
				case JIESUAN_ADD:
					data = msg.getData().getString("data");
					totalPrice += Float.valueOf(data);
					jiesuanTotalPriceView.setText(new BigDecimal(totalPrice)
							.setScale(1, BigDecimal.ROUND_HALF_UP) + "元");
					break;
				case JIESUAN_DEL:
					int pos = Integer.valueOf(msg.getData().getString(
							"position"));
					animateDismissAdapter.animateDismiss(pos);
					data = msg.getData().getString("data");
					totalPrice -= Float.valueOf(data);
					jiesuanTotalPriceView.setText(new BigDecimal(totalPrice)
							.setScale(1, BigDecimal.ROUND_HALF_UP) + "元");
					break;
				case JIESUAN_MINUS:
					data = msg.getData().getString("data");
					totalPrice -= Float.valueOf(data);
					jiesuanTotalPriceView.setText(new BigDecimal(totalPrice)
							.setScale(1, BigDecimal.ROUND_HALF_UP) + "元");
					break;
				case NET_ERROR:
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				case JIESUAN_SUCCESS:
					toastText.setText(getResources().getString(
							R.string.jiesuan_success));
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				case HAVE_NO_ADDRESS:
					toastText.setText(getResources().getString(
							R.string.str_have_no_address));
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				case LIMIT_PRICE_ERROR:
					toastText.setText(getResources().getString(
							R.string.str_limit_error0) + ClientData.getInstance().getMinLimit() + getResources().getString(
									R.string.str_limit_error1));
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
		initData();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void initData() {
		Intent intent = getIntent();
		try {
			if (intent.getExtras() != null) {
				if (intent.getExtras().get("data") != null) {
					mList = GsonTool.fromJson(
							intent.getExtras().get("data") + "",
							new TypeToken<List<GoodsDetailData>>() {
							});
					for (GoodsDetailData d : mList) {
						totalPrice += Float.valueOf(JiesuanAdapter
								.itemTotalPrice(d.getCurrent_price() + "",
										d.getSelect_count()));
					}
					jiesuanTotalPriceView.setText(totalPrice + "元");
					mAdapter = new JiesuanAdapter(mList, mContext, mHandler);
					animateDismissAdapter = new AnimateDismissAdapter<String>(
							mAdapter, new MyOnDismissCallback());
					animateDismissAdapter.setListView(listView);
					listView.setAdapter(animateDismissAdapter);
				}
				if (intent.getExtras() != null
						&& intent.getExtras().get("defaultAddress") != null) {
					String strAddress = intent.getExtras()
							.get("defaultAddress").toString();
					address = GsonTool.fromJson(strAddress,
							ShipAddressData.class);
					initShdz.setVisibility(View.VISIBLE);
					addShdz.setVisibility(View.GONE);
					shdzShr.setText(getResources().getString(R.string.str_shr)
							+ address.getName());
					shdzPhone.setText(address.getPhone());
					shdzAddress.setText(getResources().getString(
							R.string.str_shdz)
							+ address.getAddress());
				} else {
					initShdz.setVisibility(View.GONE);
					addShdz.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MyOnDismissCallback implements OnDismissCallback {

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				mList.remove(position);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	public void selectShdz(View v) {
		Intent intent = new Intent(JieSuanActivity.this, ShdzActivity.class);
		intent.putExtra("type", CLICK_TYPE.SELECT);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (data.getExtras() != null
					&& data.getExtras().getString("addressData") != null) {
				String strAddress = data.getExtras().getString("addressData");
				try {
					address = GsonTool.fromJson(strAddress,
							ShipAddressData.class);
					initShdz.post(new Runnable() {
						@Override
						public void run() {
							initShdz.setVisibility(View.VISIBLE);
							addShdz.setVisibility(View.GONE);
							shdzShr.setText(getResources().getString(
									R.string.str_shr)
									+ address.getName());
							shdzPhone.setText(address.getPhone());
							shdzAddress.setText(address.getAddress());
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}

	public void confirmBuy(View v) {
		ConfirmData confirmData = new ConfirmData();
		final List<SettlementGoods> goodsList = new ArrayList<SettlementGoods>();
		for (GoodsDetailData d : mList) {
			SettlementGoods goods = new SettlementGoods();
			goods.setGoods_id(d.getId());
			goods.setCount(d.getSelect_count());
			goodsList.add(goods);
		}
		confirmData.setGoods_list(goodsList);
		if (shdzLiuyan.getText().toString() != null) {
			confirmData.setOrder_info(shdzLiuyan.getText().toString());
		}
		if (address != null && address.getId() != null) {
			confirmData.setShip_address_id(address.getId());
			ControllerManager.getInstance().getOrderController()
					.unRegisterAll();
			ControllerManager.getInstance().getOrderController()
					.registerNotification(new IViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							if (obj.getCode() == 200) {
								removeInCart(goodsList);
								Message updateViewMsg = mHandler
										.obtainMessage(JIESUAN_SUCCESS);
								mHandler.sendMessage(updateViewMsg);
							} else {
								Message updateViewMsg = mHandler
										.obtainMessage(NET_ERROR);
								mHandler.sendMessage(updateViewMsg);
							}
						}
					});
			if (totalPrice >= Float.valueOf(ClientData.getInstance().getMinLimit())) {
				ControllerManager.getInstance().getOrderController()
						.confirmBuy(confirmData);
			} else {
				Message updateViewMsg = mHandler
						.obtainMessage(LIMIT_PRICE_ERROR);
				mHandler.sendMessage(updateViewMsg);
			}

		} else {
			Message updateViewMsg = mHandler.obtainMessage(HAVE_NO_ADDRESS);
			mHandler.sendMessage(updateViewMsg);
		}
	}

	public void removeInCart(List<SettlementGoods> goodsList) {
		String cartData = ClientData.getInstance().getmCart();
		if (cartData != null) {
			try {
				List<ShopCart> cartObj = GsonTool.fromJson(
						cartData, new TypeToken<List<ShopCart>>() {
						});
				int i, j;
				for (i = 0; i < cartObj.size(); i++) {
					for (j = 0; j < goodsList.size(); j++) {
						if (cartObj.get(i).getGoods_id()
								.equals(goodsList.get(j).getGoods_id())) {
							cartObj.remove(i);
						}
					}
				}
				CartDataHelper.updateCacheCart(cartObj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void softBack(View v) {
		this.finish();
	}

}
