package com.ice.bike.api.core;

import java.util.Date;

import com.ice.bike.dao.bo.sys.SysUsr;

/**
 * 用户根
 * 
 * 创建时间： 2017年7月19日 下午11:10:23
 * 
 * @author ice
 *
 */
public class UsrStub {
	/** 用户id. */
	public int uid;
	/** 电话号码. */
	public String phone;
	/** 用户名. */
	public String usr;
	/** 密码. */
	public String pwd;
	/** 生成token的时间. */
	public Date gts;
	/** 角色类型. */
	public int role;

	public UsrStub() {
	}

	public UsrStub(SysUsr user) {
		this.uid = user.getId();
		this.phone = user.getPhone();
		this.pwd = user.getPwd();
		this.usr = user.getUsrName();
		this.role = user.getRole();
		this.gts = new Date();/* 当前时间. */
	}
}
