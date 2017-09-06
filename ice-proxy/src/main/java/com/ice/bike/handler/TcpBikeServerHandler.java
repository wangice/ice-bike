package com.ice.bike.handler;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ice.bike.codec.DeviceChannelConnection;
import com.ice.bike.codec.DeviceChannelConnection.DeviceConnection;
import com.ice.bike.contants.Constants;
import com.ice.bike.handler.commandStrategy.CommandContext;
import com.ice.bike.message.BikeLockMessage;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

@Component
@Qualifier("tcpBikeServerHandler")
@Sharable
public class TcpBikeServerHandler extends SimpleChannelInboundHandler<BikeLockMessage> {

	AttributeKey<String> device = AttributeKey.newInstance("device-id");
	
	@Autowired
	private CommandContext commandContext;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
		ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
		super.channelActive(ctx);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BikeLockMessage msg) throws Exception {
		DeviceConnection connection = null;
		System.out.println(msg.getDeviceId());
		connection = DeviceChannelConnection.getDeviceConnection(msg.getDeviceId());
		if (connection == null) {
			connection = new DeviceConnection(msg.getDeviceId(), Constants.now, //
					ctx, ctx.channel().remoteAddress().toString());
			DeviceChannelConnection.putDeviceConnection(msg.getDeviceId(), connection);
			ctx.channel().attr(device).set(msg.getDeviceId());
		} else {
			connection.setContext(ctx);
			connection.setLastAccess(Constants.now);
			connection.setRemoteAddress(ctx.channel().remoteAddress().toString());
		}
		commandContext.executeCMD(msg, connection);
	}
}
