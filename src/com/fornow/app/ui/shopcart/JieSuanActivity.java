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

import java.util.ArrayList;
import java.util.List;

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.ConfirmData;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.SettlementGoods;
import com.fornow.app.model.ShipAddressData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.addressmanager.SelectShdzActivity;
import com.fornow.app.ui.customdialog.EditCountDialog;
import com.fornow.app.ui.customdialog.LoginDialog;
import com.fornow.app.ui.goodsdetail.GoodDetailActivity;
import com.fornow.app.utils.GsonTool;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.fornow.app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Simon Lv 2013-11-17
 */
public class JieSuanActivity extends Activity {
	private LinearLayout initShdz;
	private Button addShdz;
	private TextView shdzShr, shdzPhone, shdzAddress, jiesuanTotalPriceView;
	private EditText shdzLiuyan;
	private FrameLayout jiesuanContent;
	private ListView listView;
	private Context mContext;
	private Dialog dialog;
	AnimateDismissAdapter<String> animateDismissAdapter;
	private Handler mHandler;
	private List<GoodsDetailData> mList;
	private JiesuanAdapter mAdapter;
	private ShipAddressData address;
	public static final int NET_ERROR = 0x03, JIESUAN_SUCCESS = 0x04,
			HAVE_NO_ADDRESS = 0x05, LIMIT_PRICE_ERROR = 0x06,
			COUNT_EDIT = 0x0a, GET_DATA_SUCCESS = 0x07, UUID_TIMEOUT = 0x08,
			GOODS_NOT_SELL = 0x00, GOODS_SELL_OUT = 0x01, GOODS_TIMEOUT = 0x02,
			GOODS_NOT_IN_AREA = 0x09;
	private Float totalPrice = 0.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jiesuan);
		mContext = this.getApplicationContext();
		dialog = new LoadingAnim(JieSuanActivity.this, R.style.my_dialog);
		initShdz = (LinearLayout) findViewById(R.id.jiesuan_init_shdz);
		addShdz = (Button) findViewById(R.id.jiesuan_add_shdz);
		shdzShr = (TextView) findViewById(R.id.jiesuan_shdz_shr);
		shdzPhone = (TextView) findViewById(R.id.jiesuan_shdz_phone);
		shdzAddress = (TextView) findViewById(R.id.jiesuan_shdz_address);
		jiesuanTotalPriceView = (TextView) findViewById(R.id.jiesuan_total_price);
		shdzLiuyan = (EditText) findViewById(R.id.jiesuan_liuyan);
		listView = (ListView) findViewById(R.id.listView01);
		jiesuanContent = (FrameLayout) findViewById(R.id.jiesuanContent);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String data;
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
				TextView toastText = (TextView) view
						.findViewById(R.id.toast_text);
				Toast toast = new Toast(JieSuanActivity.this);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(view);
				switch (msg.what) {
				case NET_ERROR:
					dialog.dismiss();
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					toast.show();
					break;
				case GOODS_NOT_SELL:
					dialog.dismiss();
					toastText.setText(getResources().getString(
							R.string.str_goods_not_sell));
					toast.show();
					break;
				case GOODS_SELL_OUT:
					dialog.dismiss();
					toastText.setText(getResources().getString(
							R.string.str_goods_sell_out));
					toast.show();
					break;
				case GOODS_TIMEOUT:
					dialog.dismiss();
					toastText.setText(getResources().getString(
							R.string.str_goods_timeout));
					toast.show();
					break;
				case GOODS_NOT_IN_AREA:
					dialog.dismiss();
					toastText.setText(getResources().getString(
							R.string.str_goods_not_in_area));
					toast.show();
					break;
				case JIESUAN_SUCCESS:
					dialog.dismiss();
					mAdapter.notifyDataSetChanged();
					break;
				case HAVE_NO_ADDRESS:
					toastText.setText(getResources().getString(
							R.string.str_have_no_address));
					toast.show();
					break;
				case LIMIT_PRICE_ERROR:
					toastText.setText(getResources().getString(
							R.string.str_limit_error0)
							+ ClientData.getInstance().getMinLimit()
							+ getResources().getString(
									R.string.str_limit_error1));
					toast.show();
					break;
				case GET_DATA_SUCCESS:
					try {
						data = msg.getData().getString("data");
						String strDefaultAddress = null;
						if (data != null) {
							ShipAddressData defaultAddress = null;
							List<ShipAddressData> addressData = GsonTool
									.getGsonTool()
									.fromJson(
											data,
											new TypeToken<List<ShipAddressData>>() {
											}.getType());
							for (ShipAddressData d : addressData) {
								if (d.isIsdefault()) {
									defaultAddress = d;
									break;
								}
							}
							if (defaultAddress != null) {
								strDefaultAddress = GsonTool.getGsonTool()
										.toJson(defaultAddress);
							}
						}
						initData(strDefaultAddress);

					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case COUNT_EDIT:
					final int position = Integer.valueOf(msg.getData()
							.getString("position"));

					final EditCountDialog editDialogBuilder = new EditCountDialog(
							JieSuanActivity.this, R.style.Theme_Dialog);
					editDialogBuilder.setEditCount(mList.get(position)
							.getSelect_count() + "");
					Spannable spanText = (Spannable) editDialogBuilder
							.getEditCount();
					Selection.setSelection(spanText, editDialogBuilder
							.getEditCount().length());
					editDialogBuilder
							.setOnCancelBtnListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									editDialogBuilder.dismiss();
								}
							});

					editDialogBuilder
							.setOnConfirmBtnListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									editDialogBuilder.dismiss();
									if (editDialogBuilder.getEditCount()
											.length() != 0) {
										int count = Integer
												.parseInt(editDialogBuilder
														.getEditCount()
														.toString());
										mList.get(position).setSelect_count(
												count);
										mAdapter.notifyDataSetChanged();
										totalPrice = 0.0f;
										for (GoodsDetailData d : mList) {
											totalPrice += Float.valueOf(JiesuanAdapter.itemTotalPrice(
													d.getCurrent_price() + "",
													d.getSelect_count()));
										}
										jiesuanTotalPriceView
												.setText(totalPrice + "");
									}
								}
							});

					editDialogBuilder.show();
					break;
				case UUID_TIMEOUT:
					dialog.dismiss();
					LoginDialog loginDialog = new LoginDialog(
							JieSuanActivity.this, mContext, mContext
									.getResources().getString(
											R.string.str_tishi), mContext
									.getResources().getString(
											R.string.str_uuid_timeout), 0);
					loginDialog.build();
					break;
				default:
					break;
				}
			}
		};
		getShdz();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void initData(String strAddress) {
		dialog.dismiss();
		Intent intent = getIntent();
		try {
			if (intent.getExtras() != null) {
				if (intent.getExtras().get("data") != null) {
					mList = GsonTool.getGsonTool().fromJson(
							intent.getExtras().get("data") + "",
							new TypeToken<List<GoodsDetailData>>() {
							}.getType());
					totalPrice = 0.0f;
					for (GoodsDetailData d : mList) {
						totalPrice += Float.valueOf(JiesuanAdapter
								.itemTotalPrice(d.getCurrent_price() + "",
										d.getSelect_count()));
					}
					jiesuanTotalPriceView.setText(totalPrice + "");
					mAdapter = new JiesuanAdapter(mList, mContext, mHandler);
					listView.setAdapter(mAdapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							go2detail(GsonTool.getGsonTool().toJson(
									mList.get(position)));
						}
					});
				}
				if (intent.getExtras() != null && strAddress != null) {
					address = GsonTool.getGsonTool().fromJson(strAddress,
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

				jiesuanContent.setVisibility(View.VISIBLE);
			} else {
				jiesuanContent.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jiesuanContent.setVisibility(View.INVISIBLE);
		}
	}

	public void getShdz() {
		dialog.show();
		ControllerManager.getInstance().getAddressManageController()
				.unRegisterAll();
		ControllerManager.getInstance().getAddressManageController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						// TODO Auto-generated method stub
						Message updateViewMsg;
						switch (obj.getCode()) {
						case 200:
							updateViewMsg = mHandler
									.obtainMessage(GET_DATA_SUCCESS);
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
		ControllerManager.getInstance().getAddressManageController()
				.getShippingAddress();
	}

	public void selectShdz(View v) {
		Intent intent = new Intent(JieSuanActivity.this,
				SelectShdzActivity.class);
		startActivityForResult(intent, 0);
	}

	public void go2detail(String detail) {
		if (detail != null) {
			Intent intent = new Intent(JieSuanActivity.this,
					GoodDetailActivity.class);
			intent.putExtra("data", detail);
			startActivity(intent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (data.getExtras() != null
					&& data.getExtras().getString("addressData") != null) {
				String strAddress = data.getExtras().getString("addressData");
				try {
					address = GsonTool.getGsonTool().fromJson(strAddress,
							ShipAddressData.class);
					initShdz.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
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
					.registerNotification(new ViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							// TODO Auto-generated method stub
							Message updateViewMsg;
							switch (obj.getCode()) {
							case 200:
								removeInCart(goodsList);

								Intent intent = new Intent(
										JieSuanActivity.this,
										JieSuanSuccessActivity.class);
								startActivity(intent);
								break;
							case 408:
								updateViewMsg = mHandler
										.obtainMessage(UUID_TIMEOUT);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 403:
								updateViewMsg = mHandler
										.obtainMessage(GOODS_NOT_SELL);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 406:
								updateViewMsg = mHandler
										.obtainMessage(GOODS_SELL_OUT);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 401:
								updateViewMsg = mHandler
										.obtainMessage(GOODS_TIMEOUT);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 404:
								updateViewMsg = mHandler
										.obtainMessage(GOODS_NOT_IN_AREA);
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
			if (totalPrice >= Float.valueOf(ClientData.getInstance()
					.getMinLimit())) {
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
				List<ShopCart> cartObj = GsonTool.getGsonTool().fromJson(
						cartData, new TypeToken<List<ShopCart>>() {
						}.getType());
				int i, j;
				for (i = 0; i < cartObj.size(); i++) {
					for (j = 0; j < goodsList.size(); j++) {
						if (cartObj.get(i).getGoods_id()
								.equals(goodsList.get(j).getGoods_id())) {
							cartObj.remove(i);
						}
					}
				}
				ControllerManager.getInstance().getShopCartController()
						.unRegisterAll();
				ControllerManager.getInstance().getShopCartController()
						.registerNotification(new ViewListener() {

							@Override
							public void updateView(ViewUpdateObj obj) {
								// TODO
								// Auto-generated
								// method stub
								Message updateViewMsg;
								switch (obj.getCode()) {
								case 200:
									updateViewMsg = mHandler
											.obtainMessage(JIESUAN_SUCCESS);
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

				ControllerManager.getInstance().getShopCartController()
						.updateCartData(cartObj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void softBack(View v) {
		this.finish();
	}

}
