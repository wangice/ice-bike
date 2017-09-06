package com.ice.bike.service.sys;

import com.ice.bike.dao.bo.sys.SysUsr;

/**
 * 
 * 创建时间： 2017年7月23日 下午11:24:11
 * 
 * @author ice
 *
 */
public interface SysUsrService {
	/** 保存用户信息. */
	public int saveSysUsr(SysUsr usr);

	public SysUsr querySysUsrInfoById(int id);

	/** 根据电话号码查询用户信息. */
	public SysUsr querySysUsrInfoByPhoner(String phone);

	/** 根据账号查询用户信息. */
	public SysUsr querySysUsrInfoByAccount(String account);

	/** 根据账号或电话号码查询. */
	public SysUsr querySysUsrInfoByPhoneOrAccount(String phone, String account);
	/***********************************/
	/**                                */
	/***********************************/

}
