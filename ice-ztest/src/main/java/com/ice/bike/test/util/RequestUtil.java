package com.ice.bike.test.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.ice.bike.test.manager.rsp.RspUsrAuth;

import misc.Crypto;
import misc.Log;
import misc.Misc;

public class RequestUtil {

	public static final String API_SHINEMONITOR_URI = "http://localhost:8080/ice-manager";

	/** 用户登录. */
	public static final RspUsrAuth loginManager(String usr, String pwd) throws Exception {
		String salt = System.currentTimeMillis() + "";
		String action = Misc.printf2Str("&action=auth&usr=%s&company-key=%s", usr);
		String sign = Crypto.sha1StrLowerCase((salt + Crypto.sha1StrLowerCase(pwd.getBytes()) + action).getBytes());
		String req = Misc.printf2Str(RequestUtil.API_SHINEMONITOR_URI + "?sign=%s&salt=%s%s", sign, salt, action);
		HttpResponse rsp = HttpClients.createDefault().execute(new HttpGet(req));
		String str = EntityUtils.toString(rsp.getEntity());
		return Misc.json2Obj(str, RspUsrAuth.class);
	}

	/** 请求. */
	public static final String RequestGet(String token, String secret, String action, String param) throws Exception {
		String salt = System.currentTimeMillis() + "";
		String sign = Crypto.sha1StrLowerCase((salt + secret + token + action + param).getBytes());
		String req = Misc.printf2Str(RequestUtil.API_SHINEMONITOR_URI + action + "?sign=%s&salt=%s&token=%s%s", sign,
				salt, token, param);
		Log.info("req: %s", req);
		HttpResponse rsp = HttpClients.createDefault().execute(new HttpGet(req));
		String str = EntityUtils.toString(rsp.getEntity());
		Log.info("rsp: %s", str);
		return str;
	}
}
