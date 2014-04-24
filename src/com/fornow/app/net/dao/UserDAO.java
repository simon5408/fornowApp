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

import com.fornow.app.controller.LoginController.loginType;
import com.fornow.app.datapool.CacheData;
import com.fornow.app.model.LoginData;
import com.fornow.app.net.ControllerListener;
import com.fornow.app.net.DataCallback;
import com.fornow.app.net.HttpHeader;
import com.fornow.app.net.NetRequest;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.NetworkManager;
import com.fornow.app.util.GsonTool;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class UserDAO {

	public UserDAO() {

	}

	public void login(LoginData request, final ControllerListener ctr) {
		sendLoginOrRegister(request, loginType.LOGIN, ctr);
	}

	public void register(LoginData request, final ControllerListener ctr) {
		sendLoginOrRegister(request, loginType.REGISTER, ctr);
	}

	private void sendLoginOrRegister(LoginData request, loginType type,
			final ControllerListener ctr) {
		String url = "";
		switch (type) {
		case LOGIN:
			url = CacheData.getInstance().getBaseUrl() + "/login";
			break;
		case REGISTER:
			url = CacheData.getInstance().getBaseUrl() + "/registerUser";
			break;
		default:
			break;
		}
		try {
			String requestBody = GsonTool.getGsonTool().toJson(request,
					LoginData.class);
			NetRequest netPostReq = NetRequest.createPostRequest(url,
					requestBody.getBytes());
			NetworkManager.sendPostReq(netPostReq, new DataCallback() {
				@Override
				public void updateData(NetResponse netRes) {
					Log.d("sendLoginOrRegister", "Code:" + netRes.code);
//					netRes.code = 200;
//					netRes.res = "{'uuid':'4cdbc040657a4847b2667e31d9e2c3d9', 'userinfo':{'user_name':'张三', 'sex':'男','age':24,'is_marital':null,'birthday':null,'constellation':null,'blood_type':null,'phone':'1235345363','qq':null,'email':'12234523452@qq.com','home_town':null,'residence':null,'avatar':null,'interest':null,'college':null,'department':null,'profession':null,'company':null}}";
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
		Log.d("TAG", "userInfo" + userInfo);
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
}
