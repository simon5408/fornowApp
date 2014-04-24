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
package com.fornow.app.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class HttpUtils {
	public static InputStream getStreamFromURL(String imageURL) {

		InputStream in = null;
		try {
			URL url = new URL(imageURL);

			URLConnection openConnection = url.openConnection();

			if (openConnection != null && openConnection.getDate() > 0) {
				HttpURLConnection connection = (HttpURLConnection) openConnection;
				in = connection.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;

	}
}
