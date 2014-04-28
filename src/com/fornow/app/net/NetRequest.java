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

package com.fornow.app.net;

import java.util.LinkedList;

/**
 * 
 * Aug 28, 2013
 * 
 * @author Simon Lv
 */
public class NetRequest {
	
	public String url;
	public byte[] data;
	public LinkedList<HttpHeader> headers ; 
	
	private NetRequest(String url){
		this.url = url;
	}
	
	private NetRequest(String url, byte[] data){
		this(url,data,null);
	}
	
	private NetRequest(String url, LinkedList<HttpHeader> headers) {
		this(url, null, headers);
	}

	private NetRequest(String url, byte[] data, LinkedList<HttpHeader> headers){
		this.url = url;
		this.data = data;
		this.headers = headers;
	}
	
	public static NetRequest createGetRequest(String url){
		return new NetRequest(url);
	}
	
	public static NetRequest createGetRequestWithHeaders(String url,
			LinkedList<HttpHeader> headers) {
		return new NetRequest(url, headers);
	}

	public static NetRequest createPostRequest(String url, byte[] data){
		return new NetRequest(url,data);
	}
	
	public static NetRequest createPostRequestWithHeaders(String url,
			byte[] data, LinkedList<HttpHeader> headers) {
		return new NetRequest(url,data,headers);
	}

}
