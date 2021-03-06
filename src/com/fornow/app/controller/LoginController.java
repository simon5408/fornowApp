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
import com.fornow.app.model.User;
import com.fornow.app.model.UserInfo;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IControllerListener;
import com.fornow.app.service.IViewListener;
import com.fornow.app.ui.shopcart.CartDataHelper;
import com.fornow.app.util.GsonTool;
import com.fornow.app.util.LogUtils;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class LoginController extends AbstractController<IViewListener, String> {
	private static final String TAG = LoginController.class.getName();
	public enum loginType {
		LOGIN, REGISTER
	};

	public void registerNotification(IViewListener notification) {
		super.register(notification);
	}

	public void unRegisterNotification(IViewListener notification) {
		super.unRegister(notification);
	}

	public void unRegisterAll() {
		super.unRegisterAll();
	}

	public void login(LoginData param) {
		sendLoginOrRegisterReq(param, loginType.LOGIN);
	}

	public void register(LoginData param) {
		sendLoginOrRegisterReq(param, loginType.REGISTER);
	}

	private void sendLoginOrRegisterReq(LoginData param, loginType type) {
		IControllerListener ctr = new IControllerListener() {

			@Override
			public void callback(NetResponse response) {
				// cache the user data
				ViewUpdateObj viewObj = new ViewUpdateObj();
				viewObj.setCode(response.code);
				if (response.code == 200) {
					try {
						User user = GsonTool.fromJson(
								response.res, User.class);
						String userInfo = GsonTool.toJson(
								user.getUserinfo());
						ClientData.getInstance().setmUUID(user.getUuid());
						ClientData.getInstance().setUser(userInfo);
						viewObj.setData(response.res);
						CartDataHelper.getCartData();
						ControllerManager.getInstance().getFavoritesController().getData();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (mNotifiables != null) {
					mNotifiables.updateView(viewObj);
				}
			}
		};
		switch (type) {
		case LOGIN:
			DaoManager.getInstance().getUserDao().login(param, ctr);
			break;
		case REGISTER:
			DaoManager.getInstance().getUserDao().register(param, ctr);
			break;
		default:
			break;
		}

	}

	public void logout() {
		IControllerListener ctr = new IControllerListener() {

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
			IControllerListener ctr = new IControllerListener() {

				@Override
				public void callback(NetResponse response) {
					ViewUpdateObj viewObj = new ViewUpdateObj();
					viewObj.setCode(response.code);
					if (response.code == 200) {
						try {
							User user = GsonTool.fromJson(
									response.res, User.class);
							String userInfo = GsonTool.toJson(
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
				String userInfo = GsonTool.toJson(user);
				LogUtils.d(TAG, "--------set userInfo:" + userInfo);
				DaoManager.getInstance().getUserDao()
						.updateUser(uuid, userInfo, ctr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
