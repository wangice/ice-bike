package com.ice.bike.test.proxy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyBikeClient {
	/** 连接. */
	public void connect(int port, String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)//
					.channel(NioSocketChannel.class)//
					.option(ChannelOption.TCP_NODELAY, true)//
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("lineBasedFrameDecoder", new LineBasedFrameDecoder(100));
							ch.pipeline().addLast("stringDecoder", new StringDecoder());
							ch.pipeline().addLast("stringEncoder", new StringEncoder());
							ch.pipeline().addLast("nettyBikeClientHandler", new NettyBikeClientHandler());
						}
					});
			ChannelFuture future = b.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 9527;

		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		new NettyBikeClient().connect(port, "127.0.0.1");
	}
}
