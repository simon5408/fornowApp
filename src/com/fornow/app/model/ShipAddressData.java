/*****************************************************************************
 *
 *                      YIMI PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to YIMI
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from YIMI.
 *
 *            Copyright (c) 2013 by YIMI.  All rights reserved.
 *
 *****************************************************************************/
package com.fornow.app.model;

/**
 * @author Flex.Zang 2013-11-6
 */
public class ShipAddressData extends AbstractModel {
	private static final long serialVersionUID = 1L;
	private String id;
	private String phone;
	private String name;
	private String address;
	private String area_id;
	private String postcode;
	private boolean isdefault;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public boolean isIsdefault() {
		return isdefault;
	}

	public void setIsdefault(boolean isdefault) {
		this.isdefault = isdefault;
	}
}
