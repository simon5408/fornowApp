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

import java.util.LinkedList;

import android.util.Log;

import com.fornow.app.datapool.CacheData;
import com.fornow.app.net.ControllerListener;
import com.fornow.app.net.DataCallback;
import com.fornow.app.net.HttpHeader;
import com.fornow.app.net.NetRequest;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.NetworkManager;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class AddressDao {
	public void getShipAddress(String uuid, final ControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/getShipAddress";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		NetRequest netGetReq = NetRequest.createGetRequestWithHeaders(url,
				headers);
		NetworkManager.sendGetReq(netGetReq, new DataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("getShipAddress", "Code:" + netRes.code);
				// netRes.code = 200;
				// netRes.res =
				// "[{'id':'1231','phone':'12312411342','name':'张三','address':'宿舍一区101','area_id':'29873694','postcode':'123456','isdefault':true},{'id':'1221','phone':'12312411342','name':'张三','address':'宿舍一区102','area_id':'29873694','postcode':'123456','isdefault':false}]";
				ctr.callback(netRes);
			}
		});
	}

	/**
	 * 更新收货地址(增加收货地址)
	 * @param uuid
	 * @param address
	 * @param ctr
	 */
	public void updateShipAddress(String uuid, String address,
			final ControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl()
				+ "/updateShipAddress";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(url,
				address.getBytes(), headers);
		NetworkManager.sendPostReq(netPostReq, new DataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("updateShipAddress", "Code:" + netRes.code);
//				netRes.code = 200;
				ctr.callback(netRes);
			}
		});
	}

	public void delShipAddress(String uuid, String addressId,
			final ControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/delShipAddress";
		String requestBody = "{'id': " + addressId + "}";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(url,
				requestBody.getBytes(), headers);
		NetworkManager.sendPostReq(netPostReq, new DataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("delShipAddress", "Code:" + netRes.code);
//				netRes.code = 200;
				ctr.callback(netRes);
			}
		});
	}
}
