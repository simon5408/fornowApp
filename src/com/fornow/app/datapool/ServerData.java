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
package com.fornow.app.datapool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.fornow.app.model.RegionData;
import com.fornow.app.model.UserInfo;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class ServerData implements Serializable {

	private static final long serialVersionUID = 1056766234482500184L;
	private static final String filePath = "";

	private UserInfo user;
	private RegionData region;

	private static ServerData serverData = null;

	public static ServerData getInstance() {
		if (serverData == null) {
			synchronized (serverData) {
				if (serverData == null) {
					try {
						InputStream is = new FileInputStream(filePath);
						ObjectInputStream ois = new ObjectInputStream(is);
						serverData = (ServerData) ois.readObject();
						ois.close();
					} catch (Exception e) {
					}
					if (serverData == null) {
						serverData = new ServerData();
					}
				}
			}
		}
		return serverData;
	}

	public void persist() throws IOException {
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		FileOutputStream os = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(this);
		oos.close();
		os.close();
	}

	private ServerData() {

	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public RegionData getRegion() {
		return region;
	}

	public void setRegion(RegionData region) {
		this.region = region;
	}

}
