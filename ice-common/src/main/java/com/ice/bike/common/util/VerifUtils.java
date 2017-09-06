package com.ice.bike.common.util;

import com.ice.bike.common.constant.CommonConstants;

/**
 * 
 * 创建时间： 2017年7月16日 上午12:42:11
 * 
 * @author ice
 *
 */
public class VerifUtils {

	/** 验证电话号码的长度. */
	public static final boolean verifPhone(String phone) {
		if (phone == null) {
			return false;
		}
		if (phone.length() < CommonConstants.PHONE_MIN_LEN //
				&& phone.length() > CommonConstants.PHONE_MAX_LEN) {
			return true;
		}
		return true;
	}
}
