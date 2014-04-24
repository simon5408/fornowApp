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

import com.fornow.app.datapool.ClientData;
import com.fornow.app.net.ControllerListener;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.net.dao.DaoManager;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class FavoritesController extends
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

	public void getData() {
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
			ControllerListener ctr = new ControllerListener() {

				@Override
				public void callback(NetResponse response) {
					// TODO Auto-generated method stub
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
			ControllerListener ctr = new ControllerListener() {

				@Override
				public void callback(NetResponse response) {
					// TODO Auto-generated method stub
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
