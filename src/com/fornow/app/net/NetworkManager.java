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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class NetworkManager {
	private static final int TIMEOUT = 10000;

	/**
	 * Send get request
	 * 
	 * @param netReq
	 *            : NetRequest object
	 * @param callback
	 *            : DataCallback
	 */
	public static void sendGetReq(final NetRequest netReq,
			final DataCallback callback) {

		new Thread() {

			@Override
			public void run() {

				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
				NetResponse netRes = new NetResponse(0, "Network Error");
				try {

					HttpGet get = new HttpGet(netReq.url);
					if (netReq.headers != null)
						for (HttpHeader header : netReq.headers)
							get.addHeader(header.name, header.value);

					HttpResponse response = client.execute(get);
					netRes = parserResponse(response);

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (callback != null && netRes != null) {
						callback.updateData(netRes);
					}
				}

			}
		}.start();
	}

	/**
	 * Send post request.
	 * 
	 * @param netReq
	 *            :NetRequest Object
	 * @param callback
	 *            :DataCallback
	 */
	public static void sendPostReq(final NetRequest netReq,
			final DataCallback callback) {

		new Thread() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
				NetResponse netRes = new NetResponse(0, "Network Error");
				try {
					HttpPost post = new HttpPost(netReq.url);

					if (netReq.headers != null)
						for (HttpHeader header : netReq.headers)
							post.addHeader(header.name, header.value);

					post.setEntity(new StringEntity(new String(netReq.data),
							"UTF-8"));
					HttpResponse response = client.execute(post);
					netRes = parserResponse(response);

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (callback != null) {
						callback.updateData(netRes);
					}
				}
			}
		}.start();
	}

	/**
	 * Parser HTTP response
	 * 
	 * @param res
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private static NetResponse parserResponse(HttpResponse res)
			throws IllegalStateException, IOException {
		NetResponse response = new NetResponse(0, "Network Error");
		StatusLine status = res.getStatusLine();
		if (status != null) {
			response = new NetResponse(status.getStatusCode(),
					streamToString(res.getEntity().getContent()));
		}
		return response;
	}

	/**
	 * Convert Stream Object to String.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String streamToString(InputStream is) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		StringBuffer sb = new StringBuffer();
		int c = 0;
		byte[] buffer = new byte[1024];
		while ((c = bis.read(buffer)) != -1) {
			sb.append(new String(buffer, 0, c));
		}
		return sb.toString();
	}

}
