package com.ice.bike.manager.controller;

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
import com.ice.bike.manager.redis.DbRedis;
import com.ice.bike.manager.rsp.RspUsrInfo;
import com.ice.bike.service.sys.SysUsrService;

/***
 * 
 * 创建时间： 2017年7月30日 下午3:44:54
 * 
 * @author ice
 *
 */
@Controller
@RequestMapping("/sys/usr")
public class UserController extends BaseController {

	@Resource
	public DbRedis dbRedis;

	@Resource
	public SysUsrService usrService;

	/** 查询用户信息. */
	@ResponseBody
	@RequestMapping(value = "/queryuserInfo", method = RequestMethod.GET)
	public String queryUserInfo(HttpServletRequest request) {
		HttpReq req = this.HttpReq(request);
		int id = req.getParInt0("id");
		if (id < 1) {
			return trans(RspErr.ERR_FORMAT_ERROR);
		}
		SysUsr usr = usrService.querySysUsrInfoById(id);
		if (usr == null) {
			return trans(RspErr.ERR_NO_RECORD);
		}
		return trans(new RspUsrInfo().fill(usr));
	}

	/** 退出登录. */
	@ResponseBody
	@RequestMapping(value = "/logotOut", method = RequestMethod.GET)
	public String logotOut(HttpServletRequest request) {
		HttpReq req = this.HttpReq(request);
		String token = req.getParStr("token");
		if (token == null) {
			return trans(RspErr.ERR_FORMAT_ERROR);
		}
		boolean success = dbRedis.deleteUsrStub(token);
		if (!success) {/* 操作异常. */
			return trans(RspErr.ERR_SYSTEM_EXCEPTION);
		}
		return trans(RspErr.ERR_NONE);
	}
}
