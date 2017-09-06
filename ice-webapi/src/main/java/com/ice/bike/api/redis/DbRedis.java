package com.ice.bike.api.redis;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ice.bike.api.core.UsrStub;
import com.ice.bike.common.redis.RedisCacheUtil;

import misc.Crypto;
import misc.Dateu;
import misc.Log;
import misc.Misc;

/**
 * redis操作
 * 
 * 创建时间： 2017年7月19日 下午11:19:58
 * 
 * @author ice
 *
 */
@Service
public class DbRedis {
	/** 用户操作时的独占锁超时时间(毫秒). */
	// private static final long USR_OPER_LOCK_TIMEOUT = 5 * 1000;

	/** 鉴权成功后token的过期时间(秒). */
	public static final String DIS_CFG_AUTH_TOKEN_EXPIRE = "DIS_CFG_AUTH_TOKEN_EXPIRE";

	//
	public static long dis_cfg_auth_token_expire = (7 * Dateu.DAY) / Dateu.SECOND;

	/** 用户登录后token存根. */
	private static final String KEY_PREFIX_TOKEN = "TOKEN:";
	/** 验证码. */
	private static final String KEY_PREFIX_PHONE = "PHONE:";
	/** 注册用户时的分布式锁. */
	// private static final String KEY_PREFIX_REGLOCK = "REGLOCK:";

	@Resource
	public RedisCacheUtil<Object> redisCacheUtil;

	/** 构造一个鉴权token. */
	public static final String makeToken() {
		return Crypto.sha256StrLowerCase(
				new UUID(Misc.randLang(), Misc.randLang()).toString().replaceAll("-", "").getBytes());
	}

	/** 用户登录或注册后设置鉴权token. */
	public void setToken(String token, UsrStub usrStub) {
		try {
			redisCacheUtil.setCacheObject(DbRedis.KEY_PREFIX_TOKEN + token, Misc.obj2json(usrStub),
					DbRedis.dis_cfg_auth_token_expire);
		} catch (Exception e) {
			Log.error("%s", Log.trace(e));
		}
	}

	/** 根据token返回用户存根. */
	public UsrStub getToken(String token) {
		UsrStub usr = null;
		try {
			usr = (UsrStub) redisCacheUtil.getCacheObject(DbRedis.KEY_PREFIX_TOKEN + token);
		} catch (Exception e) {
			Log.error("%s", Log.trace(e));
			return usr;
		}
		return usr;
	}

	/** 更新用户存根. */
	public void updateUsrStub(String token, UsrStub usrStub) {
		try {
			redisCacheUtil.setCacheObject(DbRedis.KEY_PREFIX_TOKEN + token, Misc.obj2json(usrStub));
		} catch (Exception e) {
			Log.error("%s", Log.trace(e));
		}
	}

	/** 保存验证码. */
	public void setVerifCode(String phone, String verifCode, long time) {
		try {
			redisCacheUtil.setCacheObject(DbRedis.KEY_PREFIX_PHONE + phone, verifCode, time);
		} catch (Exception e) {
			Log.error("%s", Log.trace(e));
		}
	}

	/** 获取验证码. */
	public String getVerifCode(String phone) {
		String verifCode = null;
		try {
			verifCode = (String) redisCacheUtil.getCacheObject(DbRedis.KEY_PREFIX_PHONE + phone);
		} catch (Exception e) {
			Log.error("%s", Log.trace(e));
			return null;
		}
		return verifCode;
	}
}
