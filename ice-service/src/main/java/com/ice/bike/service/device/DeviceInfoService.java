package com.ice.bike.service.device;

import org.springframework.stereotype.Service;

import com.ice.bike.dao.bo.device.DeviceInfo;

@Service
public interface DeviceInfoService {
	/** 保存设备信息数据. */
	int insertDeviceInfo(DeviceInfo deviceInfo);
}
