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
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class GoodsListData extends AbstractModel {
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
	private int sell_out;
	private int max_count;

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

}
