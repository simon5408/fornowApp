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
package com.fornow.app.controller;

import java.util.ArrayList;
import java.util.List;

import android.os.Message;

import com.fornow.app.dao.DaoManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.net.ControllerListener;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.AppClass;
import com.fornow.app.utils.GsonTool;
import com.google.gson.reflect.TypeToken;

/**
 * @author Simon Lv 2013-10-23
 */
public class ShoppingCartController extends
		AbstractController<ViewListener, String> {
	public void registerNotification(ViewListener notification) {
		super.register(notification);
	}

	public void unRegisterNotification(ViewListener notification) {
		super.unRegister(notification);
	}

	public void unRegisterAll() {
		super.unRegisterAll();
	}

	public void getCartDataFromCache() {
		String cacheCart = ClientData.getInstance().getmCart();
		ViewUpdateObj viewObj = new ViewUpdateObj();
		viewObj.setData(cacheCart);
		if (mNotifiables != null) {
			mNotifiables.updateView(viewObj);
		}
	}

	public void getCartData() {
		final ViewUpdateObj viewObj = new ViewUpdateObj();
		final String cacheCart = ClientData.getInstance().getmCart();
		viewObj.setData(cacheCart);
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			// TODO get cart
			ControllerListener ctr = new ControllerListener() {
				@Override
				public void callback(NetResponse response) {
					// TODO Auto-generated method stub

					viewObj.setCode(response.code);
					if (response.code == 200) {
						List<ShopCart> newCart = null;
						try {
							List<ShopCart> backCart = GsonTool.getGsonTool()
									.fromJson(response.res,
											new TypeToken<List<ShopCart>>() {
											}.getType());
							if (cacheCart != null) {
								List<ShopCart> cachedCart = GsonTool
										.getGsonTool()
										.fromJson(
												cacheCart,
												new TypeToken<List<ShopCart>>() {
												}.getType());

								int i, j, cachedSize = cachedCart.size(), backSize = backCart
										.size();
								List<ShopCart> sameCart = new ArrayList<ShopCart>();
								newCart = new ArrayList<ShopCart>();
								for (i = 0; i < cachedSize; i++) {
									for (j = 0; j < backSize; j++) {
										if (cachedCart
												.get(i)
												.getGoods_id()
												.equals(backCart.get(j)
														.getGoods_id())) {
											sameCart.add(cachedCart.get(i));
										}
									}
								}

								boolean flag = true;
								for (i = 0; i < cachedSize; i++) {
									flag = true;
									for (j = 0; j < sameCart.size(); j++) {
										if (cachedCart
												.get(i)
												.getGoods_id()
												.equals(sameCart.get(j)
														.getGoods_id())) {
											flag = false;
										}
									}
									if (flag) {
										newCart.add(cachedCart.get(i));
									}
								}
								newCart.addAll(backCart);

							} else {
								newCart = backCart;
							}
							String strCart = GsonTool.getGsonTool().toJson(
									newCart);
							ClientData.getInstance().setmCart(strCart);
							viewObj.setData(GsonTool.getGsonTool().toJson(
									newCart));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (mNotifiables != null) {
						mNotifiables.updateView(viewObj);
					}
				}
			};
			DaoManager.getInstance().getCartDao().getCart(uuid, ctr);
		} else {
			if (mNotifiables != null) {
				mNotifiables.updateView(viewObj);
			}
		}
	}

	public void addCart(ShopCart cart) {
		boolean flag = true;
		try {
			String cacheCart = ClientData.getInstance().getmCart();
			List<ShopCart> localCart;
			if (cacheCart != null) {
				localCart = GsonTool.getGsonTool().fromJson(cacheCart,
						new TypeToken<List<ShopCart>>() {
						}.getType());
			} else {
				localCart = new ArrayList<ShopCart>();
			}
			if (cart != null) {
				for (ShopCart s : localCart) {
					if (s.getGoods_id().equals(cart.getGoods_id())) {
						int count = s.getCount();
						count += cart.getCount();
						s.setCount(count);
						flag = false;
					}
				}
				if (flag) {
					localCart.add(cart);
				}
				updateCartData(localCart);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateCartData(List<ShopCart> cartData) {
		try {
			if (cartData != null) {
				String strCart = GsonTool.getGsonTool().toJson(cartData);
				ClientData.getInstance().setmCart(strCart);
				String uuid = ClientData.getInstance().getmUUID();
				if (uuid != null) {
					ControllerListener ctr = new ControllerListener() {

						@Override
						public void callback(NetResponse response) {
							// TODO Auto-generated method stub
							ViewUpdateObj viewObj = new ViewUpdateObj();
							viewObj.setCode(response.code);
							if (response.code == 200) {
								viewObj.setData(response.res);
								Message updateViewMsg = AppClass.globalHandler
										.obtainMessage(AppClass.UPDATE_CART_COUNT);
								AppClass.globalHandler
										.sendMessage(updateViewMsg);
							}
							if (mNotifiables != null) {
								mNotifiables.updateView(viewObj);
							}
						}
					};
					DaoManager.getInstance().getCartDao()
							.updateCart(uuid, strCart, ctr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
