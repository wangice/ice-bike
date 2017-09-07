package com.ice.bike.common.mogodb.dao;

import java.util.List;

import net.sf.json.JSONObject;


public interface DeviceLocationService {
	   // 查询
    public List<DeviceLocationVo> getItemInfo(JSONObject json) throws Exception;
    
    // 保存
    public int save(DeviceLocationVo itemInfo) throws Exception;
    
    // 更新
    public void update(DeviceLocationVo intemInfo) throws Exception;
    
    //根据deviceid查询该设备信息
    public DeviceLocationVo getDeviceInfo(JSONObject json) throws Exception;
    
}
