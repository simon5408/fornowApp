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
 * @author Simon Lv 2013-8-16
 */
public class ControllerManager {
	private static ControllerManager controllerManager;
	private LoginController loginController;
	private RegionController regionController;
//	private SearchController searchController;

	private ControllerManager() {

	}

	public void init() {
		loginController = new LoginController();
		regionController = new RegionController();
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
	
//	public SearchController getSearchController() {
//		return searchController;
//	}
}
