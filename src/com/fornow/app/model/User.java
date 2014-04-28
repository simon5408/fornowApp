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
package com.fornow.app.model;

/**
 * @author Simon Lv 2013-10-22
 */
public class User extends AbstractModel {
	private static final long serialVersionUID = 1L;
	private String uuid;
	private UserInfo userinfo;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public UserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}

}
