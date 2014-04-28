/**
 * 
 */
package com.fornow.app.model;

/**
 * @author Simon Lv
 * 
 */
public class RegisterData extends AbstractModel {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String checkCode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

}
