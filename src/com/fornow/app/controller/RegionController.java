/**
 * 
 */
package com.fornow.app.controller;

import com.fornow.app.dao.DaoManager;
import com.fornow.app.net.ControllerListener;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;

/**
 * @author Simon Lv
 * 
 */
public class RegionController extends AbstractController<ViewListener, String> {
	public void registerNotification(ViewListener notification) {
		super.register(notification);
	}

	public void unRegisterNotification(ViewListener notification) {
		super.unRegister(notification);
	}

	public void unRegisterAll() {
		super.unRegisterAll();
	}

	public void getRegions() {
		ControllerListener ctr = new ControllerListener() {

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

		};
		DaoManager.getInstance().getRegionDao().getRegions(ctr);
	}

}
