package com.ice.bike.device;

import com.ice.bike.dao.bo.device.DeviceInfo;
import com.ice.bike.message.BikeStatusReport;

public class DeviceInfoOper {

	/** BikeLockMessage转化DeviceInfo. */
	public static final DeviceInfo transformDeviceInfo(BikeStatusReport report) {
		DeviceInfo device = new DeviceInfo();
		device.setDeviceId(report.getDeviceCode() + "");
		device.setDeviceImsi(report.getImsi());
		device.setDeviceMac(report.getMacLong());
		device.setDeviceMonitorVersion(report.getMonitorVersion());
		device.setDeviceSerial(report.getSerialNum());
		device.setDeviceGprsItem(report.getGprsItme());
		device.setDeviceCsq(report.getCspIdx());
		return device;
	}
}
