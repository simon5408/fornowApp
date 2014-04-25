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

import com.fornow.app.datapool.CacheData;
import com.fornow.app.net.NetRequest;
import com.fornow.app.net.NetResponse;
import com.fornow.app.net.NetworkManager;
import com.fornow.app.service.IControllerListener;
import com.fornow.app.service.IDataCallback;
import com.fornow.app.util.LogUtils;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class SearchDataDao {
	private static final String TAG = SearchDataDao.class.getName();
	public SearchDataDao() {

	}

	public void getBanner(final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/getBanner";
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				LogUtils.d(TAG, "[getBanner] Code:" + netRes.code);
				//TODO
				 netRes.code = 200;
				 netRes.res =
				 "[{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200}]";
				ctr.callback(netRes);
			}
		});
	}

	public void getVersion(final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/getVersion";
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				LogUtils.d(TAG, "[getVersion] Code:" + netRes.code);
				LogUtils.d(TAG, "[getVersion] res:" + netRes.res);
				//TODO
				 netRes.code = 200;
				 netRes.res = "{'url':'http://downloadurl','version':'0.1'}";
				ctr.callback(netRes);
			}
		});
	}

	public void getLimitPrice(final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/getSendPrice";
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				LogUtils.d(TAG, "[getLimitPrice] Code:" + netRes.code);
				LogUtils.d(TAG, "[getLimitPrice] res:" + netRes.res);
				//TODO
				 netRes.code = 200;
				 netRes.res = "{'sendPrice':15}";
				ctr.callback(netRes);
			}
		});
	}

	public void getDataByKey(String request, final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl() + "/getGoodsList";
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				LogUtils.d(TAG, "[getDataByKey] Code:" + netRes.code);
				ctr.callback(netRes);
			}
		});
	}

	public void getGoods(int offset, int length, String type,
			final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl()
				+ "/getGoodsList?category=" + type + "&offset=" + offset
				+ "&length=" + length;
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				LogUtils.d(TAG, "[getGoods] Code:" + netRes.code);
				//TODO
				 netRes.code = 200;
				 netRes.res =
				 "[{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200}]";
				ctr.callback(netRes);
			}
		});
	}

	public void getGroupShoping(int offset, int length, String category,
			final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl()
				+ "/getGroupBuyingList?category=" + category + "&offset="
				+ offset + "&length=" + length;
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				LogUtils.d(TAG, "[getGroupShoping] Code:" + netRes.code);
				//TODO
				 netRes.code = 200;
				 netRes.res =
				 "[{'id': '1','name': '担担面1','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '5.5','introduction': '四川担担面','start_time': '1385561958634','end_time': '1385561958634','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200},{'id': '1','name': '担担面2','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '6.5','introduction': '四川担担面','start_time': '1385561958634','end_time': '1385561958633','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200},{'id': '1','name': '担担面3','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '7.5','introduction': '四川担担面','start_time': '1385561958634','end_time': '1385561958636','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200},{'id': '1','name': '担担面4','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '4.5','introduction': '四川担担面','start_time': '1385561958634','end_time': '1385561958639','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200},{'id': '1','name': '担担面5','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '1.5','introduction': '四川担担面','start_time': '1385561958634','end_time': '1385561958638','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200},{'id': '1','name': '担担面6','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '3.5','introduction': '四川担担面','start_time': '1385561958634','end_time': '1385561958637','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200},{'id': '1','name': '担担面7','category': 'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image': [{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price': '6.0','current_price': '2.5','introduction': '四川担担面','start_time': '1385561958634','end_time': '1385561958635','deliver_area': [{'area_id': '123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}],'max_count': 200}]";
				ctr.callback(netRes);
			}
		});
	}

	public void getPrivilege(int offset, int length, String type,
			final IControllerListener ctr) {
		String url = CacheData.getInstance().getBaseUrl()
				+ "/getBargainPriceList";
		NetRequest netGetReq = NetRequest.createGetRequest(url);
		NetworkManager.sendGetReq(netGetReq, new IDataCallback() {
			@Override
			public void updateData(NetResponse netRes) {
				LogUtils.d(TAG, "[getPrivilege] Code:" + netRes.code);
				//TODO
				 netRes.code = 200;
				 netRes.res =
				 "[{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200},{'id': '1','name': '担担面','category':'wheaten','icon':{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},'image':[{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'},{'id':'123','url':'http://diancanwang.vicp.cc/images/2.png'}],'original_price':'6.0','current_price':'5.5','introduction': '四川担担面','deliver_area':[{'area_id':'123','area_name': '宿舍一区'},{'area_id': '124','area_name': '宿舍二区'}], 'sell_out': 50,'max_cout': 200}]";
				ctr.callback(netRes);
			}
		});
	}
}
