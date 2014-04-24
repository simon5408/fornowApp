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
package com.fornow.app.net.dao;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class DaoManager {
	private static DaoManager daoManager;
	private UserDAO userDao;
	private RegionDao regionDao;
	private SearchDataDao searchDataDao;
	private CartDao cartDao;
	private AddressDao addressDao;
	private OrderDao orderDao;
	private FavoritesDao favoritesDao;

	private DaoManager() {

	}

	public void init() {
		userDao = new UserDAO();
		regionDao = new RegionDao();
		searchDataDao = new SearchDataDao();
		cartDao = new CartDao();
		addressDao = new AddressDao();
		orderDao = new OrderDao();
		favoritesDao = new FavoritesDao();
	}

	public static DaoManager getInstance() {
		if (null == daoManager) {
			synchronized (DaoManager.class) {
				if (null == daoManager) {
					daoManager = new DaoManager();
				}
			}
		}
		return daoManager;
	}

	public UserDAO getUserDao() {
		return userDao;
	}

	public RegionDao getRegionDao() {
		return regionDao;
	}

	public SearchDataDao getSearchDataDao() {
		return searchDataDao;
	}

	public CartDao getCartDao() {
		return cartDao;
	}

	public AddressDao getAddressDao() {
		return addressDao;
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public FavoritesDao getFavoritesDao() {
		return favoritesDao;
	}

}
