package com.ice.bike.dao.mapper.device.ext;

import com.ice.bike.dao.bo.device.DeviceInfo;

public interface DeviceInfoExtMapper {
	/** 查询设备信息. */
	DeviceInfo findDeviceInfoByDeviceId(String deviceId);
}
