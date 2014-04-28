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
package com.fornow.app.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @author Simon Lv 2013-8-4
 */
public class MD5Utils {
	private static MD5Utils instance = null;
	private static MessageDigest md5 = null;

	private MD5Utils() {
	};

	public static MD5Utils getInstance() {
		try {
			instance = new MD5Utils();
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			return null;
		}
		return instance;
	}

	public String getStringHash(String source) {
		String hash = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(source
					.getBytes());
			hash = getStreamHash(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hash;
	}

	public String getFileHash(String file) {
		String hash = null;
		try {
			FileInputStream in = new FileInputStream(file);
			hash = getStreamHash(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hash;
	}

	public String getStreamHash(InputStream stream) {
		String hash = null;
		byte[] buffer = new byte[1024];
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(stream);
			int numRead = 0;
			while ((numRead = in.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			in.close();
			hash = toHexString(md5.digest());
		} catch (Exception e) {
			if (in != null)
				try {
					in.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			e.printStackTrace();
		}
		return hash;
	}

	private String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	private char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f' };

}
