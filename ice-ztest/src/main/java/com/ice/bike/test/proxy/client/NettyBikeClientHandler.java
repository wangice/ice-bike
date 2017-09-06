package com.ice.bike.test.proxy.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyBikeClientHandler extends SimpleChannelInboundHandler<String> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("启动");
		ctx.channel().writeAndFlush("server");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println(msg);
		
	}

}
