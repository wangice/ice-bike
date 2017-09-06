package com.ice.bike.message;

public class BikeLockMessage {
	/** 协议类型. */
	private byte protocolType;
	/** 协议版本号. */
	private byte protocolVersion;
	/** 设备编号. */
	private String deviceId;
	/** 设备类型. */
	private byte deviceType;
	/** 命令字. */
	private byte command;
	/** 应答标识. */
	private byte reply;
	/** 内容. */
	private BikeStatusReport report;

	public byte getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(byte protocolType) {
		this.protocolType = protocolType;
	}

	public byte getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(byte protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public byte getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(byte deviceType) {
		this.deviceType = deviceType;
	}

	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	public byte getReply() {
		return reply;
	}

	public void setReply(byte reply) {
		this.reply = reply;
	}

	public BikeStatusReport getReport() {
		return report;
	}

	public void setReport(BikeStatusReport report) {
		this.report = report;
	}

	@Override
	public String toString() {
		return "BikeLockMessage [protocolType=" + protocolType + ", protocolVersion=" + protocolVersion + ", deviceId="
				+ deviceId + ", deviceType=" + deviceType + ", command=" + command + ", reply=" + reply + ", report="
				+ report + "]";
	}
}
