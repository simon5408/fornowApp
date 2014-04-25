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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.ImgData;
import com.fornow.app.model.ShipAddressData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IViewListener;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.home.AutoPlayGallery;
import com.fornow.app.ui.home.ImageAdapter;
import com.fornow.app.ui.loadImg.AsyncImgLoader;
import com.fornow.app.ui.loadImg.AsyncImgLoader.ImageCallback;
import com.fornow.app.ui.mine.LoginActivity;
import com.fornow.app.ui.shopcart.CartDataHelper;
import com.fornow.app.ui.shopcart.JieSuanActivity;
import com.fornow.app.util.GsonTool;
import com.google.gson.reflect.TypeToken;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class GoodDetailActivity extends Activity {
	private GoodsDetailData detailData;
	private static final int GET_DATA_SUCCESS = 0x00, NET_ERROR = 0x01,
			ADD_FAVOR_SUCCESS = 0x02, DEL_FAVOR_SUCCESS = 0x03,
			BIG_IMG_LOADED = 0x04;
	private AutoPlayGallery gallery;
	private TextView detailName, detailCurrentPrice, detailOriginPrice,
			sellCount, detailIntroduction, groupbuyTimeView;
	private ImageButton favoriteFlagView;
	private ArrayList<Drawable> drawables;
	private Dialog dialog;
	private Handler mHandler;
	private boolean boolFavorite = false;
	private ImageAdapter imgAdapter;
	private AsyncImgLoader imgLoader = new AsyncImgLoader();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		initHandler();
		initView();
		bindData();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
				TextView toastText = (TextView) view
						.findViewById(R.id.toast_text);
				Toast toast = new Toast(GoodDetailActivity.this);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(view);
				switch (msg.what) {
				case GET_DATA_SUCCESS:
					dialog.dismiss();
					try {
						String data = msg.getData().getString("data");
						String strDefaultAddress = null;
						if (data != null) {
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
								strDefaultAddress = GsonTool
										.toJson(defaultAddress);
							}
						}
						List<GoodsDetailData> goodsList = new ArrayList<GoodsDetailData>();
						goodsList.add(detailData);
						String strGoodsList = GsonTool.toJson(
								goodsList);

						Intent intent = new Intent(GoodDetailActivity.this,
								JieSuanActivity.class);
						intent.putExtra("data", strGoodsList);
						intent.putExtra("defaultAddress", strDefaultAddress);
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case BIG_IMG_LOADED:
					imgAdapter.notifyDataSetChanged();
					gallery.setAdapter(imgAdapter);
					break;
				case NET_ERROR:
					dialog.dismiss();
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					toast.show();
					break;
				case ADD_FAVOR_SUCCESS:
					boolFavorite = true;
					favoriteFlagView.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.favorite_yes));
					toastText.setText(getResources().getString(
							R.string.str_add2fav_success));
					toast.show();
					break;
				case DEL_FAVOR_SUCCESS:
					boolFavorite = false;
					favoriteFlagView.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.favorite_no));
					toastText.setText(getResources().getString(
							R.string.str_del_from_fav_success));
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

	public void initView() {
		gallery = (AutoPlayGallery) findViewById(R.id.img_gallery);
		gallery.setCallBack(new IViewListener() {

			@Override
			public void updateView(ViewUpdateObj obj) {
			}
		});
		favoriteFlagView = (ImageButton) findViewById(R.id.favorite_flag);
		detailName = (TextView) findViewById(R.id.detail_name);
		detailCurrentPrice = (TextView) findViewById(R.id.detail_current_price);
		detailOriginPrice = (TextView) findViewById(R.id.detail_origin_price);
		sellCount = (TextView) findViewById(R.id.sell_count);
		groupbuyTimeView = (TextView) findViewById(R.id.detail_groupbuy_end_time);
		detailIntroduction = (TextView) findViewById(R.id.detail_introduction);
		dialog = new LoadingAnim(GoodDetailActivity.this, R.style.my_dialog);
	}

	public void bindData() {
		Intent intent = getIntent();
		String detail = intent.getStringExtra("data");

		if (detail != null) {
			try {
				detailData = GsonTool.fromJson(detail,
						GoodsDetailData.class);
				drawables = new ArrayList<Drawable>();
				imgAdapter = new ImageAdapter(this, drawables);
				for (ImgData img : detailData.getImage()) {
					imgLoader.loadDrawable(img.getUrl(), img.getId(),
							new ImageCallback() {
								@Override
								public void imageLoaded(
										final Drawable imageDrawable,
										final String Tag) {
									if (imageDrawable != null) {
										drawables.add(imageDrawable);
									} else {
										drawables
												.add(getResources()
														.getDrawable(
																R.drawable.home_banner01));
									}

									Message updateViewMsg = mHandler
											.obtainMessage(BIG_IMG_LOADED);
									mHandler.sendMessage(updateViewMsg);
								}
							});
				}

				boolFav(detailData.getId());

				if (detailData.getName() != null) {
					detailName.setText(getResources().getString(
							R.string.goods_name_tag)
							+ detailData.getName());
				}
				if (detailData.getCurrent_price() != null) {
					detailCurrentPrice.setText(getResources().getString(
							R.string.goods_unit)
							+ detailData.getCurrent_price()
							+ getResources().getString(R.string.sell_unit));
				}
				if (detailData.getOriginal_price() != null) {
					detailOriginPrice.setText(getResources().getString(
							R.string.goods_unit)
							+ detailData.getOriginal_price()
							+ getResources().getString(R.string.sell_unit));
					detailOriginPrice.getPaint().setFlags(
							Paint.STRIKE_THRU_TEXT_FLAG);
				}
				if (detailData.getEnd_time() != null) {
					groupbuyTimeView.setVisibility(View.VISIBLE);
					DateFormat format = new SimpleDateFormat("MM-dd HH:mm");
					String dateAfterFormat = format.format(new Date(Long
							.parseLong(detailData.getEnd_time())));
					groupbuyTimeView.setText(getResources().getString(
							R.string.str_groupbuy_end_time_tag)
							+ dateAfterFormat);
				} else {
					groupbuyTimeView.setVisibility(View.GONE);
				}
				sellCount.setText(detailData.getSell_out()
						+ getResources().getString(R.string.str_rygm));
				if (detailData.getIntroduction() != null) {
					detailIntroduction.setText(detailData.getIntroduction());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void boolFav(String goodId) {
		String favData = ClientData.getInstance().getmFavorites();
		if (favData != null) {
			List<GoodsDetailData> favList = GsonTool.fromJson(
					favData, new TypeToken<List<GoodsDetailData>>() {
					});
			Iterator<GoodsDetailData> itr = favList.iterator();
			while (itr.hasNext()) {
				if (itr.next().getId().equals(goodId)) {
					boolFavorite = true;
					favoriteFlagView.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.favorite_yes));
				}
			}
		}
	}

	public void addFavorite(View v) {
		if (ClientData.getInstance().getmUUID() == null) {
			AlertDialog.Builder builder = new Builder(GoodDetailActivity.this);
			builder.setMessage("对不起，您未登录账号，请登陆!");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent in = new Intent(GoodDetailActivity.this,
									LoginActivity.class);
							startActivityForResult(in, 0);
						}
					});
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			return;
		} else {
			sendFavorite();
		}
	}

	public void sendFavorite() {
		if (!boolFavorite) {
			ControllerManager.getInstance().getFavoritesController()
					.unRegisterAll();
			ControllerManager.getInstance().getFavoritesController()
					.registerNotification(new IViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							if (obj.getCode() == 200) {
								Message updateViewMsg = mHandler
										.obtainMessage(ADD_FAVOR_SUCCESS);
								mHandler.sendMessage(updateViewMsg);
							} else {
								Message updateViewMsg = mHandler
										.obtainMessage(NET_ERROR);
								mHandler.sendMessage(updateViewMsg);
							}
						}
					});
			ControllerManager.getInstance().getFavoritesController()
					.add2Fav(detailData.getId());
		} else {
			ControllerManager.getInstance().getFavoritesController()
					.unRegisterAll();
			ControllerManager.getInstance().getFavoritesController()
					.registerNotification(new IViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							if (obj.getCode() == 200) {
								Message updateViewMsg = mHandler
										.obtainMessage(DEL_FAVOR_SUCCESS);
								mHandler.sendMessage(updateViewMsg);
							} else {
								Message updateViewMsg = mHandler
										.obtainMessage(NET_ERROR);
								mHandler.sendMessage(updateViewMsg);
							}
						}
					});
			ControllerManager.getInstance().getFavoritesController()
					.delFromFav(detailData.getId());
		}
	}

	public void confirmBuy(View v) {
		if (ClientData.getInstance().getmUUID() == null) {
			AlertDialog.Builder builder = new Builder(GoodDetailActivity.this);
			builder.setMessage("对不起，您未登录账号，请登陆!");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent in = new Intent(GoodDetailActivity.this,
									LoginActivity.class);
							startActivityForResult(in, 1);
						}
					});
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			return;
		} else {
			jiesuan();
		}
	}

	public void jiesuan() {
		dialog.show();
		ControllerManager.getInstance().getAddressManageController()
				.unRegisterAll();
		ControllerManager.getInstance().getAddressManageController()
				.registerNotification(new IViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						if (obj.getCode() == 200) {
							Message updateViewMsg = mHandler
									.obtainMessage(GET_DATA_SUCCESS);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case 0:
				sendFavorite();
				break;
			case 1:
				jiesuan();
				break;
			default:
				break;
			}

			break;
		default:
			break;
		}
	}

	public void add2cart(View v) {
		ShopCart cart = new ShopCart();
		if (detailData != null) {
			if (detailData.getId() != null) {
				cart.setGoods_id(detailData.getId());
			}
			if (detailData.getName() != null) {
				cart.setName(detailData.getName());
			}
			if (detailData.getCategory() != null) {
				cart.setCategory(detailData.getCategory());
			}
			if (detailData.getIcon() != null) {
				cart.setIcon(detailData.getIcon());
			}
			if (detailData.getImage() != null) {
				cart.setImage(detailData.getImage());
			}
			if (detailData.getOriginal_price() != null) {
				cart.setOriginal_price(detailData.getOriginal_price());
			}
			if (detailData.getCurrent_price() != null) {
				cart.setCurrent_price(detailData.getCurrent_price());
			}
			if (detailData.getIntroduction() != null) {
				cart.setIntroduction(detailData.getIntroduction());
			}
			if (detailData.getDeliver_area() != null) {
				cart.setDeliver_area(detailData.getDeliver_area());
			}
			cart.setCount(detailData.getSelect_count());
		}
		boolean success = CartDataHelper.addCart(cart);
		View view = getLayoutInflater().inflate(R.layout.my_toast, null);
		TextView toastText = (TextView) view.findViewById(R.id.toast_text);
		Toast toast = new Toast(GoodDetailActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		if (success) {
			toastText.setText(getResources().getString(
					R.string.str_add2cart_success));
			toast.show();
		} else {
			toastText.setText(getResources().getString(
					R.string.str_add2cart_fail));
			toast.show();
		}
	}

	public void softBack(View v) {
		this.finish();
	}
}
