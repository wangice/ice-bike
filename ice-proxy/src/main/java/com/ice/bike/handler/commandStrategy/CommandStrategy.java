package com.ice.bike.handler.commandStrategy;

import org.springframework.stereotype.Service;

import com.ice.bike.codec.DeviceChannelConnection.DeviceConnection;
import com.ice.bike.message.BikeLockMessage;

@Service
public interface CommandStrategy {
	/** 执行指令. */
	void executeCMD(BikeLockMessage message, DeviceConnection connection);

	/** 发送指令到设备. */
	void sendOrderToDevice(BikeLockMessage message, DeviceConnection connection);

	/** 响应设备. */
	void responseToDevice(BikeLockMessage message, DeviceConnection connection);

	/** 操作数据. */
	void operData(BikeLockMessage message);
}
