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

import android.os.Message;

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.ShopCart;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.net.dao.DaoManager;
import com.fornow.app.ui.AppClass;
import com.fornow.app.util.GsonTool;
import com.google.gson.reflect.TypeToken;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class CartDataHelper {

	public static boolean addCart(ShopCart cart) {
		boolean flag = true;
		try {
			String cacheCart = ClientData.getInstance().getmCart();
			List<ShopCart> localCart;
			if (cacheCart != null) {
				localCart = GsonTool.fromJson(cacheCart,
						new TypeToken<List<ShopCart>>() {
						});
			} else {
				localCart = new ArrayList<ShopCart>();
			}
			if (cart != null) {
				for (ShopCart s : localCart) {
					if (s.getGoods_id().equals(cart.getGoods_id())) {
						flag = false;
					}
				}
				if (flag) {
					localCart.add(cart);
					String newCart = GsonTool.toJson(localCart);
					ClientData.getInstance().setmCart(newCart);
					Message updateViewMsg = AppClass.globalHandler
							.obtainMessage(AppClass.UPDATE_CART_COUNT);
					AppClass.globalHandler.sendMessage(updateViewMsg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public static void updateCacheCart(List<ShopCart> cartData) {
		if (cartData != null) {
			try {
				String cart = GsonTool.toJson(cartData);
				ClientData.getInstance().setmCart(cart);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Message updateViewMsg = AppClass.globalHandler
				.obtainMessage(AppClass.UPDATE_CART_COUNT);
		AppClass.globalHandler.sendMessage(updateViewMsg);
	}

	public static void getCartData() {
		ControllerManager.getInstance().getShopCartController().unRegisterAll();
		ControllerManager.getInstance().getShopCartController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						if (obj.getCode() == 200) {
							Message updateViewMsg = AppClass.globalHandler
									.obtainMessage(AppClass.UPDATE_CART_COUNT);
							AppClass.globalHandler.sendMessage(updateViewMsg);
						} else {

						}

					}
				});
		ControllerManager.getInstance().getShopCartController().getCartData();
	}

	// 同步本地数据到后台
	public static void syncCart() {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			String strCart = ClientData.getInstance().getmCart();
			if (strCart != null) {
				DaoManager.getInstance().getCartDao().updateCart(uuid, strCart);
			}
		}

	}
}
