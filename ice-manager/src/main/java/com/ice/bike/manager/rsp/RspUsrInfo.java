package com.ice.bike.manager.rsp;

import com.ice.bike.dao.bo.sys.SysUsr;

/**
 * 
 * 创建时间： 2017年7月30日 下午3:24:00
 * 
 * @author ice
 *
 */
public class RspUsrInfo {
	/** 主键. */
	private Integer id;
	/** 电话号码. */
	private String phone;
	/** 账号. */
	private String account;
	/** 用户名. */
	private String usrName;
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

	/** 数据转化.将SysUsr转为RspUsrInfo. */
	public RspUsrInfo fill(SysUsr usr) {
		this.id = usr.getId();
		this.phone = usr.getPhone();
		this.account = usr.getAccount();
		this.usrName = usr.getUsrName();
		this.role = usr.getRole();
		this.identity = usr.getIdentity();
		this.identityStatus = usr.getIdentityStatus();
		this.email = usr.getEmail();
		this.imgPath = usr.getImgPath();
		return this;
	}

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
		this.phone = phone;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
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
		this.identity = identity;
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
		this.email = email;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
}
