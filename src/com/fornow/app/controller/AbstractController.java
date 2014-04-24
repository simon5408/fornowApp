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
public abstract class AbstractController<T, O> {
	protected T mNotifiables;

	protected void register(T notification) {
		if (notification != null) {
			synchronized (AbstractController.class) {
				if (mNotifiables == notification) {
					return;
				} else if (null == mNotifiables) {
					mNotifiables = notification;
				}
			}
		}
	}

	protected void unRegister(T notification) {
		if (notification != null) {
			synchronized (AbstractController.class) {
				if (mNotifiables == notification) {
					mNotifiables = null;
					return;
				}
			}
		}
	}

	protected void unRegisterAll() {
		if (mNotifiables != null) {
			synchronized (AbstractController.class) {
				if (mNotifiables != null) {
					mNotifiables = null;
					return;
				}
			}
		}
	}
}
