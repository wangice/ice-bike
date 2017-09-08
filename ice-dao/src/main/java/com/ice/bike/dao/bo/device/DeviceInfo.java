package com.ice.bike.dao.bo.device;

import java.util.Date;

public class DeviceInfo {
	/** 主键ID. */
	private Integer id;

	/** 设备号 */
	private String deviceId;

	/** 设备序列号. */
	private String deviceSerial;
	/** 设备MAC地址. */

	private String deviceMac;
	/** 设备IMSI. */

	private String deviceImsi;

	/** 设备监控版本信息. */
	private String deviceMonitorVersion;

	/** GPS信息标识(0x00 基站定位,0x01 GPS有效标识). */
	private Byte deviceGprsItem;

	/** csq 信号. */
	private Byte deviceCsq;

	/** 更新时间. */
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceSerial() {
		return deviceSerial;
	}

	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public String getDeviceImsi() {
		return deviceImsi;
	}

	public void setDeviceImsi(String deviceImsi) {
		this.deviceImsi = deviceImsi;
	}

	public String getDeviceMonitorVersion() {
		return deviceMonitorVersion;
	}

	public void setDeviceMonitorVersion(String deviceMonitorVersion) {
		this.deviceMonitorVersion = deviceMonitorVersion;
	}

	public Byte getDeviceGprsItem() {
		return deviceGprsItem;
	}

	public void setDeviceGprsItem(Byte deviceGprsItem) {
		this.deviceGprsItem = deviceGprsItem;
	}

	public Byte getDeviceCsq() {
		return deviceCsq;
	}

	public void setDeviceCsq(Byte deviceCsq) {
		this.deviceCsq = deviceCsq;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
