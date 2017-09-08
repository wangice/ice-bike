package com.ice.bike.dao.mapper.device;

import com.ice.bike.dao.bo.device.DeviceInfo;

public interface DeviceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceInfo record);

    int insertSelective(DeviceInfo record);

    DeviceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceInfo record);

    int updateByPrimaryKey(DeviceInfo record);
}