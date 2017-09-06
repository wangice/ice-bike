package com.ice.bike.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ice.bike.message.BikeLockDecoder;
import com.ice.bike.message.BikeLockEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

@Component
@Qualifier("nettyBikeProtocolInitalizer")
public class NettyBikeProtocolInitalizer extends ChannelInitializer<SocketChannel> {

	@Autowired
	@Qualifier("tcpBikeServerHandler")
	private TcpBikeServerHandler tcpBikeServerHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new BikeLockDecoder());
		ch.pipeline().addLast(new BikeLockEncoder());
		ch.pipeline().addLast("handler", tcpBikeServerHandler);
	}
}
