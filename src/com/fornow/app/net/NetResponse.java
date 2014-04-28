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

/**
 * 
 * Aug 28, 2013
 * 
 * @author Simon Lv
 */
public class NetResponse {

	public int code;
	public String res;

	public NetResponse(int code, String res) {
		this.code = code;
		this.res = res;
	}
}
