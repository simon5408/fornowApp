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
 * @author Simon Lv 2013-9-24
 */
public class GoodsDetailData extends AbstractModel {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String category;
	private ImgData icon;
	private ImgData[] image;
	private Double original_price;
	private Double current_price;
	private String introduction;
	private DeliverAreaData[] deliver_area;
	private String start_time;
	private String end_time;
	private int sell_out;
	private int max_count;
	private int select_count = 1;
	private boolean is_sell;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ImgData getIcon() {
		return icon;
	}

	public void setIcon(ImgData icon) {
		this.icon = icon;
	}

	public ImgData[] getImage() {
		return image;
	}

	public void setImage(ImgData[] image) {
		this.image = image;
	}

	public Double getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(Double original_price) {
		this.original_price = original_price;
	}

	public Double getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(Double current_price) {
		this.current_price = current_price;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public DeliverAreaData[] getDeliver_area() {
		return deliver_area;
	}

	public void setDeliver_area(DeliverAreaData[] deliver_area) {
		this.deliver_area = deliver_area;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getSell_out() {
		return sell_out;
	}

	public void setSell_out(int sell_out) {
		this.sell_out = sell_out;
	}

	public int getMax_count() {
		return max_count;
	}

	public void setMax_count(int max_count) {
		this.max_count = max_count;
	}

	public int getSelect_count() {
		return select_count;
	}

	public void setSelect_count(int select_count) {
		this.select_count = select_count;
	}

	public boolean isIs_sell() {
		return is_sell;
	}

	public void setIs_sell(boolean is_sell) {
		this.is_sell = is_sell;
	}
}
