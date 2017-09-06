package com.ice.bike.api.controller;

import com.ice.bike.api.core.Distrans;
import com.ice.bike.api.core.Rsp.RspErr;
import com.ice.bike.api.redis.DbRedis;
import com.ice.bike.api.rsp.RspVerifCode;
import com.ice.bike.common.constant.CommonConstants;
import com.ice.bike.common.util.StringHelper;
import com.ice.bike.common.util.VerifUtils;

/**
 * 
 * 创建时间： 2017年7月17日 下午11:37:44
 * 
 * @author ice
 *
 */
public class SMSController {

	/** 请求获取验证码. */
	public static final void requestMsgCode(Distrans trans, String sign, String salt, DbRedis redis) throws Exception {
		String phone = trans.getParStr("phone");
		if (!VerifUtils.verifPhone(phone)) {
			trans.end(RspErr.ERR_FORMAT_ERROR);
			return;
		}
		String random = StringHelper.getRandomString(6);
		// TODO: 需要短信验证请求，请求成功后发送成功信息
		// 缓存到redis中
		redis.setVerifCode(phone, random, CommonConstants.VERIF_EXPIRE_TIME);/* 保存redis中. */
		//
		RspVerifCode rsp = new RspVerifCode();
		rsp.phone = phone;
		rsp.verifCode = random;
		rsp.time = CommonConstants.VERIF_EXPIRE_TIME;
		trans.end(rsp);
	}
}
