package com.ice.bike.api;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ice.bike.common.redis.RedisCacheUtil;

public class TestUtils {
	static RedisCacheUtil<Object> redisCacheUtil;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		redisCacheUtil = (RedisCacheUtil<Object>) ac.getBean("redisCacheUtil");
		// if (redisCacheUtil.setCacheObject("ice", "wang") != null) {
		// String name = (String) redisCacheUtil.getCacheObject("ice");
		// System.out.println(name);
		// }

		// Boolean absent = redisCacheUtil.getValueOperations().setIfAbsent("ice",
		// "wang");
		// System.out.println(absent.booleanValue());

		User user = new User();
		user.name = "ice";
		user.pwd = "123456";
		redisCacheUtil.setCacheObject("ice1", user);
		User object = (User) redisCacheUtil.getCacheObject("ice1");
		System.out.println("user:" + object.name + ",pwd: " + object.pwd);
	}

	public static class User implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private String pwd;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}
	}
}
