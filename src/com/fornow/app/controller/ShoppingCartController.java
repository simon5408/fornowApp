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

import com.fornow.app.dao.DaoManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IControllerListener;
import com.fornow.app.service.IViewListener;
import com.fornow.app.ui.shopcart.CartDataHelper;
import com.fornow.app.util.GsonTool;
import com.google.gson.reflect.TypeToken;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class ShoppingCartController extends
		AbstractController<IViewListener, String> {
	public void registerNotification(IViewListener notification) {
		super.register(notification);
	}

	public void unRegisterNotification(IViewListener notification) {
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
			// get cart
			IControllerListener ctr = new IControllerListener() {
				@Override
				public void callback(NetResponse response) {
					viewObj.setCode(response.code);
					if (response.code == 200) {
						List<ShopCart> newCart = null;
						try {
							List<ShopCart> backCart = GsonTool
									.fromJson(response.res,
											new TypeToken<List<ShopCart>>() {
											});
							if (cacheCart != null) {
								List<ShopCart> cachedCart = GsonTool
										.fromJson(
												cacheCart,
												new TypeToken<List<ShopCart>>() {
												});

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
							CartDataHelper.updateCacheCart(newCart);
							CartDataHelper.syncCart();
							viewObj.setData(GsonTool.toJson(
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

	// 删除本地
	public void deleteCacheCart() {

	}

	// 用户退出后需要删除本地所有cart
	public void deleteAllCacheCart() {
		ClientData.getInstance().setmCart(null);
		CartDataHelper.updateCacheCart(null);
	}

}
