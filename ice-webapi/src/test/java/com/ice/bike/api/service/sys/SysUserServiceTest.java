package com.ice.bike.api.service.sys;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ice.bike.dao.bo.sys.SysUsr;
import com.ice.bike.service.sys.SysUsrService;

public class SysUserServiceTest {

	public static ApplicationContext ac;

	public static SysUsrService sysUsrService;

	@BeforeClass
	public static void loadApplication() {
		ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		sysUsrService = (SysUsrService) ac.getBean("sysUsrServiceImpl");
	}

	@Test
	public void saveSysUsr() {
		SysUsr usr = new SysUsr();
		usr.setPhone("13476257867");
		usr.setAccount("ice");
	}
}
