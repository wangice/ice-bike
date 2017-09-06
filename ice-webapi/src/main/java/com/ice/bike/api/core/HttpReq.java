package com.ice.bike.api.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import misc.Misc;

public class HttpReq {

	public HttpServletRequest request;

	public Map<String, String> pars = new HashMap<>();

	public boolean jsonp = false;

	public HttpReq(HttpServletRequest request) {
		this.request = request;
		this.parsePars();
		this.jsonp = !Misc.isNull(this.pars.get("callback"));
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
			return (!this.jsonp) ? action : action.substring(0, action.indexOf("&callback="));
		} catch (Exception e) {
			return null;
		}
	}
}
