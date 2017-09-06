package com.ice.bike.handler.commandStrategy;

import org.springframework.stereotype.Component;

import com.ice.bike.codec.DeviceChannelConnection.DeviceConnection;
import com.ice.bike.message.BikeLockMessage;

@Component
public class CommandContext {

	private CommandStrategy strategy;
	private CommandStrategyFactory strategyFactory;

	public CommandContext() {
		strategyFactory = CommandStrategyFactory.getInstanll();
	}

	public void executeCMD(BikeLockMessage message, DeviceConnection connection) throws Exception {
		if (message != null && CMDTypeEnum.valueOf(message.getCommand()) != null) {
			strategy = strategyFactory.create(message.getCommand());
			strategy.executeCMD(message, connection);
		} else {
			// 异常
			System.out.println("此命令【" + (message != null ? message.getCommand() : "") + "】不在命令字列表中");
		}
	}
}
