package com.ice.bike.message;

import com.ice.bike.contants.BikeContants;

/**
 * 服务器发送指令
 * 
 * 创建时间： 2017年9月3日 下午4:54:10
 * 
 * @author ice
 *
 */
public class BikeServerOrderMessage {
	/** 构造Imsi应答包. */
	public static final BikeLockMessage createImsiMessage(String deviceId, String imsi, byte lockStatus,
			byte electrict) {
		BikeLockMessage message = new BikeLockMessage();
		message.setProtocolType(BikeContants.TYPE);
		message.setProtocolVersion(BikeContants.VERSION);
		message.setDeviceId(deviceId);
		message.setDeviceType(BikeContants.DEVICETYPE);
		message.setCommand(BikeContants.IMSICOMMAND);
		message.setReply(BikeContants.REMARKSUCCESS);
		message.setReport(BikeServerOrderMessage.createImsiReport(imsi, lockStatus, electrict));
		return message;
	}

	/** 构造IMSI发送信息包. */
	private static final BikeStatusReport createImsiReport(String imsi, byte lockStatus, byte electrict) {
		BikeStatusReport report = new BikeStatusReport();
		report.setImsi(imsi);
		report.setLock(lockStatus);
		report.setElectricity(electrict);
		report.setCspIdx((byte) 20);
		return report;
	}

	/** 构造开锁指令. */
	public static final BikeLockMessage createOpenLockMessage(String deviceId, long userId, String yyyyMMddHHmmss) {
		BikeLockMessage message = new BikeLockMessage();
		message.setProtocolType(BikeContants.TYPE);
		message.setProtocolVersion(BikeContants.VERSION);
		message.setDeviceId(deviceId);
		message.setDeviceType(BikeContants.DEVICETYPE);
		message.setCommand(BikeContants.OPENLOCKCOMMAND);
		message.setReply(BikeContants.REMARK);
		message.setReport(BikeServerOrderMessage.createOpenLockReport(userId, yyyyMMddHHmmss));
		return message;
	}

	/** 构造开锁包. */
	private static final BikeStatusReport createOpenLockReport(long userId, String yyyyMMddHHmmss) {
		BikeStatusReport report = new BikeStatusReport();
		report.setLock((byte) 2);
		report.setUserId(userId);
		report.setServerTime(yyyyMMddHHmmss);
		return report;
	}
}
