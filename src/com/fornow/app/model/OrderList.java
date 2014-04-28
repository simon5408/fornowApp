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

/**
 * @author Simon Lv 2013-11-21
 */
public class OrderList extends AbstractModel {
	private static final long serialVersionUID = 1L;
	private String order_id;
	private int status;
	private String address;
	private boolean take_out;
	private Double deal_price;
	private String deal_date;
	private OrderGoodsList[] goods_list;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isTake_out() {
		return take_out;
	}

	public void setTake_out(boolean take_out) {
		this.take_out = take_out;
	}

	public Double getDeal_price() {
		return deal_price;
	}

	public void setDeal_price(Double deal_price) {
		this.deal_price = deal_price;
	}

	public String getDeal_date() {
		return deal_date;
	}

	public void setDeal_date(String deal_date) {
		this.deal_date = deal_date;
	}

	public OrderGoodsList[] getGoods_list() {
		return goods_list;
	}

	public void setGoods_list(OrderGoodsList[] goods_list) {
		this.goods_list = goods_list;
	}

}
