package com.ice.bike.handler.commandStrategy;

import org.springframework.stereotype.Component;

import com.ice.bike.codec.DeviceChannelConnection.DeviceConnection;
import com.ice.bike.message.BikeLockMessage;

@Component
public abstract class AbstractCommandStrategy implements CommandStrategy {

	public static final byte REMARK = (byte) 0xFF;// 应答标志(表示此包为发出的命令，而非命令的应答包)
	public static final byte REMARK2 = (byte) 0x00;// 应答标志(表示此包为应答包)

	@Override
	public void executeCMD(BikeLockMessage message, DeviceConnection connection) {
		if (message.getReply() == REMARK) {
			sendOrderToDevice(message, connection);
		} else if (message.getReply() == REMARK2) {
			responseToDevice(message, connection);
		} else {
			// todo 其他指令,不做任何操作.
		}
	}

	@Override
	public void sendOrderToDevice(BikeLockMessage message, DeviceConnection connection) {
		connection.getContext().channel().writeAndFlush(message);
	}

	@Override
	public void responseToDevice(BikeLockMessage message, DeviceConnection connection) {

	}

	@Override
	public void operData(BikeLockMessage message) {

	}

}
