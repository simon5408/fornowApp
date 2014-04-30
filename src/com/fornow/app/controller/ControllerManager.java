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

/**
 * @author Jiafa Lv
 * @email simon-jiafa@126.com
 * @date Apr 29, 2014 9:40:02 AM
 */
public class ControllerManager {
	private static ControllerManager controllerManager;
	private LoginController loginController;
	private RegionController regionController;
	private AddressManageController addressManageController;
//	private SearchController searchController;

	private ControllerManager() {

	}

	public void init() {
		loginController = new LoginController();
		regionController = new RegionController();
		addressManageController = new AddressManageController();
//		searchController = new SearchController();
	}

	public static ControllerManager getInstance() {
		if (controllerManager == null) {
			synchronized (ControllerManager.class) {
				if (controllerManager == null) {
					controllerManager = new ControllerManager();
				}
			}
		}
		return controllerManager;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public RegionController getRegionController() {
		return regionController;
	}
	
	public AddressManageController getAddressManageController() {
		return addressManageController;
	}
	
//	public SearchController getSearchController() {
//		return searchController;
//	}
}
