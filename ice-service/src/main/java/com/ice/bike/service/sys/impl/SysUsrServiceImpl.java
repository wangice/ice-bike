package com.ice.bike.service.sys.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ice.bike.dao.bo.sys.SysUsr;
import com.ice.bike.dao.mapper.sys.SysUsrMapper;
import com.ice.bike.dao.mapper.sys.ext.SysUsrExtMapper;
import com.ice.bike.service.sys.SysUsrService;

/**
 * 
 * 创建时间： 2017年7月23日 下午11:24:21
 * 
 * @author ice
 *
 */
@Service("sysUsrService")
public class SysUsrServiceImpl implements SysUsrService {

	@Autowired
	public SysUsrMapper sysUsrMapper;

	@Autowired
	public SysUsrExtMapper sysUsrExtMapper;

	@Override
	public int saveSysUsr(SysUsr usr) {
		return sysUsrMapper.insertSelective(usr);
	}

	@Override
	public SysUsr querySysUsrInfoByPhoner(String phone) {
		return sysUsrExtMapper.selectSysUsrByPhone(phone);
	}

	@Override
	public SysUsr querySysUsrInfoByAccount(String account) {
		return sysUsrExtMapper.selectSysUsrByAccount(account);
	}

	@Override
	public SysUsr querySysUsrInfoById(int id) {
		return sysUsrMapper.selectByPrimaryKey(id);
	}

	@Override
	public SysUsr querySysUsrInfoByPhoneOrAccount(String phone, String account) {
		return sysUsrExtMapper.selectSysUsrByPhoneOrAccount(phone, account);
	}
}
