package com.ice.bike.dao.bo.sys;

import java.util.Date;

public class SysUsr {
	/** 主键. */
	private Integer id;
	/** 电话号码. */
	private String phone;
	/** 账号. */
	private String account;
	/** 用户名. */
	private String usrName;
	/** 密码. */
	private String pwd;
	/** 角色. */
	private Integer role;
	/** 身份证号. */
	private String identity;
	/** 身份证状态. */
	private int identityStatus;
	/** 邮箱. */
	private String email;
	/** 图片路径. */
	private String imgPath;
	/** 创建时间. */
	private Date createTime;
	/** 更新时间. */
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account == null ? null : account.trim();
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName == null ? null : usrName.trim();
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd == null ? null : pwd.trim();
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity == null ? null : identity.trim();
	}

	public int getIdentityStatus() {
		return identityStatus;
	}

	public void setIdentityStatus(int identityStatus) {
		this.identityStatus = identityStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath == null ? null : imgPath.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}