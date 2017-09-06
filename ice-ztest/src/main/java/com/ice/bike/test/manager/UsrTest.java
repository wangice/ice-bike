package com.ice.bike.test.manager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.ice.bike.test.util.RequestUtil;

import misc.Crypto;
import misc.Log;
import misc.Misc;
import misc.Net;

public class UsrTest {
	@Test
	public void regist() throws Exception {
		String usr = "ice";
		String pwd = "123456";
		String phone = "13476257867";
		String sha1Phone = Crypto.sha1StrLowerCase(phone.getBytes());
		String sha1Pwd = Crypto.sha1StrLowerCase(pwd.getBytes());
		byte[] rc4dec = Crypto.rc4dec(sha1Phone.getBytes(), sha1Pwd.getBytes());
		String rc4Pwd = Net.byte2HexStr(rc4dec);
		String salt = System.currentTimeMillis() + "";
		String head = "/usr/regist" + "?";
		String action = Misc.printf2Str("&action=regist&usr=%s&pwd=%s&phone=%s", usr, rc4Pwd, phone);
		String sign = Crypto
				.sha1StrLowerCase((salt + Crypto.sha1StrLowerCase(pwd.getBytes()) + head + action).getBytes());
		String req = Misc.printf2Str(RequestUtil.API_SHINEMONITOR_URI + head + "sign=%s&salt=%s%s", sign, salt, action);
		HttpResponse rsp = HttpClients.createDefault().execute(new HttpGet(req));
		String str = EntityUtils.toString(rsp.getEntity());
		Log.info("%s", str);
	}
}
