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

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class HttpHeader {

	public static final String NAME_CONTENT_TYPE = "Content-Type";

	public static final String VAL_TEXT = "text/plain";
	public static final String VAL_JSON = "application/json";
	public static final String VAL_BIN = "application/octet-stream";

	public static final HttpHeader CONTENT_JSON = new HttpHeader(
			NAME_CONTENT_TYPE, VAL_JSON);
	public static final HttpHeader CONTENT_BIN = new HttpHeader(
			NAME_CONTENT_TYPE, VAL_BIN);
	public static final HttpHeader CONTENT_TEXT = new HttpHeader(
			NAME_CONTENT_TYPE, VAL_TEXT);

	public String name;
	public String value;

	public HttpHeader(String headerName, String headerValue) {
		this.name = "Cookie";
		this.value = "JSESSIONID=" + headerValue;
	}
}
