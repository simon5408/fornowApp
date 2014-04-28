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
import com.fornow.app.model.ConfirmData;
import com.fornow.app.net.IControllerListener;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.IViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.utils.GsonTool;

/**
 * @author Simon Lv 2013-11-21
 */
public class OrderController extends AbstractController<IViewListener, String> {
	public void registerNotification(IViewListener notification) {
		super.register(notification);
	}

	public void unRegisterNotification(IViewListener notification) {
		super.unRegister(notification);
	}

	public void unRegisterAll() {
		super.unRegisterAll();
	}

	public void getOrder(int offset, int length, int stat) {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
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
			String status = (stat==4)? null: stat + "";
			DaoManager.getInstance().getOrderDao().getOrder(uuid, offset, length, status, ctr);
		}
	}

	public void confirmBuy(ConfirmData data) {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
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
			try {
				String requestData = GsonTool.getGsonTool().toJson(data);
				DaoManager.getInstance().getOrderDao()
						.settlement(uuid, requestData, ctr);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
