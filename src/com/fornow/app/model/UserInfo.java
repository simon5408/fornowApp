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
 * @name User
 * @description User is used for
 * @author Simon Lv
 * @date Aug 9, 2013
 */
public class UserInfo extends AbstractModel {
	private static final long serialVersionUID = 388124099941260179L;
	private String user_account;
	private String user_name;
	private String sex;
	private int age;
	private boolean is_marital;
	private String birthday;
	private String constellation;
	private String blood_type;
	private String phone;
	private String qq;
	private String email;
	private String home_town;
	private String residence;
	private ImgData avatar;
	private String interest;
	private String college;
	private String department;
	private String profession;
	private String company;

	public String getUser_account() {
		return user_account;
	}

	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isIs_marital() {
		return is_marital;
	}

	public void setIs_marital(boolean is_marital) {
		this.is_marital = is_marital;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getBlood_type() {
		return blood_type;
	}

	public void setBlood_type(String blood_type) {
		this.blood_type = blood_type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHome_town() {
		return home_town;
	}

	public void setHome_town(String home_town) {
		this.home_town = home_town;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public ImgData getAvatar() {
		return avatar;
	}

	public void setAvatar(ImgData avatar) {
		this.avatar = avatar;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

}
