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

import java.util.LinkedList;

import android.util.Log;

import com.fornow.app.datapool.CacheData;
import com.fornow.app.net.HttpHeader;
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
public class FavoritesDao {
	public void getFavorites(String uuid, final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/getFavorites";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		NetRequest netGetReq = NetRequest.createGetRequestWithHeaders(url,
				headers);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("getFavorites", "Code:" + netRes.code);
				// netRes.code = 200;
				// netRes.res =
				// "[{'id': '1','name': '担担面担担面担担面担担面担担面担担面','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '5.5','introduction': '四川担担面四川担担面四川担担面四川担担面四川担担面四川担担面四川担担面','start_time': '1385561958634','end_time': '1385561958634','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200},{'id': '1','name': '担担面','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '5.5','introduction': '四川担担面','start_time': '1385561958634','end_time': '1385561958634','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200},{'id': '1','name': '担担面','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '5.5','introduction': '四川担担面','start_time': '1385561958634','end_time': '1385561958634','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200}]";
				ctr.callback(netRes);
			}
		});
	}

	public void add2Favorites(String uuid, String goodsId,
			final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/add2favorites";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		String requestBody = "{'id': '" + goodsId + "'}";
		NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(url,
				requestBody.getBytes(), headers);
		NetworkManager.sendPostReq(netPostReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("add2Favorites", "Code:" + netRes.code);
				// netRes.code = 200;
				ctr.callback(netRes);
			}
		});
	}

	public void delFromFavorites(String uuid, String goodsId,
			final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/delFromfavorites";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		String requestBody = "{'id': '" + goodsId + "'}";
		NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(url,
				requestBody.getBytes(), headers);
		NetworkManager.sendPostReq(netPostReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("delFromFavorites", "Code:" + netRes.code);
				// netRes.code = 200;
				ctr.callback(netRes);
			}
		});
	}
}
