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
 * @author Simon Lv 2013-12-1
 */
public class ConfirmData extends AbstractModel {
	private static final long serialVersionUID = 1L;
	private String ship_address_id;
	private boolean take_out;
	private String order_info;
	private List<SettlementGoods> goods_list;

	public String getShip_address_id() {
		return ship_address_id;
	}

	public void setShip_address_id(String ship_address_id) {
		this.ship_address_id = ship_address_id;
	}

	public boolean isTake_out() {
		return take_out;
	}

	public void setTake_out(boolean take_out) {
		this.take_out = take_out;
	}

	public String getOrder_info() {
		return order_info;
	}

	public void setOrder_info(String order_info) {
		this.order_info = order_info;
	}

	public List<SettlementGoods> getGoods_list() {
		return goods_list;
	}

	public void setGoods_list(List<SettlementGoods> goods_list) {
		this.goods_list = goods_list;
	}

}
