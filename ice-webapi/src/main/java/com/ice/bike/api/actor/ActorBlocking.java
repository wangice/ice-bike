package com.ice.bike.api.actor;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import misc.Misc;

/**
 * 拥有自己独立线程(一个或多个)的Actor, 用于有限的IO阻塞操作, 如数据库.
 * 
 * 创建时间： 2017年7月18日 下午9:40:31
 * 
 * @author ice
 *
 */
public abstract class ActorBlocking extends Actor {

	/** 等待处理的Consumer. */
	private ConcurrentLinkedQueue<Consumer<Void>> cs = new ConcurrentLinkedQueue<>();
	/** cs的size. */
	private final AtomicInteger size = new AtomicInteger();
	/** 拥有线程的个数. */
	private int tc = 1;
	/** 线程忙? */
	private volatile boolean busy = false;

	public ActorBlocking(int tc) {
		super(ActorType.BLOCKING);
		this.tc = tc < 1 ? 1 : tc;
		this.start();
	}

	/** 线程忙? */
	public boolean isBusy() {
		return this.busy;
	}

	/** 队列尺寸. */
	public int size() {
		return this.size.get();
	}

	public void push(Consumer<Void> c) {
		this.cs.add(c);
		size.incrementAndGet();
		synchronized (this) {
			this.notify();
		}
	}

	private void start() {
		ActorBlocking ab = this;
		ExecutorService es = Executors.newFixedThreadPool(tc);
		for (int i = 0; i < this.tc; i++) {
			es.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						ab.run();
					}
				}
			});
		}
	}

	private void run() {
		Consumer<Void> c = this.cs.poll();
		if (c == null) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
			c = this.cs.poll();
		}
		if (c != null) {/* 抢占式. */
			size.decrementAndGet();
			this.busy = true;
			Misc.exeConsumer(c, null);
			this.busy = false;
		}
	}
}
