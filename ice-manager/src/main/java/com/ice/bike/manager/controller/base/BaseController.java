package com.ice.bike.manager.controller.base;

import javax.servlet.http.HttpServletRequest;

import com.ice.bike.manager.core.HttpReq;
import com.ice.bike.manager.core.Rsp;
import com.ice.bike.manager.core.Rsp.RspErr;

import misc.Misc;

public class BaseController {
	private Rsp rsp;

	protected HttpReq HttpReq(HttpServletRequest request) {
		return new HttpReq(request);
	}

	protected String trans(RspErr err) {
		return this.trans(err, null);
	}

	protected String trans(Object o) {
		this.rsp = new Rsp();
		this.rsp.err = RspErr.ERR_NONE.ordinal();
		this.rsp.desc = RspErr.ERR_NONE.toString();
		this.rsp.dat = o;
		return Misc.obj2json(rsp);
	}

	protected String trans(RspErr err, Object o) {
		this.rsp = new Rsp();
		this.rsp.err = err.ordinal();
		this.rsp.desc = err.toString();
		this.rsp.dat = o;
		return Misc.obj2json(rsp);
	}
}
