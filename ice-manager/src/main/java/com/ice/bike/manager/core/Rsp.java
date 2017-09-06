package com.ice.bike.manager.core;

import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.ProtocolMessageEnum;

public class Rsp {

	public enum RspErr implements ProtocolMessageEnum {
		/** 成功. */
		ERR_NONE, ERR_FAIL,
		/** 找不到action. */
		ERR_ACTION_NOT_FOUND,
		/** 格式错误. */
		ERR_FORMAT_ERROR,
		/** 缺少必要的参数. */
		ERR_MISSING_PARAMETER,
		/** 异常. */
		ERR_SYSTEM_EXCEPTION,
		/** 未鉴权. */
		ERR_NOT_AUTH,
		/** 注册失败(用户名已存在). */
		ERR_USER_EXISTED,
		/** 用户名或密码错误. */
		ERR_USER_PWD_ERROR,
		/** 无权限. */
		ERR_NO_PERMISSION,
		/** 查询记录. */
		ERR_NO_RECORD,
		/** 用户不存在. */
		ERR_USER_UNEXISTED,
		/** 盐值错误. */
		ERR_SALT,
		/** sign错误. */
		Err_SIGN,
		/** 拒绝. */
		ERR_FORBIDDEN,
		/** 验证码失效. */
		VERIF_CODE_LOSE_EFFECT,
		/** 验证码错误. */
		VERIF_CODE_ERROR;

		@Override
		public EnumDescriptor getDescriptorForType() {
			return null;
		}

		@Override
		public int getNumber() {
			return this.ordinal();
		}

		@Override
		public EnumValueDescriptor getValueDescriptor() {
			return null;
		}
	}

	public int err;

	public String desc;

	public Object dat;
}
