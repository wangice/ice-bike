package com.ice.bike.handler.commandStrategy;

import java.util.HashMap;
import java.util.Map;

import com.ice.bike.handler.commandStrategy.impl.Command06Strategy;

public class CommandStrategyFactory {

	private static Map<Byte, CommandStrategy> map = new HashMap<>();

	private static CommandStrategyFactory factory = null;

	static {
		try {
			map.put(CMDTypeEnum.CMD_MAC_REPORT.getValue(), new Command06Strategy());
		} catch (Exception e) {
		}
	}

	private CommandStrategyFactory() {

	}

	public static CommandStrategyFactory getInstanll() {
		if (factory == null) {
			factory = new CommandStrategyFactory();
		}
		return factory;
	}

	public CommandStrategy create(byte commond) {
		return map.get(commond);
	}
}
