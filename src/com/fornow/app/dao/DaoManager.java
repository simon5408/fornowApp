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
package com.fornow.app.dao;

/**
 * @author Simon Lv 2013-8-18
 */
public class DaoManager {
	private static DaoManager daoManager;
	private UserDAO userDao;
	private RegionDao regionDao;
//	private SearchDataDao searchDataDao;

	private DaoManager() {

	}

	public void init() {
		userDao = new UserDAO();
		regionDao = new RegionDao();
//		searchDataDao = new SearchDataDao();
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
	
//	public SearchDataDao getSearchDataDao() {
//		return searchDataDao;
//	}
}
