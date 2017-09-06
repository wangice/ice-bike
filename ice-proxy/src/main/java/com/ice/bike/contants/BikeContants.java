package com.ice.bike.contants;

public class BikeContants {

	/** 协议类型. */
	public static final byte TYPE = 0x01;

	/** 协议版本. */
	public static final byte VERSION = 0x01;

	/** 单车类别. */
	public static final byte DEVICETYPE = 0x01;

	/** 应答标志 . */
	public static final byte REMARK = (byte) 0xFF;

	/** 应答标志 (成功). */
	public static final byte REMARKSUCCESS = (byte) 0x00;

	/** MAC 命令. */
	public static final byte MACCOMMAND = 0x06;

	/** IMSI 命令. */
	public static final byte IMSICOMMAND = 0x08;

	/** 开锁指令. */
	public static final byte OPENLOCKCOMMAND = 0x04;

	/** 设备主动上报数据. */
	public static final byte OPENLOCKACTIVECOMMAND = 0x15;
}
