/**
 * 
 */
package com.fornow.app.controller;

import com.fornow.app.dao.DaoManager;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IControllerListener;
import com.fornow.app.service.IViewListener;

/**
 * @author Simon Lv
 * 
 */
public class RegionController extends AbstractController<IViewListener, String> {
	public void registerNotification(IViewListener notification) {
		super.register(notification);
	}

	public void unRegisterNotification(IViewListener notification) {
		super.unRegister(notification);
	}

	public void unRegisterAll() {
		super.unRegisterAll();
	}

	public void getRegions() {
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
		DaoManager.getInstance().getRegionDao().getRegions(ctr);
	}

}
