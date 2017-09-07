package com.ice.bike.common.mogodb.dao;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="device_location")
public class DeviceLocationVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3241805767996085905L;
	//@Id 
	private String id ;
	//@Field(value="deviceId")
	private String deviceId;
	//@Field(value="deviceLock")
	private String deviceLock;
	//@Field(value="electric")
	private String electric;
	//@Field(value="exception")
	private String exception;
	//private String loc;
	//@Field(value="longitude")
	private String longitude;
	//@Field(value="latitude")
	private String latitude;
	//@Field(value="nowTime")
	private String nowTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceLock() {
		return deviceLock;
	}

	public void setDeviceLock(String deviceLock) {
		this.deviceLock = deviceLock;
	}

	public String getElectric() {
		return electric;
	}

	public void setElectric(String electric) {
		this.electric = electric;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	
//    @Override
//	public String toString() {
//	    return "ItemInfo [id=" + id + ", deviceId=" + deviceId + ", deviceLock=" + deviceLock + ", electric=" + electric + ","
//	    		+ " exception=" + exception + ", longitude=" + longitude + ", latitude=" + latitude + ", nowTime=" + nowTime + "]";
//	}
	
}
