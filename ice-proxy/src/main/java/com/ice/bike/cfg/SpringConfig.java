package com.ice.bike.cfg;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.ice.bike.handler.NettyBikeProtocolInitalizer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Configuration
@PropertySource({ "netty-server.properties" })
public class SpringConfig {
	@Value("${boss.thread.count}")
	private int bossCount;

	@Value("${worker.thread.count}")
	private int workerCount;

	@Value("${so.keepalive}")
	private boolean keepAlive;

	@Value("${so.backlog}")
	private int backlog;

	@Value("${tcp.port}")
	private int tcpPort;

	@Autowired
	@Qualifier("nettyBikeProtocolInitalizer")
	private NettyBikeProtocolInitalizer bikeProtocolInitalizer;

	@Bean(name = "bootstrap")
	public ServerBootstrap bootstrap() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup(), workGroup())//
				.channel(NioServerSocketChannel.class)//
				.childHandler(bikeProtocolInitalizer);
		Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
		for (ChannelOption option : tcpChannelOptions.keySet()) {
			bootstrap.option(option, tcpChannelOptions.get(option));
		}
		return bootstrap;
	}

	@Bean(name = "bossGroup")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(bossCount);
	}

	@Bean(name = "workGroup")
	public NioEventLoopGroup workGroup() {
		return new NioEventLoopGroup(workerCount);
	}

	@Bean(name = "tcpSocketAddress")
	public InetSocketAddress tcpAddress() {
		return new InetSocketAddress(tcpPort);
	}

	@Bean(name = "tcpChannelOptions")
	public Map<ChannelOption<?>, Object> tcpChannelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
		options.put(ChannelOption.SO_BACKLOG, backlog);
		return options;
	}
}
