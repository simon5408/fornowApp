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

import java.io.Serializable;

/**
 * @name ClientData
 * @description ClientData is used for
 * @author Simon Lv
 * @date Aug 9, 2013
 */
public class ClientData implements Serializable {

	private static final long serialVersionUID = -5701224101571896291L;
	@SuppressWarnings("unused")
	private static final String filePath = "";

	private static ClientData clientData = null;
	private String version = null;
	private String minLimit = null;
	private String mUUID = null;
	private String mCart = null;
	private String mBanner = null;
	private String mPrivilege = null;
	private String user = null;
	private String mShdz = null;
	private String mFavorites = null;

	private ClientData() {

	}

	public static ClientData getInstance() {
		if (clientData == null) {
			synchronized (ClientData.class) {
				if (clientData == null) {
					// try {
					// InputStream is = new FileInputStream(filePath);
					// ObjectInputStream ois = new ObjectInputStream(is);
					// clientData = (ClientData) ois.readObject();
					// ois.close();
					// } catch (Exception e) {
					// }
					if (clientData == null) {
						clientData = new ClientData();
					}
				}
			}
		}
		return clientData;
	}

	public void recycle() {
		clientData = null;
	}

	// public void persist() throws IOException {
	// File f = new File(filePath);
	// if (f.exists()) {
	// f.delete();
	// }
	// FileOutputStream os = new FileOutputStream(f);
	// ObjectOutputStream oos = new ObjectOutputStream(os);
	// oos.writeObject(this);
	// oos.close();
	// os.close();
	// }

	public String getmUUID() {
		return mUUID;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(String minLimit) {
		this.minLimit = minLimit;
	}

	public void setmUUID(String mUUID) {
		this.mUUID = mUUID;
	}

	public String getmCart() {
		return mCart;
	}

	public void setmCart(String mCart) {
		this.mCart = mCart;
	}

	public String getmBanner() {
		return mBanner;
	}

	public void setmBanner(String mBanner) {
		this.mBanner = mBanner;
	}

	public String getmPrivilege() {
		return mPrivilege;
	}

	public void setmPrivilege(String mPrivilege) {
		this.mPrivilege = mPrivilege;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getmShdz() {
		return mShdz;
	}

	public void setmShdz(String mShdz) {
		this.mShdz = mShdz;
	}

	public String getmFavorites() {
		return mFavorites;
	}

	public void setmFavorites(String mFavorites) {
		this.mFavorites = mFavorites;
	}
}
