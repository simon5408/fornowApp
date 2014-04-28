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
import com.fornow.app.model.LoginData;
import com.fornow.app.model.RegisterData;
import com.fornow.app.net.ControllerListener;
import com.fornow.app.net.DataCallback;
import com.fornow.app.net.HttpHeader;
import com.fornow.app.net.NetRequest;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.NetworkManager;
import com.fornow.app.utils.GsonTool;

/**
 * @name UserDAO
 * @description UserDAO is used for
 * @author Simon Lv
 * @date Aug 9, 2013
 */
public class UserDAO {

	public UserDAO() {

	}

	public void login(LoginData request, final ControllerListener ctr) {
		String url = "";
		url = CacheData.getInstance().getBaseUrl() + "/user/app/login";
		try {
			String requestBody = GsonTool.getGsonTool().toJson(request,
					LoginData.class);
			NetRequest netPostReq = NetRequest.createPostRequest(url,
					requestBody.getBytes());
			NetworkManager.sendPostReq(netPostReq, new DataCallback() {
				@Override
				public void updateData(NetResponse netRes) {
					Log.d("sendLogin", "Code:" + netRes.code);
					// netRes.code = 200;
					 netRes.res =
					 "{'uuid':'4cdbc040657a4847b2667e31d9e2c3d9', 'userinfo':{'user_name':'张三', 'sex':'男','age':24,'is_marital':null,'birthday':null,'constellation':null,'blood_type':null,'phone':'1235345363','qq':null,'email':'12234523452@qq.com','home_town':null,'residence':null,'avatar':null,'interest':null,'college':null,'department':null,'profession':null,'company':null}}";
					ctr.callback(netRes);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void register(String deviceId, RegisterData request,
			final ControllerListener ctr) {
		String url = "";
		url = CacheData.getInstance().getBaseUrl() + "/registerUser";
		try {
			LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
			headers.add(new HttpHeader("uuid", deviceId));
			headers.add(HttpHeader.CONTENT_JSON);
			String requestBody = GsonTool.getGsonTool().toJson(request,
					RegisterData.class);
			NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(
					url, requestBody.getBytes(), headers);
			NetworkManager.sendPostReq(netPostReq, new DataCallback() {
				@Override
				public void updateData(NetResponse netRes) {
					Log.d("updateUser", "Code:" + netRes.code);
					// netRes.code = 200;
					// netRes.res =
					// "{'uuid':'4cdbc040657a4847b2667e31d9e2c3d9', 'userinfo':{'user_name':'张三', 'sex':'男','age':24,'is_marital':null,'birthday':null,'constellation':null,'blood_type':null,'phone':'1235345363','qq':null,'email':'12234523452@qq.com','home_town':null,'residence':null,'avatar':null,'interest':null,'college':null,'department':null,'profession':null,'company':null}}";
					ctr.callback(netRes);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logout(final ControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/logout";
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new DataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("logout", "Code:" + netRes.code);
				ctr.callback(netRes);
			}
		});
	}

	public void updateUser(String uuid, String userInfo,
			final ControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/setUserInfo";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(url,
				userInfo.getBytes(), headers);
		NetworkManager.sendPostReq(netPostReq, new DataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("updateUser", "Code:" + netRes.code);
				// netRes.code = 200;
				// netRes.res =
				// "{'uuid':'4cdbc040657a4847b2667e31d9e2c3d9', 'userinfo':{'user_name':'张三', 'sex':'男','age':24,'is_marital':null,'birthday':null,'constellation':null,'blood_type':null,'phone':'1235345363','qq':null,'email':'12234523452@qq.com','home_town':null,'residence':null,'avatar':null,'interest':null,'college':null,'department':null,'profession':null,'company':null}}";
				ctr.callback(netRes);
			}
		});
	}

	public void getCheckCode(String phone, final ControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl()
				+ "/getCheckCode?phone=" + phone;
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new DataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("getBanner", "Code:" + netRes.code);
				// netRes.code = 200;
				// netRes.res =
				// "[{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200}]";
				ctr.callback(netRes);
			}
		});
	}

	public void sendSuggestion(String uuid, String suggestion,
			final ControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/userFeedback";
		LinkedList<HttpHeader> headers = new LinkedList<HttpHeader>();
		headers.add(new HttpHeader("uuid", uuid));
		headers.add(HttpHeader.CONTENT_JSON);
		String suggest = "{feedback: " + suggestion + "}";
		NetRequest netPostReq = NetRequest.createPostRequestWithHeaders(url,
				suggest.getBytes(), headers);
		NetworkManager.sendPostReq(netPostReq, new DataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				Log.d("userFeedback", "Code:" + netRes.code);
				// netRes.code = 200;
				// netRes.res =
				// "{'uuid':'4cdbc040657a4847b2667e31d9e2c3d9', 'userinfo':{'user_name':'张三', 'sex':'男','age':24,'is_marital':null,'birthday':null,'constellation':null,'blood_type':null,'phone':'1235345363','qq':null,'email':'12234523452@qq.com','home_town':null,'residence':null,'avatar':null,'interest':null,'college':null,'department':null,'profession':null,'company':null}}";
				ctr.callback(netRes);
			}
		});
	}
}
