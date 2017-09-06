package com.ice.bike.api.controller;

import javax.annotation.Resource;

import com.ice.bike.api.core.Distrans;
import com.ice.bike.api.core.Rsp.RspErr;
import com.ice.bike.api.core.UsrStub;
import com.ice.bike.api.redis.DbRedis;
import com.ice.bike.api.rsp.RspLoginInfo;
import com.ice.bike.service.sys.SysUsrService;

/**
 * 
 * 创建时间： 2017年7月21日 下午11:21:39
 * 
 * @author ice
 *
 */
public class UserController {
	/** redis存储用户信息前缀. */
	public static final String USR_INFO = "USR:";

	@Resource
	public SysUsrService usrService;

	/** 用户登录. */
	public static final void usrLogin(Distrans trans, String sign, String salt, DbRedis redis) throws Exception {
		// 1.获取参数
		String phone = trans.getParStr("phone");
		String verifCode = trans.getParStr("verifCode");/* 验证码. */
		if (phone == null || verifCode == null) {
			trans.end(RspErr.ERR_FORMAT_ERROR);
			return;
		}
		// 2.比较验证码是否正确
		String code = redis.getVerifCode(phone);
		if (code == null) {
			trans.end(RspErr.VERIF_CODE_LOSE_EFFECT);
			return;
		}
		if (!code.equals(verifCode)) {
			trans.end(RspErr.VERIF_CODE_ERROR);
			return;
		}
		// 先从缓存中读取该用户，如果存在，返回数据
		UsrStub usrStub = redis.getToken(USR_INFO + phone);
		if (usrStub != null) {
			UserController.rspLogin(trans, usrStub);
			return;
		}
		// 如果不存在，读取数据库数据，如果存在，缓存设置值，返回数据
		
		// 数据库数据不存在，创建该用户，设置缓存，返回数据
	}

	/** 登录响应. */
	private static final void rspLogin(Distrans trans, UsrStub usrStub) {
		RspLoginInfo rsp = new RspLoginInfo();
		rsp.phone = usrStub.phone;
		rsp.usr = usrStub.usr;
		rsp.role = usrStub.role;
		rsp.token = DbRedis.makeToken();
		trans.end(rsp);
	}
}
