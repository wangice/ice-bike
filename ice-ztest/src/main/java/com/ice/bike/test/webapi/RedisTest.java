package com.ice.bike.test.webapi;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ice.bike.common.redis.RedisCacheUtil;

public class RedisTest {

	static RedisCacheUtil<String> redisCacheUtil;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() {
		@SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		redisCacheUtil = (RedisCacheUtil<String>) ac.getBean("redisCacheUtil");
	}

	@Test
	public void redis() throws Exception {
		if (redisCacheUtil.setCacheObject("ice", "wang") != null) {
			String name = redisCacheUtil.getCacheObject("ice");
			System.out.println(name);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		redisCacheUtil = (RedisCacheUtil<String>) ac.getBean("redisCacheUtil");
		if (redisCacheUtil.setCacheObject("ice", "wang") != null) {
			String name = redisCacheUtil.getCacheObject("ice");
			System.out.println(name);
		}
	}
}
