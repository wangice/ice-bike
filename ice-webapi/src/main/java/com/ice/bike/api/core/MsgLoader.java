package com.ice.bike.api.core;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import misc.Log;

/**
 * 加载配置文件
 * 
 * 创建时间： 2017年7月19日 下午11:09:59
 * 
 * @author ice
 *
 */
public class MsgLoader {
	/** action在登录后才能操作 */
	public static final int ACTION_OPTION_LOGIN = 1;
	/** action操作需要对数据库近些写操作 */
	public static final int ACTION_OPTION_DBWR = 2;

	public static final ConcurrentHashMap<String, MsgStub> msgs = new ConcurrentHashMap<>();

	/** 读取配置文件 */
	@SuppressWarnings("unchecked")
	public static final boolean init() {
		try {
			Document read = new SAXReader().read("../msg/msg.xml");
			List<Element> msgs = read.getRootElement().elements("msgs");
			for (int i = 0; i < msgs.size(); i++) {
				if (!MsgLoader.loadMsg(msgs.get(i))) {
					return false;
				}
			}
			return msgs.size() > 0;
		} catch (Exception e) {
			if (Log.isError())
				Log.error("%s", Log.trace(e));
			return false;
		}
	}

	public static final boolean loadMsg(Element element) throws Exception {
		Class<?> cls = Class.forName(element.attributeValue("class"));
		if (cls == null) {
			Log.info("can not found class:%s", element.attribute("class"));
			return false;
		}
		@SuppressWarnings("unchecked")
		List<Element> elements = element.elements("msg");
		for (Element e : elements) {
			String methodName = e.attributeValue("method");
			String desc = e.attributeValue("desc");
			Method method = MsgLoader.findMethodByName(cls, methodName);
			boolean login = !("false".equals(e.attributeValue("login")));
			String option = e.attributeValue("option");
			boolean dbrw = option == null ? false : option.indexOf("DBRW") >= 0;
			int bitset = 0X00000000;
			if (login)
				bitset |= MsgLoader.ACTION_OPTION_LOGIN;
			if (dbrw)
				bitset |= MsgLoader.ACTION_OPTION_DBWR;
			if (desc == null) {
				Log.info("can not found class:%s method:%s", methodName, method);
				return false;
			}
			MsgLoader.msgs.put(method.getName(), new MsgStub(method, bitset, desc));
		}
		return true;
	}

	public static class MsgStub {
		public Method method;
		public int option;
		public String desc;

		public MsgStub(Method method, int option, String desc) {
			super();
			this.method = method;
			this.option = option;
			this.desc = desc;
		}
	}

	public static final Method findMethodByName(Class<?> cls, String methodName) {
		Method[] mt = cls.getDeclaredMethods();
		for (Method method : mt) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}
}
