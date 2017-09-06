package com.ice.bike.api.core;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.async.DeferredResult;
import com.ice.bike.api.core.Rsp.RspErr;

import misc.Dateu;
import misc.Log;
import misc.Misc;
import misc.Net;

public class Distrans {
	public DeferredResult<String> def;
	/** 用户的根. */
	public UsrStub stub;
	/** httpServletRequest封装. */
	public HttpReq req;
	/** 延迟结束. */
	public boolean lazy = false;
	/** 响应. */
	public Rsp rsp;
	//
	public String token;
	public String action;
	public String query;
	public String ip;
	/** 事务产生时间. */
	private long sts;
	/** 事务结束时间. */
	private long ets;

	public Distrans(HttpServletRequest request, HttpServletResponse response, DeferredResult<String> def) {
		this.sts = new Date().getTime();
		this.req = new HttpReq(request);
		this.def = def;
		response.setContentType(!this.req.jsonp ? "text/javascript" : "application/x-json");
	}

	public final void setStub(UsrStub stub) {
		this.stub = stub;
	}

	public final DeferredResult<String> end(Object o) {
		return this.end(Rsp.RspErr.ERR_NONE, o);
	}

	public final DeferredResult<String> end(RspErr err) {
		return this.end(err, null);
	}

	public final DeferredResult<String> end(RspErr err, Object o) {
		return (!this.lazy) ? this.endImmediate(err, o)/* 事务立即结束. */ : this.endLazy(err, o) /* 延迟结束, 等待flush. */;
	}

	/** 事务结束. */
	public final DeferredResult<String> flush() {
		String r = Misc.obj2json(this.rsp);
		if (this.req.jsonp) {
			StringBuilder strb = new StringBuilder("jsoncallback");
			strb.append("(").append(r).append(");");
			this.def.setResult(strb.toString());
		} else
			this.def.setResult(r);
		this.ets = new Date().getTime();
		if (this.stub == null) /* 鉴权通过之前. */
			return this.def;
		if (this.ets - this.sts > Dateu.SECOND * 5) {
			if (Log.isWarn())
				Log.warn("have http trans over 5 seconds, elap: %dms, uid: %d, usr: %s, req: %s, ip: %s",
						this.ets - this.sts, this.stub.uid, this.stub.usr, Net.urlDecode(this.query), this.ip);
		}
		// 保存访问日志
		// CollUsrOperLog usrOperLog = new CollUsrOperLog();
		// usrOperLog.uid = this.stub.uid;
		// usrOperLog.usr = this.stub.usr;
		// usrOperLog.token = this.token;
		// usrOperLog.action = this.action;
		// usrOperLog.req = Net.urlDecode(this.query);
		// usrOperLog.rsp = r.length() > WsMacro.LEN_MAX_RSP_DISP ? this.rsp.err + "" :
		// r;
		// usrOperLog.peer = this.ip;
		// usrOperLog.gts = Dateu.nowGmt0();
		// Ws.wab.future(v ->
		// {
		// try
		// {
		// DbMongo.getDataStore().save(usrOperLog);
		// } catch (Exception e)
		// {
		// if (Log.isError())
		// Log.error("%s", Log.trace(e));
		// }
		// });
		return this.def;
	}

	/** 事务延迟结束. */
	private final DeferredResult<String> endLazy(RspErr err, Object o) {
		Rsp rsp = new Rsp();
		rsp.err = err.ordinal();
		rsp.desc = err.toString();
		rsp.dat = o;
		this.rsp = rsp;
		return this.def;
	}

	/** 事务立即结束. */
	private final DeferredResult<String> endImmediate(RspErr err, Object o) {
		this.rsp = new Rsp();
		this.rsp.err = err.ordinal();
		this.rsp.desc = err.toString();
		this.rsp.dat = o;
		return this.flush();
	}

	/** 获取连接中的String参数. */
	public String getParStr(String par) {
		return this.req.getParStr(par);
	}

	/** 获取连接中的int参数. */
	public int getParInt0(String par) {
		return this.req.getParInt0(par);
	}

	/** 获取连接中的long参数. */
	public long getParLong0(String par) {
		return this.req.getParLong0(par);
	}

	public String getAction() {
		return this.req.getAction();
	}
}
