package com.ice.bike.test.proxy.client;

import com.ice.bike.contants.BikeContants;
import com.ice.bike.message.BikeLockMessage;
import com.ice.bike.message.BikeStatusReport;

/**
 * 客户端发送指令
 * 
 * 创建时间： 2017年9月3日 下午3:38:14
 * 
 * @author ice
 *
 */
public class BikeClientOrderMessage {

	/** 构造MAC应答包. */
	public static final BikeLockMessage createMacMessage(String deviceId,byte lockStatus, byte electricity, String mac, //
			String yyyyMMddHHmmss, String monitorVersion, String serialNum) {
		BikeLockMessage message = new BikeLockMessage();
		message.setProtocolType(BikeContants.TYPE);
		message.setProtocolVersion(BikeContants.VERSION);
		message.setDeviceId(deviceId);
		message.setDeviceType(BikeContants.DEVICETYPE);
		message.setCommand(BikeContants.MACCOMMAND);
		message.setReply(BikeContants.REMARK);
		message.setReport(BikeClientOrderMessage.createMacReport(lockStatus, electricity, mac, yyyyMMddHHmmss, monitorVersion, serialNum));
		return message;
	}

	/**
	 * 构造MAC发送信息包.
	 * 
	 * @param lockStatus 锁的状态
	 * @param electricity 锁的电量
	 * @param mac MAC地址
	 * @param yyyyMMddHHmmss 时间
	 * @param monitorVersion 版本信息
	 * @param serialNum 设备生产序列号
	 * @return
	 */
	public static final BikeStatusReport createMacReport(byte lockStatus, byte electricity, String mac, //
			String yyyyMMddHHmmss, String monitorVersion, String serialNum) {
		BikeStatusReport report = new BikeStatusReport();
		report.setLock(lockStatus);
		report.setElectricity(electricity);
		report.setMacLong(mac);
		report.setServerTime(yyyyMMddHHmmss);
		report.setGprsItme((byte) 0x01);
		report.setMonitorVersion(monitorVersion);
		report.setDeviceNumber(serialNum);
		return report;
	}

	/** 构造Imsi应答包. */
	public static final BikeLockMessage createImsiMessage(String deviceId) {
		BikeLockMessage message = new BikeLockMessage();
		message.setProtocolType(BikeContants.TYPE);
		message.setProtocolVersion(BikeContants.VERSION);
		message.setDeviceId(deviceId);
		message.setDeviceType(BikeContants.DEVICETYPE);
		message.setCommand(BikeContants.IMSICOMMAND);
		message.setReply(BikeContants.REMARK);
		message.setReport(BikeClientOrderMessage.createImsiReport());
		return message;
	}

	/** 构造IMSI发送信息包. */
	public static final BikeStatusReport createImsiReport() {
		BikeStatusReport report = new BikeStatusReport();
		report.setImsi("9460040071301613");
		report.setLock((byte) 1);
		report.setElectricity((byte) 50);
		report.setCspIdx((byte) 20);
		return report;
	}

	/** 开锁成功回复. */
	public static final BikeLockMessage createOpenLockSuccessMessage(String deviceId, long userId,
			String yyyyMMddHHmmss) {
		BikeLockMessage message = new BikeLockMessage();
		message.setProtocolType(BikeContants.TYPE);
		message.setProtocolVersion(BikeContants.VERSION);
		message.setDeviceId(deviceId);
		message.setDeviceType(BikeContants.DEVICETYPE);
		message.setCommand(BikeContants.OPENLOCKCOMMAND);
		message.setReply(BikeContants.REMARKSUCCESS);
		message.setReport(BikeClientOrderMessage.createOpenLockSuccessReport(userId, yyyyMMddHHmmss));
		return message;
	}

	/** 构造开锁成功包. */
	private static final BikeStatusReport createOpenLockSuccessReport(long userId, String yyyyMMddHHmmss) {
		BikeStatusReport report = new BikeStatusReport();
		report.setOpenDevice((byte) 2);
		report.setUserId(userId);
		report.setServerTime(yyyyMMddHHmmss);
		report.setLock((byte) 2);
		report.setException((byte) 0);
		return report;
	}

	/** 开锁超时. */
	public static final BikeLockMessage createOpenLockTimeOutMessage(String deviceId, long userId,
			String yyyyMMddHHmmss) {
		BikeLockMessage message = new BikeLockMessage();
		message.setProtocolType(BikeContants.TYPE);
		message.setProtocolVersion(BikeContants.VERSION);
		message.setDeviceId(deviceId);
		message.setDeviceType(BikeContants.DEVICETYPE);
		message.setCommand(BikeContants.OPENLOCKCOMMAND);
		message.setReply(BikeContants.REMARKSUCCESS);
		message.setReport(BikeClientOrderMessage.createOpenLockTimeOutReport(userId, yyyyMMddHHmmss));
		return message;
	}

	/** 构造开锁超时包. */
	private static final BikeStatusReport createOpenLockTimeOutReport(long userId, String yyyyMMddHHmmss) {
		BikeStatusReport report = new BikeStatusReport();
		report.setOpenDevice((byte) 1);
		report.setUserId(userId);
		report.setServerTime(yyyyMMddHHmmss);
		report.setLock((byte) 1);
		report.setException((byte) 4);
		return report;
	}

	/** 开锁成功回复. */
	public static final BikeLockMessage createOpenLockMessage(String deviceId, long userId, String yyyyMMddHHmmss) {
		BikeLockMessage message = new BikeLockMessage();
		message.setProtocolType(BikeContants.TYPE);
		message.setProtocolVersion(BikeContants.VERSION);
		message.setDeviceId(deviceId);
		message.setDeviceType(BikeContants.DEVICETYPE);
		message.setCommand(BikeContants.OPENLOCKACTIVECOMMAND);
		message.setReply(BikeContants.REMARK);
		message.setReport(BikeClientOrderMessage.createOpenLockSuccessReport(userId, yyyyMMddHHmmss));
		return message;
	}

}
