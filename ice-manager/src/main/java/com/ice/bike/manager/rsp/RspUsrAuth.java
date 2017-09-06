package com.ice.bike.manager.rsp;

public class RspUsrAuth {
	/** 账号. */
	private String account;
	/** 电话号码. */
	private String phone;
	/** 临时密码. */
	private String pwd;
	/** 生成唯一token. */
	private String token;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
