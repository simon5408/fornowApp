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
public class OrderDao {
	public void getOrder(String uuid, int offset, int length, String status,
			final ControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/getOrder?offset="
				+ offset + "&length=" + length;
		String postData = "{'status': " + status + "}";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);

		NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(url,
				postData.getBytes(), headers);
		NetworkManager.sendPostReq(netPostReq, new DataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("getOrder", "Code:" + netRes.code);
				// netRes.code = 200;
				// netRes.res =
				// "[{'order_id': '389398047495963','status': 1,'address': '江苏省 南京市 雨花台区','take_out': true,'deal_price': 5.5,'deal_date': '1385561958634','goods_list':[{'goods_id': '123','name': '麻辣豆腐盖浇饭','unit_price': 5.5,'count': 1,'icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}},{'goods_id': '123','name': '麻辣豆腐盖浇饭','unit_price': 5.5,'count': 1,'icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}}]},{'order_id': '389398047495963','status': 2,'address': '江苏省 南京市 雨花台区 软件大道 江苏润和软件外包园 三楼 欧美外包事业部','take_out': true,'deal_price': 5.5,'deal_date': '1385561958634','goods_list':[{'goods_id': '123','name': '麻辣豆腐盖浇饭','unit_price': 5.5,'count': 1,'icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}},{'goods_id': '123','name': '麻辣豆腐盖浇饭','unit_price': 5.5,'count': 1,'icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}}]},{'order_id': '389398047495963','status': 0,'address': '江苏省 南京市 雨花台区 软件大道 江苏润和软件外包园 三楼 欧美外包事业部','take_out': true,'deal_price': 5.5,'deal_date': '1385561958634','goods_list':[{'goods_id': '123','name': '麻辣豆腐盖浇饭','unit_price': 5.5,'count': 1,'icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}},{'goods_id': '123','name': '麻辣豆腐盖浇饭','unit_price': 5.5,'count': 1,'icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}}]},{'order_id': '389398047495963','status': 3,'address': '江苏省 南京市 雨花台区 软件大道 江苏润和软件外包园 三楼 欧美外包事业部','take_out': true,'deal_price': 5.5,'deal_date': '1385561958634','goods_list':[{'goods_id': '123','name': '麻辣豆腐盖浇饭','unit_price': 5.5,'count': 1,'icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}},{'goods_id': '123','name': '麻辣豆腐盖浇饭','unit_price': 5.5,'count': 1,'icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}}]}]";
				ctr.callback(netRes);
			}
		});
	}

	/**
	 * 结算
	 * 
	 * @param uuid
	 * @param requestData
	 * @param ctr
	 */
	public void settlement(String uuid, String requestData,
			final ControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/settlement";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);

		NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(url,
				requestData.getBytes(), headers);
		NetworkManager.sendPostReq(netPostReq, new DataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("getOrder", "Code:" + netRes.code);
				// netRes.code = 200;
				ctr.callback(netRes);
			}
		});
	}
}
