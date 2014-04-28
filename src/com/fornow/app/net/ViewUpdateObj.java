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
package com.fornow.app.net;

import com.fornow.app.ui.NotifyId;

/**
 * @author Simon Lv 2013-11-25
 */
public class ViewUpdateObj {
	private int code = 200;
	private String data;
	private NotifyId notifyId;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public NotifyId getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(NotifyId notifyId) {
		this.notifyId = notifyId;
	}

}
