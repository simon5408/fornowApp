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
import com.fornow.app.model.LoginData;
import com.fornow.app.model.RegisterData;
import com.fornow.app.model.User;
import com.fornow.app.model.UserInfo;
import com.fornow.app.net.ControllerListener;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.utils.GsonTool;

/**
 * @author Simon Lv 2013-8-16
 */
public class LoginController extends AbstractController<ViewListener, String> {
	public enum loginType {
		LOGIN, REGISTER
	};

	public void registerNotification(ViewListener notification) {
		super.register(notification);
	}

	public void unRegisterNotification(ViewListener notification) {
		super.unRegister(notification);
	}

	public void unRegisterAll() {
		super.unRegisterAll();
	}

	public void login(LoginData param) {
		ControllerListener ctr = new ControllerListener() {

			@Override
			public void callback(NetResponse response) {
				
				// cache the user data
				ViewUpdateObj viewObj = new ViewUpdateObj();
				viewObj.setCode(response.code);
				if (response.code == 200) {
					try {
						User user = GsonTool.getGsonTool().fromJson(
								response.res, User.class);
						String userInfo = GsonTool.getGsonTool().toJson(
								user.getUserinfo());
						ClientData.getInstance().setmUUID(user.getUuid());
						ClientData.getInstance().setUser(userInfo);
						viewObj.setData(response.res);
						ControllerManager.getInstance().getShopCartController()
								.getCartData();
						ControllerManager.getInstance()
								.getFavoritesController().getData();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (mNotifiables != null) {
					mNotifiables.updateView(viewObj);
				}
			}
		};
		DaoManager.getInstance().getUserDao().login(param, ctr);
	}

	public void register(String deviceId, RegisterData param) {
		ControllerListener ctr = new ControllerListener() {

			@Override
			public void callback(NetResponse response) {
				
				// cache the user data
				ViewUpdateObj viewObj = new ViewUpdateObj();
				viewObj.setCode(response.code);
				if (response.code == 200) {
					try {
						User user = GsonTool.getGsonTool().fromJson(
								response.res, User.class);
						String userInfo = GsonTool.getGsonTool().toJson(
								user.getUserinfo());
						ClientData.getInstance().setmUUID(user.getUuid());
						ClientData.getInstance().setUser(userInfo);
						viewObj.setData(response.res);
						ControllerManager.getInstance().getShopCartController()
								.getCartData();
						ControllerManager.getInstance()
								.getFavoritesController().getData();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (mNotifiables != null) {
					mNotifiables.updateView(viewObj);
				}
			}
		};
		DaoManager.getInstance().getUserDao().register(deviceId, param, ctr);
	}

	public void logout() {
		ControllerListener ctr = new ControllerListener() {

			@Override
			public void callback(NetResponse response) {
				
				ViewUpdateObj viewObj = new ViewUpdateObj();
				viewObj.setCode(response.code);
				if (response.code == 200) {
					viewObj.setData(response.res);
				}
				if (mNotifiables != null) {
					mNotifiables.updateView(viewObj);
				}
			}
		};
		DaoManager.getInstance().getUserDao().logout(ctr);
	}

	public void updateUser(UserInfo user) {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			ControllerListener ctr = new ControllerListener() {

				@Override
				public void callback(NetResponse response) {
					
					ViewUpdateObj viewObj = new ViewUpdateObj();
					viewObj.setCode(response.code);
					if (response.code == 200) {
						try {
							User user = GsonTool.getGsonTool().fromJson(
									response.res, User.class);
							String userInfo = GsonTool.getGsonTool().toJson(
									user.getUserinfo());
							ClientData.getInstance().setUser(userInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (mNotifiables != null) {
						mNotifiables.updateView(viewObj);
					}
				}
			};
			try {
				String userInfo = GsonTool.getGsonTool().toJson(user);
				DaoManager.getInstance().getUserDao()
						.updateUser(uuid, userInfo, ctr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void getCheckCode(String phone) {
		ControllerListener ctr = new ControllerListener() {

			@Override
			public void callback(NetResponse response) {
				
				ViewUpdateObj viewObj = new ViewUpdateObj();
				viewObj.setCode(response.code);
				if (response.code == 200) {
					viewObj.setData(response.res);
				}
				if (mNotifiables != null) {
					mNotifiables.updateView(viewObj);
				}
			}
		};
		DaoManager.getInstance().getUserDao().getCheckCode(phone, ctr);
	}

	public void sendSuggestion(String suggestion) {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			ControllerListener ctr = new ControllerListener() {

				@Override
				public void callback(NetResponse response) {
					
					ViewUpdateObj viewObj = new ViewUpdateObj();
					viewObj.setCode(response.code);
					if (response.code == 200) {
						viewObj.setData(response.res);
					}
					if (mNotifiables != null) {
						mNotifiables.updateView(viewObj);
					}
				}
			};
			DaoManager.getInstance().getUserDao()
					.sendSuggestion(uuid, suggestion, ctr);
		}
	}
}
