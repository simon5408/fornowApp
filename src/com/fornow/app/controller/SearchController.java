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

import com.fornow.app.service.IViewListener;

/**
 * @author Simon Lv 2013-8-24
 */
public class SearchController extends AbstractController<IViewListener, String> {

	public void registerNotification(IViewListener notification) {
		super.register(notification);
	}

	public void unRegisterNotification(IViewListener notification) {
		super.unRegister(notification);
	}

	public void unRegisterAll() {
		super.unRegisterAll();
	}

	public void getHomeData() {
//		int offset = 0, length = 10;

//		DaoManager.getInstance().getSearchDataDao()
//				.getBanner(new IControllerListener() {
//					@Override
//					public void callback(NetResponse response) {
//						
//						ViewUpdateObj viewObj = new ViewUpdateObj();
//						viewObj.setCode(response.code);
//						if (response.code == 200) {
//							ClientData.getInstance().setmBanner(response.res);
//							viewObj.setData(response.res);
//							viewObj.setNotifyId(NotifyId.HOME_BANNER);
//						}
//						if (mNotifiables != null) {
//							mNotifiables.updateView(viewObj);
//						}
//					}
//				});

//		DaoManager.getInstance().getSearchDataDao()
//				.getPrivilege(offset, length, new IControllerListener() {
//					@Override
//					public void callback(NetResponse response) {
//						
//						ViewUpdateObj viewObj = new ViewUpdateObj();
//						viewObj.setCode(response.code);
//						if (response.code == 200) {
//							ClientData.getInstance()
//									.setmPrivilege(response.res);
//							viewObj.setData(response.res);
//							viewObj.setNotifyId(NotifyId.HOME_PRIVILEGE);
//						}
//						if (mNotifiables != null) {
//							mNotifiables.updateView(viewObj);
//						}
//					}
//				});

//		DaoManager.getInstance().getSearchDataDao()
//				.getVersion(new IControllerListener() {
//					@Override
//					public void callback(NetResponse response) {
//						
//						ViewUpdateObj viewObj = new ViewUpdateObj();
//						viewObj.setCode(response.code);
//						if (response.code == 200) {
//							viewObj.setData(response.res);
//							try {
//								ImVersion version = GsonTool
//										.getGsonTool()
//										.fromJson(response.res, ImVersion.class);
//								ClientData.getInstance().setVersion(
//										version.getVersion());
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							viewObj.setNotifyId(NotifyId.VERSION);
//						}
//						if (mNotifiables != null) {
//							mNotifiables.updateView(viewObj);
//						}
//					}
//				});

//		DaoManager.getInstance().getSearchDataDao()
//				.getLimitPrice(new IControllerListener() {
//					@Override
//					public void callback(NetResponse response) {
//						
//						ViewUpdateObj viewObj = new ViewUpdateObj();
//						viewObj.setCode(response.code);
//						if (response.code == 200) {
//							viewObj.setData(response.res);
//							try {
//								LimitPrice limitPrice = GsonTool.getGsonTool()
//										.fromJson(response.res,
//												LimitPrice.class);
//								ClientData.getInstance().setMinLimit(
//										limitPrice.getSendPrice() + "");
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							viewObj.setNotifyId(NotifyId.MIN_LIMIT);
//						}
//						if (mNotifiables != null) {
//							mNotifiables.updateView(viewObj);
//						}
//					}
//				});
//	}

//	public void getPrivilege(int offset, int length) {
//		DaoManager.getInstance().getSearchDataDao()
//				.getPrivilege(offset, length, new IControllerListener() {
//					@Override
//					public void callback(NetResponse response) {
//						
//						ViewUpdateObj viewObj = new ViewUpdateObj();
//						viewObj.setCode(response.code);
//						if (response.code == 200) {
//							ClientData.getInstance()
//									.setmPrivilege(response.res);
//							viewObj.setData(response.res);
//							viewObj.setNotifyId(NotifyId.HOME_PRIVILEGE);
//						}
//						if (mNotifiables != null) {
//							mNotifiables.updateView(viewObj);
//						}
//					}
//				});
//	}

//	public void getGoodsList(int offset, int length, String category) {
//		DaoManager.getInstance().getSearchDataDao()
//				.getGoods(offset, length, category, new IControllerListener() {
//					@Override
//					public void callback(NetResponse response) {
//						
//						ViewUpdateObj viewObj = new ViewUpdateObj();
//						viewObj.setCode(response.code);
//						if (response.code == 200) {
//							viewObj.setData(response.res);
//						}
//						if (mNotifiables != null) {
//							mNotifiables.updateView(viewObj);
//						}
//					}
//				});
//	}

//	public void getGroupList(int offset, int length, String category) {
//		DaoManager
//				.getInstance()
//				.getSearchDataDao()
//				.getGroupShoping(offset, length, category,
//						new IControllerListener() {
//							@Override
//							public void callback(NetResponse response) {
//								
//								ViewUpdateObj viewObj = new ViewUpdateObj();
//								viewObj.setCode(response.code);
//								if (response.code == 200) {
//									viewObj.setData(response.res);
//								}
//								if (mNotifiables != null) {
//									mNotifiables.updateView(viewObj);
//								}
//							}
//						});
	}
}
