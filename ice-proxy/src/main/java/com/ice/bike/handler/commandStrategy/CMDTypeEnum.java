package com.ice.bike.handler.commandStrategy;

public enum CMDTypeEnum {
	/**
	 * 0x06   锁上报蓝牙MAC地址信息
	 * 
	 */
	CMD_MAC_REPORT((byte) 0x06, "com.ice.bike.handler.commandStrategy.impl.Command06Strategy");

	/** 命令模式. */
	private byte value;
	/** 关联的类. */
	private String description;

	private CMDTypeEnum(byte value, String description) {
		this.value = value;
		this.description = description;
	}

	public static CMDTypeEnum valueOf(byte command) {
		for (CMDTypeEnum type : CMDTypeEnum.values()) {
			if (type.value == command) {
				return type;
			}
		}
		return null;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
