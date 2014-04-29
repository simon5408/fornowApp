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
package com.fornow.app.service;

import com.fornow.app.net.NetResponse;

/**
 * @author Simon Lv 2013-8-24
 */
public abstract class IControllerListener {
	abstract public void callback(NetResponse response);
}
