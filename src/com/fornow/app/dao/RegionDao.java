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

import android.util.Log;

import com.fornow.app.datapool.CacheData;
import com.fornow.app.net.NetRequest;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.NetworkManager;
import com.fornow.app.service.IControllerListener;
import com.fornow.app.service.IDataCallback;

/**
 * @author Simon Lv 2013-8-24
 */
public class RegionDao {

	public RegionDao() {

	}

	public void getRegions(final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/getRegions";
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("getRegions", "Code:" + netRes.code);
				 netRes.code = 200;
				 netRes.res = "[{'id':1,'name':'江苏','sons':[{'id':2,'name':'南京','sons':[{'id':2,'name':'鼓楼区', 'sons':[{'id':2,'name':'凤翔新城', 'sons':[]}]},{'id':1,'name':'江宁区', 'sons':[]}]},{'id':1,'name':'苏州', 'sons':[]}]},{'id':1,'name':'江苏','sons':[{'id':2,'name':'南京','sons':[{'id':2,'name':'鼓楼区', 'sons':[{'id':2,'name':'凤翔新城', 'sons':[]}]},{'id':1,'name':'江宁区', 'sons':[]}]},{'id':1,'name':'苏州', 'sons':[]}]}]";
				ctr.callback(netRes);
			}
		});
	}
}
