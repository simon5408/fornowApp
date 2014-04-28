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
package com.fornow.app.ui.goodsdetail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.GoodsDetailData;
import com.fornow.app.model.ImgData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.net.IViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.customdialog.EditCountDialog;
import com.fornow.app.ui.customdialog.LoginDialog;
import com.fornow.app.ui.home.AutoPlayGallery;
import com.fornow.app.ui.home.ImageAdapter;
import com.fornow.app.ui.loadimg.AsyncImgLoader;
import com.fornow.app.ui.loadimg.AsyncImgLoader.ImageCallback;
import com.fornow.app.ui.shopcart.JieSuanActivity;
import com.fornow.app.utils.GsonTool;
import com.google.gson.reflect.TypeToken;
import com.fornow.app.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Simon Lv 2013-11-14
 */
public class GoodDetailActivity extends Activity {
	private GoodsDetailData detailData;
	private Context mContext;
	private static final int NET_ERROR = 0x01, ADD_FAVOR_SUCCESS = 0x02,
			DEL_FAVOR_SUCCESS = 0x03, BIG_IMG_LOADED = 0x04,
			UUID_TIMEOUT = 0x05, ADD_FAVOR_ERROR = 0x06,
			ADD_CART_SUCCESS = 0x07;
	private AutoPlayGallery gallery;
	private TextView detailName, detailCurrentPrice, detailOriginPrice,
			sellCount, detailIntroduction, groupbuyTimeView, detailDeliverArea,
			isFavoriteText;
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
		mContext = this.getApplicationContext();
		initHandler();
		initView();
		bindData();
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	}

	@SuppressLint("HandlerLeak")
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
					isFavoriteText.setText(getResources().getString(
							R.string.str_yishoucang));
					isFavoriteText.setTextColor(getResources().getColor(
							R.color.yishoucangColor));
					break;
				case DEL_FAVOR_SUCCESS:
					boolFavorite = false;
					favoriteFlagView.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.favorite_no));
					isFavoriteText.setText(getResources().getString(
							R.string.str_shoucang));
					isFavoriteText.setTextColor(getResources().getColor(
							R.color.manColor));
					break;
				case ADD_FAVOR_ERROR:
					toastText.setText(getResources().getString(
							R.string.str_add2fav_success));
					toast.show();
					boolFavorite = true;
					favoriteFlagView.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.favorite_yes));
					isFavoriteText.setText(getResources().getString(
							R.string.str_yishoucang));
					isFavoriteText.setTextColor(getResources().getColor(
							R.color.yishoucangColor));
					break;
				case UUID_TIMEOUT:
					dialog.dismiss();
					LoginDialog loginDialog = new LoginDialog(
							GoodDetailActivity.this, mContext, mContext
									.getResources().getString(
											R.string.str_tishi), mContext
									.getResources().getString(
											R.string.str_uuid_timeout), 0);
					loginDialog.build();
					break;
				case ADD_CART_SUCCESS:
					toastText.setText(getResources().getString(
							R.string.str_add2cart_success));
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
		isFavoriteText = (TextView) findViewById(R.id.is_favorite_text);
		groupbuyTimeView = (TextView) findViewById(R.id.detail_groupbuy_end_time);
		detailDeliverArea = (TextView) findViewById(R.id.detail_deliver_area);
		detailIntroduction = (TextView) findViewById(R.id.detail_introduction);
		dialog = new LoadingAnim(GoodDetailActivity.this, R.style.my_dialog);
	}

	@SuppressLint("SimpleDateFormat")
	public void bindData() {
		Intent intent = getIntent();
		String detail = intent.getStringExtra("data");

		if (detail != null) {
			try {
				detailData = GsonTool.getGsonTool().fromJson(detail,
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
					detailName.setText(detailData.getName());
				}
				if (detailData.getCurrent_price() != null) {
					detailCurrentPrice.setText(detailData.getCurrent_price()
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
				if (detailData.getDeliver_area() != null) {
					String deliverArea = getResources().getString(
							R.string.str_psfw);
					for (int i = 0; i < detailData.getDeliver_area().length; i++) {
						deliverArea += detailData.getDeliver_area()[i]
								.getArea_name() + " ";
					}
					detailDeliverArea.setText(deliverArea);
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
			List<GoodsDetailData> favList = GsonTool.getGsonTool().fromJson(
					favData, new TypeToken<List<GoodsDetailData>>() {
					}.getType());
			Iterator<GoodsDetailData> itr = favList.iterator();
			while (itr.hasNext()) {
				if (itr.next().getId().equals(goodId)) {
					boolFavorite = true;
					favoriteFlagView.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.favorite_yes));
					isFavoriteText.setText(getResources().getString(
							R.string.str_yishoucang));
					isFavoriteText.setTextColor(getResources().getColor(
							R.color.yishoucangColor));
				}
			}
		}
	}

	public void addFavorite(View v) {
		if (isLogin(0)) {
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
							
							Message updateViewMsg;
							switch (obj.getCode()) {
							case 200:
								updateViewMsg = mHandler
										.obtainMessage(ADD_FAVOR_SUCCESS);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 408:
								updateViewMsg = mHandler
										.obtainMessage(UUID_TIMEOUT);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 409:
								updateViewMsg = mHandler
										.obtainMessage(ADD_FAVOR_ERROR);
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
			ControllerManager.getInstance().getFavoritesController()
					.add2Fav(detailData.getId());
		} else {
			ControllerManager.getInstance().getFavoritesController()
					.unRegisterAll();
			ControllerManager.getInstance().getFavoritesController()
					.registerNotification(new IViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							
							Message updateViewMsg;
							switch (obj.getCode()) {
							case 200:
								updateViewMsg = mHandler
										.obtainMessage(DEL_FAVOR_SUCCESS);
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
			ControllerManager.getInstance().getFavoritesController()
					.delFromFav(detailData.getId());
		}
	}

	public void confirmBuy(View v) {
		if (isLogin(1)) {
			jiesuan();
		}
	}

	public void add2cart(View v) {
		if (isLogin(2)) {
			excuteAdd2cart();
		}
	}

	public boolean isLogin(final int resultCode) {
		if (ClientData.getInstance().getmUUID() == null) {
			LoginDialog loginDialog = new LoginDialog(GoodDetailActivity.this,
					mContext, mContext.getResources().getString(
							R.string.str_tishi), mContext.getResources()
							.getString(R.string.str_not_login_msg), resultCode);
			loginDialog.build();
			return false;
		} else {
			return true;
		}
	}

	public void jiesuan() {
		final EditCountDialog editDialogBuilder = new EditCountDialog(
				GoodDetailActivity.this, R.style.Theme_Dialog);
		editDialogBuilder.setEditCount(detailData.getSelect_count() + "");
		Spannable spanText = (Spannable) editDialogBuilder.getEditCount();
		Selection.setSelection(spanText, editDialogBuilder.getEditCount()
				.length());
		editDialogBuilder.setOnCancelBtnListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				editDialogBuilder.dismiss();
				detailData.setSelect_count(1);
			}
		});

		editDialogBuilder.setOnConfirmBtnListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				editDialogBuilder.dismiss();
				if (editDialogBuilder.getEditCount().length() != 0) {
					int count = Integer.parseInt(editDialogBuilder
							.getEditCount().toString());
					detailData.setSelect_count(count);
					List<GoodsDetailData> goodsList = new ArrayList<GoodsDetailData>();
					goodsList.add(detailData);
					String strGoodsList = GsonTool.getGsonTool().toJson(
							goodsList);

					Intent intent = new Intent(GoodDetailActivity.this,
							JieSuanActivity.class);
					intent.putExtra("data", strGoodsList);
					startActivity(intent);
				}
			}
		});

		editDialogBuilder.show();
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
				// jiesuan();
				break;
			default:
				break;
			}

			break;
		default:
			break;
		}
	}

	public void excuteAdd2cart() {
		final EditCountDialog editDialogBuilder = new EditCountDialog(
				GoodDetailActivity.this, R.style.Theme_Dialog);
		editDialogBuilder.setEditCount(detailData.getSelect_count() + "");
		Spannable spanText = (Spannable) editDialogBuilder.getEditCount();
		Selection.setSelection(spanText, editDialogBuilder.getEditCount()
				.length());
		editDialogBuilder.setOnCancelBtnListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				editDialogBuilder.dismiss();
				detailData.setSelect_count(1);
			}
		});

		editDialogBuilder.setOnConfirmBtnListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				editDialogBuilder.dismiss();
				if (editDialogBuilder.getEditCount().length() != 0) {
					int count = Integer.parseInt(editDialogBuilder
							.getEditCount().toString());
					detailData.setSelect_count(count);

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
							cart.setOriginal_price(detailData
									.getOriginal_price());
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
					ControllerManager.getInstance().getShopCartController()
							.unRegisterAll();
					ControllerManager.getInstance().getShopCartController()
							.registerNotification(new IViewListener() {

								@Override
								public void updateView(ViewUpdateObj obj) {
									
									Message updateViewMsg;
									switch (obj.getCode()) {
									case 200:
										updateViewMsg = mHandler
												.obtainMessage(ADD_CART_SUCCESS);
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
							.addCart(cart);
				}
			}
		});

		editDialogBuilder.show();
	}

	public void softBack(View v) {
		this.finish();
	}
}
