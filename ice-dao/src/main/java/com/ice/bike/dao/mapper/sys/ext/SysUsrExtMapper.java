package com.ice.bike.dao.mapper.sys.ext;

import org.apache.ibatis.annotations.Param;

import com.ice.bike.dao.bo.sys.SysUsr;

public interface SysUsrExtMapper {
	/** 根据电话号码查询. */
	public SysUsr selectSysUsrByPhone(String phone);

	/** 根据账号查询. */
	public SysUsr selectSysUsrByAccount(String account);

	/** 根据电话和账号查询. */
	public SysUsr selectSysUsrByPhoneOrAccount(@Param("phone") String phone, @Param("account") String account);
}
