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
public class CartDao {

	public void getCart(String uuid, final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/getMyCart";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		NetRequest netGetReq = NetRequest.createGetRequestWithHeaders(url,
				headers);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("getCart", "Code:" + netRes.code);
//				netRes.code = 200;
//				netRes.res = "[{'goods_id': '1234','name': '麻辣豆腐盖浇饭','category': 'wheaten','icon':{'id':'1235','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id': '1236','url': 'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area': [{'area_id': '1237','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'count': 1},{'goods_id': '1238','name': '麻辣豆腐盖浇饭','category': 'wheaten','icon':{'id':'1239','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id': '12311','url': 'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area': [{'area_id': '12312','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'count': 1},{'goods_id': '12313','name': '麻辣豆腐盖浇饭','category': 'wheaten','icon':{'id':'12314','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id': '12315','url': 'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area': [{'area_id': '12316','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'count': 1},{'goods_id': '12317','name': '麻辣豆腐盖浇饭','category': 'wheaten','icon':{'id':'12318','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id': '12319','url': 'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area': [{'area_id': '12320','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'count': 1},{'goods_id': '12321','name': '麻辣豆腐盖浇饭','category': 'wheaten','icon':{'id':'12322','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id': '12323','url': 'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area': [{'area_id': '12324','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'count': 1},{'goods_id': '12325','name': '麻辣豆腐盖浇饭','category': 'wheaten','icon':{'id':'12326','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id': '12327','url': 'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area': [{'area_id': '12328','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'count': 1}]";
				ctr.callback(netRes);
			}
		});
	}

	public void updateCart(String uuid, String cartData) {
		String url = CacheData.getInstance().getBaseUrl() + "/updateCart";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(url,
				cartData.getBytes(), headers);
		NetworkManager.sendPostReq(netPostReq, null);
	}
}
