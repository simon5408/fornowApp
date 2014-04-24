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
package com.fornow.app.model;

import java.util.List;


/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class GlobalData extends AbstractModel{

	private static final long serialVersionUID = 1L;
	private static GlobalData g;

	private LocationData location;
	private List<RegionData> regionData;
	private List<ShopData> shopList;

	public static GlobalData getInstance() {
		if (g == null) {
			synchronized (GlobalData.class) {
				if (g == null) {
					g = new GlobalData();
				}
			}
		}
		return g;
	}

	public LocationData getLocation() {
		return location;
	}

	public void setLocation(LocationData location) {
		this.location = location;
	}

	public List<RegionData> getRegionData() {
		return regionData;
	}

	public void setRegionData(List<RegionData> regionData) {
		this.regionData = regionData;
	}

	public List<ShopData> getShopList() {
		return shopList;
	}

	public void setShopList(List<ShopData> shopList) {
		this.shopList = shopList;
	}
	
	public void saveData(){
		//TODO
	}
}
