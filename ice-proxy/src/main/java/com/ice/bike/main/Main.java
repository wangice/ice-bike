package com.ice.bike.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ice.bike.cfg.SpringConfig;

/**
 * 启动程序
 * 
 * 创建时间： 2017年8月30日 下午9:53:13
 * 
 * @author ice
 *
 */
public class Main {

	private static final Logger logger = LoggerFactory.getLogger("Main");

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		/**************************************/
		/***                                ***/
		/**************************************/
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ctx.getBean(SpringConfig.class);
		ctx.registerShutdownHook();
		logger.debug("配置文件");
	}
}
