package com.ice.bike.dao.mapper.sys;

import com.ice.bike.dao.bo.sys.SysUsr;

public interface SysUsrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUsr record);

    int insertSelective(SysUsr record);

    SysUsr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUsr record);

    int updateByPrimaryKey(SysUsr record);
}