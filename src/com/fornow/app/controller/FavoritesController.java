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

import com.fornow.app.dao.DaoManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IControllerListener;
import com.fornow.app.service.IViewListener;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class FavoritesController extends
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

	public void getData() {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			IControllerListener ctr = new IControllerListener() {

				@Override
				public void callback(NetResponse response) {
					ViewUpdateObj viewObj = new ViewUpdateObj();
					viewObj.setCode(response.code);
					if (response.code == 200) {
						viewObj.setData(response.res);
						ClientData.getInstance().setmFavorites(response.res);
					}
					if (mNotifiables != null) {
						mNotifiables.updateView(viewObj);
					}
				}

			};
			DaoManager.getInstance().getFavoritesDao().getFavorites(uuid, ctr);
		}
	}

	public void add2Fav(String goodsId) {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			IControllerListener ctr = new IControllerListener() {

				@Override
				public void callback(NetResponse response) {
					ViewUpdateObj viewObj = new ViewUpdateObj();
					viewObj.setCode(response.code);
					if (response.code == 200) {
						viewObj.setData(response.res);
						getData();
					}
					if (mNotifiables != null) {
						mNotifiables.updateView(viewObj);
					}
				}

			};
			DaoManager.getInstance().getFavoritesDao()
					.add2Favorites(uuid, goodsId, ctr);
		}
	}

	public void delFromFav(String goodsId) {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			IControllerListener ctr = new IControllerListener() {

				@Override
				public void callback(NetResponse response) {
					ViewUpdateObj viewObj = new ViewUpdateObj();
					viewObj.setCode(response.code);
					if (response.code == 200) {
						viewObj.setData(response.res);
						getData();
					}
					if (mNotifiables != null) {
						mNotifiables.updateView(viewObj);
					}
				}

			};
			DaoManager.getInstance().getFavoritesDao()
					.delFromFavorites(uuid, goodsId, ctr);
		}
	}
}
