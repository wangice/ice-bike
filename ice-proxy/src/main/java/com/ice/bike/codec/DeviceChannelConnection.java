package com.ice.bike.codec;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

public class DeviceChannelConnection {

	/** key:deviceId. */
	private static Map<String, DeviceConnection> map = new HashMap<>();

	public static final DeviceConnection getDeviceConnection(String deviceId) {
		return map.get(deviceId);
	}

	public static final void putDeviceConnection(String deviceId, DeviceConnection connection) {
		map.put(deviceId, connection);
	}

	/** 移除长连接. */
	public static final void removeDeviceConnection(String deviceId) {
		map.remove(deviceId);
	}

	public static class DeviceConnection {
		/** 设备ID. */
		private String deviceId;
		/** 最后连接时间. */
		private long lastAccess;
		/** 设备长连接. */
		private ChannelHandlerContext context;
		/** 远程连接地址. */
		private String remoteAddress;

		public DeviceConnection() {

		}

		public DeviceConnection(String deviceId, long lastAccess, ChannelHandlerContext context, String remoteAddress) {
			this.deviceId = deviceId;
			this.lastAccess = lastAccess;
			this.context = context;
			this.remoteAddress = remoteAddress;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}

		public long getLastAccess() {
			return lastAccess;
		}

		public void setLastAccess(long lastAccess) {
			this.lastAccess = lastAccess;
		}

		public ChannelHandlerContext getContext() {
			return context;
		}

		public void setContext(ChannelHandlerContext context) {
			this.context = context;
		}

		public String getRemoteAddress() {
			return remoteAddress;
		}

		public void setRemoteAddress(String remoteAddress) {
			this.remoteAddress = remoteAddress;
		}

	}

}
