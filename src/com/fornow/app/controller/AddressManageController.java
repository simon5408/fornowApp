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
import com.fornow.app.model.ShipAddressData;
import com.fornow.app.net.ControllerListener;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.net.dao.DaoManager;
import com.fornow.app.util.GsonTool;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class AddressManageController extends
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

	public void getShippingAddress() {
		// if (ClientData.getInstance().getmShdz() != null) {
		// ViewUpdateObj viewObj = new ViewUpdateObj();
		// viewObj.setData(ClientData.getInstance().getmShdz());
		// mNotifiables.updateView(viewObj);
		// } else {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			DaoManager.getInstance().getAddressDao()
					.getShipAddress(uuid, new ControllerListener() {

						@Override
						public void callback(NetResponse response) {
							ViewUpdateObj viewObj = new ViewUpdateObj();
							viewObj.setCode(response.code);
							if (response.code == 200) {
								// ClientData.getInstance().setmShdz(
								// response.res);
								viewObj.setData(response.res);
							}
							if (mNotifiables != null) {
								mNotifiables.updateView(viewObj);
							}
						}
					});
		}
		// }

	}

	/**
	 * 更新收货地址(增加收货地址)
	 * @param uuid
	 * @param address
	 * @param ctr
	 */
	public void updateShippingAddress(ShipAddressData data) {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			try {
				String address = GsonTool.toJson(data);
				DaoManager
						.getInstance()
						.getAddressDao()
						.updateShipAddress(uuid, address,
								new ControllerListener() {

									@Override
									public void callback(NetResponse response) {
										ViewUpdateObj viewObj = new ViewUpdateObj();
										viewObj.setCode(response.code);
										if (mNotifiables != null) {
											mNotifiables.updateView(viewObj);
										}
									}
								});
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void deleteShipingAddress(String addressId) {
		String uuid = ClientData.getInstance().getmUUID();
		if (uuid != null) {
			DaoManager.getInstance().getAddressDao()
					.delShipAddress(uuid, addressId, new ControllerListener() {

						@Override
						public void callback(NetResponse response) {
							ViewUpdateObj viewObj = new ViewUpdateObj();
							viewObj.setCode(response.code);
							if (mNotifiables != null) {
								mNotifiables.updateView(viewObj);
							}
						}
					});
		}
	}
}
