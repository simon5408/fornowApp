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
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class ControllerManager {
	private static ControllerManager controllerManager;
	private LoginController loginController;
	private SearchController searchController;
	private ShoppingCartController shopCartController;
	private AddressManageController addressManageController;
	private OrderController orderController;
	private FavoritesController favoritesController;

	private ControllerManager() {

	}

	public void init() {
		loginController = new LoginController();
		searchController = new SearchController();
		shopCartController = new ShoppingCartController();
		addressManageController = new AddressManageController();
		orderController = new OrderController();
		favoritesController = new FavoritesController();
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

}
