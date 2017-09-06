package com.ice.bike.common.util;

/**
 * 
 * 创建时间： 2017年7月20日 上午12:24:59
 * 
 * @author ice
 *
 */
public class StringHelper {
	// private static String string = "abcdefghijklmnopqrstuvwxyz";
	private static String string = "0123456789";

	public static int getRandom(int count) {
		return (int) Math.round(Math.random() * (count));
	}

	/** 随机生成数字字符串. */
	public static String getRandomString(int length) {
		StringBuffer sb = new StringBuffer();
		int len = string.length();
		for (int i = 0; i < length; i++) {
			sb.append(string.charAt(getRandom(len - 1)));
		}
		return sb.toString();
	}
}
