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
import com.fornow.app.model.ImVersion;
import com.fornow.app.model.LimitPrice;
import com.fornow.app.net.ControllerListener;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.net.dao.DaoManager;
import com.fornow.app.ui.NotifyId;
import com.fornow.app.util.GsonTool;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class SearchController extends AbstractController<ViewListener, String> {

	public void registerNotification(ViewListener notification) {
		super.register(notification);
	}

	public void unRegisterNotification(ViewListener notification) {
		super.unRegister(notification);
	}

	public void unRegisterAll() {
		super.unRegisterAll();
	}

	public void getHomeData() {
		int offset = 0, length = 6;

		DaoManager.getInstance().getSearchDataDao()
				.getBanner(new ControllerListener() {
					@Override
					public void callback(NetResponse response) {
						// TODO Auto-generated method stub
						ViewUpdateObj viewObj = new ViewUpdateObj();
						viewObj.setCode(response.code);
						if (response.code == 200) {
							ClientData.getInstance().setmBanner(response.res);
							viewObj.setData(response.res);
							viewObj.setNotifyId(NotifyId.HOME_BANNER);
						}
						if (mNotifiables != null) {
							mNotifiables.updateView(viewObj);
						}
					}
				});

		DaoManager.getInstance().getSearchDataDao()
				.getPrivilege(offset, length, "All", new ControllerListener() {
					@Override
					public void callback(NetResponse response) {
						// TODO Auto-generated method stub
						ViewUpdateObj viewObj = new ViewUpdateObj();
						viewObj.setCode(response.code);
						if (response.code == 200) {
							ClientData.getInstance()
									.setmPrivilege(response.res);
							viewObj.setData(response.res);
							viewObj.setNotifyId(NotifyId.HOME_PRIVILEGE);
						}
						if (mNotifiables != null) {
							mNotifiables.updateView(viewObj);
						}
					}
				});

		DaoManager.getInstance().getSearchDataDao()
				.getVersion(new ControllerListener() {
					@Override
					public void callback(NetResponse response) {
						// TODO Auto-generated method stub
						ViewUpdateObj viewObj = new ViewUpdateObj();
						viewObj.setCode(response.code);
						if (response.code == 200) {
							viewObj.setData(response.res);
							try {
								ImVersion version = GsonTool
										.getGsonTool()
										.fromJson(response.res, ImVersion.class);
								ClientData.getInstance().setVersion(
										version.getVersion());
							} catch (Exception e) {
								e.printStackTrace();
							}
							viewObj.setNotifyId(NotifyId.VERSION);
						}
						if (mNotifiables != null) {
							mNotifiables.updateView(viewObj);
						}
					}
				});

		DaoManager.getInstance().getSearchDataDao()
				.getLimitPrice(new ControllerListener() {
					@Override
					public void callback(NetResponse response) {
						// TODO Auto-generated method stub
						ViewUpdateObj viewObj = new ViewUpdateObj();
						viewObj.setCode(response.code);
						if (response.code == 200) {
							viewObj.setData(response.res);
							try {
								LimitPrice limitPrice = GsonTool.getGsonTool()
										.fromJson(response.res,
												LimitPrice.class);
								ClientData.getInstance().setMinLimit(
										limitPrice.getSendPrice() + "");
							} catch (Exception e) {
								e.printStackTrace();
							}
							viewObj.setNotifyId(NotifyId.MIN_LIMIT);
						}
						if (mNotifiables != null) {
							mNotifiables.updateView(viewObj);
						}
					}
				});
	}

	public void getGoodsList(int offset, int length, String category) {
		DaoManager.getInstance().getSearchDataDao()
				.getGoods(offset, length, category, new ControllerListener() {
					@Override
					public void callback(NetResponse response) {
						// TODO Auto-generated method stub
						ViewUpdateObj viewObj = new ViewUpdateObj();
						viewObj.setCode(response.code);
						if (response.code == 200) {
							viewObj.setData(response.res);
						}
						if (mNotifiables != null) {
							mNotifiables.updateView(viewObj);
						}
					}
				});
	}

	public void getGroupList(int offset, int length, String category) {
		DaoManager
				.getInstance()
				.getSearchDataDao()
				.getGroupShoping(offset, length, category,
						new ControllerListener() {
							@Override
							public void callback(NetResponse response) {
								// TODO Auto-generated method stub
								ViewUpdateObj viewObj = new ViewUpdateObj();
								viewObj.setCode(response.code);
								if (response.code == 200) {
									viewObj.setData(response.res);
								}
								if (mNotifiables != null) {
									mNotifiables.updateView(viewObj);
								}
							}
						});
	}
}
