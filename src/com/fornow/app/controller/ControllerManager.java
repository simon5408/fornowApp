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
	private SearchController searchController;
	private ShoppingCartController shopCartController;
	private AddressManageController addressManageController;
	private OrderController orderController;
	private FavoritesController favoritesController;
	private RegionController regionController;

	private ControllerManager() {

	}

	public void init() {
		loginController = new LoginController();
		searchController = new SearchController();
		shopCartController = new ShoppingCartController();
		addressManageController = new AddressManageController();
		orderController = new OrderController();
		favoritesController = new FavoritesController();
		regionController = new RegionController();
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

	public SearchController getSearchController() {
		return searchController;
	}

	public ShoppingCartController getShopCartController() {
		return shopCartController;
	}

	public AddressManageController getAddressManageController() {
		return addressManageController;
	}

	public OrderController getOrderController() {
		return orderController;
	}

	public FavoritesController getFavoritesController() {
		return favoritesController;
	}

	public RegionController getRegionController() {
		return regionController;
	}

}
