package com.ice.bike.manager.redis;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.ice.bike.common.redis.RedisCacheUtil;
import com.ice.bike.manager.core.UsrStub;

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
@Service("dbRedis")
public class DbRedis {
	public static long clock = System.currentTimeMillis();

	/** 用户操作时的独占锁超时时间(毫秒). */
	private static final long USR_OPER_LOCK_TIMEOUT = 5 * 1000;

	/** 鉴权成功后token的过期时间(秒). */
	public static final String DIS_CFG_AUTH_TOKEN_EXPIRE = "DIS_CFG_AUTH_TOKEN_EXPIRE";

	//
	public static long dis_cfg_auth_token_expire = (7 * Dateu.DAY) / Dateu.SECOND;

	/** 用户登录后token存根. */
	private static final String KEY_PREFIX_TOKEN = "TOKEN:";
	/** 验证码. */
	private static final String KEY_PREFIX_PHONE = "PHONE:";
	/** 注册用户时的分布式锁. */
	private static final String KEY_PREFIX_REGLOCK = "REGLOCK:";

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

	/** 删除用户存根. */
	public boolean deleteUsrStub(String token) {
		try {
			return redisCacheUtil.deleteKey(token);
		} catch (Exception e) {
			Log.error("%s", Log.trace(e));
			return false;
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

	/** ---------------------------------------------------------------- */
	/**                                                                  */
	/** 分布式锁. */
	/**                                                                  */
	/** ---------------------------------------------------------------- */
	/** 尝试获得注册时的分布式锁. */
	public boolean tryLock4Reg() {
		long ts = DbRedis.clock;
		while (!this.__tryLock__(DbRedis.KEY_PREFIX_REGLOCK)) {
			Misc.sleep(50);
			if (DbRedis.clock - ts > DbRedis.USR_OPER_LOCK_TIMEOUT + 500)
				return false;
		}
		return true;
	}

	/** 释放用户注册时的分布式锁. */
	public void unLock4Reg() {
		this.__unLock__(DbRedis.KEY_PREFIX_REGLOCK);
	}

	/** 尝试获得分布式锁. */
	private boolean __tryLock__(String key) {
		try {
			ValueOperations<String, Object> redis = redisCacheUtil.getValueOperations();
			String val = DbRedis.clock + "";
			if (redis.setIfAbsent(key, val)) /* 锁被成功设置. */
				return true;
			String oldval = (String) redis.get(key);
			if (oldval == null) /* 锁刚刚被释放. */
				return redis.setIfAbsent(key, val);
			if (DbRedis.clock - Misc.forceLongO(oldval) < DbRedis.USR_OPER_LOCK_TIMEOUT) /* 锁未超时. */
				return false;
			/** -------------------------------- */
			/**                                  */
			/** 锁已超时. */
			/**                                  */
			/** -------------------------------- */
			oldval = (String) redis.getAndSet(key, val);
			if (oldval == null) /* 锁刚刚被释放. */
				return redis.setIfAbsent(key, val);
			return (DbRedis.clock - Misc.forceLongO(oldval) >= DbRedis.USR_OPER_LOCK_TIMEOUT); /* 锁已超时, 且没有其它进程干扰. */
		} catch (Exception e) {
			Log.error("%s", Log.trace(e));
			return false;
		}
	}

	/** 释放分布式锁. */
	private void __unLock__(String key) {
		try {
			redisCacheUtil.deleteKey(DbRedis.KEY_PREFIX_REGLOCK);
		} catch (Exception e) {
			Log.error("%s", Log.trace(e));
		}
	}
}
