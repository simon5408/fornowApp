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

import com.fornow.app.net.NetRequest;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.NetworkManager;
import com.fornow.app.service.IControllerListener;
import com.fornow.app.service.IDataCallback;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class RegionDao {

	public RegionDao() {

	}

	public void getData(String request, final IControllerListener ctr) {
		NetRequest netGetReq = NetRequest
				.createGetRequest("http://httpbin.org/ip");
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("RegionDao getData", "Code:" + netRes.code);
				Log.d("RegionDao getData", "Response:" + netRes.res);
				netRes.res = "[{'id':'123','region_name':'南京信息工程大学','is_default':true,'coords':{'longitude': 104062157,'latitude': 30658255}},{'id': '124','region_name': '南京工业大学','default':false,'coords':{'longitude': 104062157,'latitude': 30658255}}]";
				ctr.callback(null);
			}
		});
	}
}
