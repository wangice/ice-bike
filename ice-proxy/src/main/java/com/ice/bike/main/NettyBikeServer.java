package com.ice.bike.main;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;

@Component
public class NettyBikeServer {
	@Autowired
	@Qualifier("bootstrap")
	private ServerBootstrap bootstrap;

	@Autowired
	@Qualifier("tcpSocketAddress")
	private InetSocketAddress tcpAddress;

	private ChannelFuture tcpAcceptorFuture;

	@PostConstruct
	public void start() throws Exception {
		tcpAcceptorFuture = bootstrap.bind(9527).sync();
		System.out.println("启动程序");
	}

	@PreDestroy
	public void destory() throws Exception {
		tcpAcceptorFuture.channel().close().sync();
		System.out.println("关闭程序");
	}
}
