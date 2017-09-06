package com.ice.bike.manager.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ice.bike.dao.bo.sys.SysUsr;
import com.ice.bike.manager.controller.base.BaseController;
import com.ice.bike.manager.core.HttpReq;
import com.ice.bike.manager.core.Rsp.RspErr;
import com.ice.bike.manager.core.UsrStub;
import com.ice.bike.manager.redis.DbRedis;
import com.ice.bike.manager.rsp.RspUsrAuth;
import com.ice.bike.service.sys.SysUsrService;

import misc.Crypto;
import misc.Log;
import misc.Misc;
import misc.Net;

@Controller
@RequestMapping("/usr")
public class UsrLogin extends BaseController {

	@Resource
	public SysUsrService usrService;

	@Resource
	public DbRedis dbRedis;

	/** 登录. */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) throws Exception {
		HttpReq req = new HttpReq(request);
		String salt = req.getParStr("salt");
		String sign = req.getParStr("sign");
		String usr = req.getParStr("usr");
		String pwd = req.getParStr("pwd");
		if (usr == null || pwd == null) {
			return trans(RspErr.ERR_FORMAT_ERROR);
		}
		SysUsr sysUsr = usrService.querySysUsrInfoByAccount(usr);
		if (sysUsr == null) {
			return trans(RspErr.ERR_USER_UNEXISTED);
		}
		if (!sysUsr.getPwd().equals(pwd)) {
			return trans(RspErr.ERR_USER_PWD_ERROR);
		}
		if (!(Crypto.sha1StrLowerCase((salt + sysUsr.getPwd() + req.getAction()).getBytes()).equals(sign))) {
			return trans(RspErr.Err_SIGN);/* 这里和上一步有重复. */
		}
		String token = DbRedis.makeToken();
		String secret = Crypto.sha1StrLowerCase(Misc.gen0aAkey128().getBytes());
		UsrStub stub = new UsrStub(sysUsr.getPhone(), sysUsr.getAccount(), secret);
		dbRedis.setToken(token, stub);
		//
		RspUsrAuth usrAuth = new RspUsrAuth();
		usrAuth.setPhone(sysUsr.getPhone());
		usrAuth.setToken(token);
		usrAuth.setAccount(usr);
		usrAuth.setPwd(pwd);
		return trans(usrAuth);
	}

	/** 注册. */
	@ResponseBody
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String regist(HttpServletRequest request) throws Exception {
		HttpReq req = new HttpReq(request);
		String salt = req.getParStr("salt");
		String sign = req.getParStr("sign");
		String usr = req.getParStr("usr");
		String phone = req.getParStr("phone");
		String pwd = req.getParStr("pwd");
		if (usr == null || phone == null || pwd == null) {
			return trans(RspErr.ERR_FORMAT_ERROR);
		}
		//
		String pnSha1 = Crypto.sha1StrLowerCase(phone.getBytes());
		byte by[] = Net.hex2bytes(pwd); /* RC4(SHA-1(phone), SHA-1(PWD)) */
		String sha1pwd = new String(Crypto.rc4dec(pnSha1.getBytes(), by)); /* 经SHA-1(phone)解密, 获得SHA-1(PWD). */
		if (!(Crypto.sha1StrLowerCase((salt + sha1pwd + req.getAction()).getBytes()).equals(sign))) {
			return trans(RspErr.Err_SIGN, "phone format error possible"); /* 格式错误. */
		}
		/** -------------------------------- */
		/**                                  */
		/** 注册. */
		/**                                  */
		/** -------------------------------- */
		if (!dbRedis.tryLock4Reg()) {
			return trans(RspErr.ERR_SYSTEM_EXCEPTION);
		}
		try {
			SysUsr sysUsr = usrService.querySysUsrInfoByPhoneOrAccount(phone, usr);
			if (sysUsr != null) {
				return trans(RspErr.ERR_USER_EXISTED);
			}
			try {
				boolean success = this.save(usr, phone, pwd);
				if (!success) {
					return trans(RspErr.ERR_FAIL);
				}
			} catch (Exception e) {
				Log.info("%s", Log.trace(e));
				return trans(RspErr.ERR_FAIL);
			}
		} finally {
			dbRedis.unLock4Reg();
		}
		String token = DbRedis.makeToken();
		String secret = Crypto.sha1StrLowerCase(Misc.gen0aAkey128().getBytes());
		UsrStub stub = new UsrStub(phone, usr, secret);
		dbRedis.setToken(token, stub);
		//
		RspUsrAuth usrAuth = new RspUsrAuth();
		usrAuth.setPhone(phone);
		usrAuth.setToken(token);
		usrAuth.setAccount(usr);
		usrAuth.setPwd(pwd);
		return trans(usrAuth);
	}

	/** 保存用户信息. */
	private boolean save(String usr, String phone, String pwd) throws Exception {
		SysUsr sysUsr = new SysUsr();
		sysUsr.setAccount(usr);
		sysUsr.setPhone(phone);
		sysUsr.setPwd(pwd);
		sysUsr.setCreateTime(new Date());
		int success = usrService.saveSysUsr(sysUsr);
		if (success > 0) {
			return true;
		}
		return false;
	}
}
