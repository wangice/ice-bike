package com.ice.manager;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ice.bike.dao.bo.sys.SysUsr;
import com.ice.bike.service.sys.SysUsrService;

import misc.Crypto;

public class UsrServiceTest {

	private static ApplicationContext ac;

	private static SysUsrService usrService;

	@BeforeClass
	public static void beforeClass() {
		ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		usrService = (SysUsrService) ac.getBean("sysUsrService");
	}

	@Test
	public void save() {
		SysUsr sysUsr = new SysUsr();
		sysUsr.setAccount("ice");
		sysUsr.setPhone("13476257867");
		String pwd = Crypto.sha1StrLowerCase("123456".getBytes());
		System.err.println(pwd.length());
		sysUsr.setPwd(pwd);
		sysUsr.setCreateTime(new Date());
		usrService.saveSysUsr(sysUsr);
	}
}
