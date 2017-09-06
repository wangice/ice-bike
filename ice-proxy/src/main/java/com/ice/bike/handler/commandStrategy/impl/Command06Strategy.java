package com.ice.bike.handler.commandStrategy.impl;

import org.springframework.stereotype.Component;

import com.ice.bike.codec.DeviceChannelConnection.DeviceConnection;
import com.ice.bike.handler.commandStrategy.AbstractCommandStrategy;
import com.ice.bike.message.BikeLockMessage;

@Component
public class Command06Strategy extends AbstractCommandStrategy {

	@Override
	public void executeCMD(BikeLockMessage message, DeviceConnection connection) {
		System.out.println("执行了");
	}

	@Override
	public void sendOrderToDevice(BikeLockMessage message, DeviceConnection connection) {

	}

	@Override
	public void responseToDevice(BikeLockMessage message, DeviceConnection connection) {

	}

	@Override
	public void operData(BikeLockMessage message) {

	}

}
