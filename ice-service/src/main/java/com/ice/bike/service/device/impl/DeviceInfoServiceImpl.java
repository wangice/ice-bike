package com.ice.bike.service.device.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ice.bike.dao.bo.device.DeviceInfo;
import com.ice.bike.dao.mapper.device.DeviceInfoMapper;
import com.ice.bike.dao.mapper.device.ext.DeviceInfoExtMapper;
import com.ice.bike.service.device.DeviceInfoService;

@Service("deviceInfoService")
public class DeviceInfoServiceImpl implements DeviceInfoService {

	@Autowired
	private DeviceInfoMapper deviceInfoMapper;

	@Autowired
	private DeviceInfoExtMapper deviceInfoExtMapper;

	@Override
	public int insertDeviceInfo(DeviceInfo deviceInfo) {
		int success;
		DeviceInfo device = deviceInfoExtMapper.findDeviceInfoByDeviceId(deviceInfo.getDeviceId());
		if (device == null) { /* 插入操作. */
			success = deviceInfoMapper.insertSelective(deviceInfo);
		} else {
			success = deviceInfoMapper.updateByPrimaryKeySelective(deviceInfo);
		}
		return success;
	}

}
