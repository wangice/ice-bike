package com.ice.bike.manager.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ice.bike.manager.core.Rsp.RspErr;

import misc.Log;
import misc.Misc;

public class HttpReq {

	public HttpServletRequest request;

	public HttpServletResponse response;

	public Map<String, String> pars = new HashMap<>();

	public boolean jsonp = false;

	public HttpReq(HttpServletRequest request) {
		this.request = request;
		this.parsePars();
		this.jsonp = !Misc.isNull(this.pars.get("callback"));
	}

	public HttpReq(HttpServletRequest request, HttpServletResponse response) {
		this(request);
		this.response = response;
	}

	private final void parsePars() {
		try {
			String[] params = this.request.getQueryString().split("&");
			if (pars == null || params.length < 1)
				return;
			for (int i = 0; i < params.length; ++i) {
				String[] kv = params[i].split("=");
				if (kv == null || kv.length < 2)
					continue;
				this.pars.put(kv[0], kv[1]);
			}
		} catch (Exception e) {
			return;
		}
	}

	public final String getParStr(String par) {
		return Misc.trim(this.pars.get(par));
	}

	public final int getParInt0(String par) {
		return Misc.forceInt0(this.pars.get(par));
	}

	public final long getParLong0(String par) {
		return Misc.forceInt0(this.pars.get(par));
	}

	public final String getAction() {
		try {
			String query = this.request.getQueryString().toString();
			String action = Misc.trim(query.substring(query.indexOf("&action"), query.length()));
			action = request.getServletPath() + "?" + action ;
			return (!this.jsonp) ? action : action.substring(0, action.indexOf("&callback="));
		} catch (Exception e) {
			return null;
		}
	}

	public void end(RspErr err) {
		Rsp rsp = new Rsp();
		rsp.err = err.ordinal();
		rsp.desc = err.toString();
		rsp.dat = null;
		this.end(Misc.obj2json(rsp));
	}

	public void end(RspErr err, Object object) {
		Rsp rsp = new Rsp();
		rsp.err = err.ordinal();
		rsp.desc = err.toString();
		rsp.dat = object;
		this.end(Misc.obj2json(rsp));
	}

	/** 返回值. */
	public void end(String json) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(json);
			Log.info("return json:%s", json);
		} catch (IOException e) {
			if (Log.isError()) {
				Log.error("%s", Log.trace(e));
			}
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
