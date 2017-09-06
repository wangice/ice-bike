package com.ice.bike.api.actor;

import java.util.function.Consumer;

import misc.Log;
import misc.Misc;

/**
 * actor抽象基类.
 * 
 * 创建时间： 2017年7月18日 下午8:33:04
 * 
 * @author ice
 *
 */
public class Actor {
	public enum ActorType {
		/** 进程内Actor,附着某个Twork上. */
		ITC,
		/** 拥有自己独立线程或线程池的actor,主要用于IO阻塞操作，如数据库查询. */
		BLOCKING,
		/** 即network to host, 网络到主机的连接. */
		N2H,
		/** 即host to network, 主机到网络的连接. */
		H2N;
	}

	/** 工作线程索引, -1时无效. */
	public int wk = -1;
	/** Actor类型. */
	public ActorType type;
	/** Actor名称. */
	public String name;

	public Actor(ActorType type) {
		this.type = type;
		this.name = this.getClass().getSimpleName();
	}
	
	public void future(Consumer<Void> c) {
		if (this.type.ordinal() == ActorType.BLOCKING.ordinal()) {/* 直接入队. */
			((ActorBlocking) this).push(c);
			return;
		}
		if (this.wk == -1) {/* 线程无效. */
			Log.fault("it`s a bug, t: %s, %s", this.name, Misc.getStackInfo());
			return;
		}
		Misc.exeConsumer(c, null);
	}
}
