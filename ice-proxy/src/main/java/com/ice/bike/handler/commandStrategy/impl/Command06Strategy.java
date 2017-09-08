package com.ice.bike.handler.commandStrategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ice.bike.codec.DeviceChannelConnection.DeviceConnection;
import com.ice.bike.contants.BikeContants;
import com.ice.bike.dao.bo.device.DeviceInfo;
import com.ice.bike.device.DeviceInfoOper;
import com.ice.bike.handler.commandStrategy.AbstractCommandStrategy;
import com.ice.bike.message.BikeLockMessage;
import com.ice.bike.service.device.DeviceInfoService;

/**
 * 更新MAC地址 信息
 * 
 * 创建时间： 2017年9月7日 下午11:59:39
 * 
 * @author ice
 *
 */
@Component
public class Command06Strategy extends AbstractCommandStrategy {

	private static final Logger logger = LoggerFactory.getLogger(Command06Strategy.class);

	@Autowired
	public DeviceInfoService deviceInfoService;

	@Override
	public void executeCMD(BikeLockMessage message, DeviceConnection connection) {
		// 1.获取数据
		// 2.保存到数据库中
		// 3.写日志
		// 4.响应
		if (message.getReply() == BikeContants.REMARK) {
			this.operData(message);/* 数据保存. */
			this.responseToDevice(message, connection);
		} else {
			if (logger.isErrorEnabled()) {
				logger.error("Mac接受回复指令不正确");
			}
		}
	}

	@Override
	public void sendOrderToDevice(BikeLockMessage message, DeviceConnection connection) {

	}

	@Override
	public void responseToDevice(BikeLockMessage message, DeviceConnection connection) {
		message.setReply(BikeContants.REMARKSUCCESS);
		connection.getContext().channel().writeAndFlush(message);
	}

	@Override
	public void operData(BikeLockMessage message) {
		DeviceInfo device = DeviceInfoOper.transformDeviceInfo(message.getReport());
		int success = deviceInfoService.insertDeviceInfo(device);
		if (success > 0) {
			
		} else {
			if (logger.isDebugEnabled()) {
				logger.error("mac更新失败");
			}
		}
	}
}
