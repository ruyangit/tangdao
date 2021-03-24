package com.tangdao.core.config.worker.hook;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 * <p>
 * TODO 描述 钩子回调线程
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
public class ShutdownHookWorker implements Runnable {

	/**
	 * 停机监控
	 */
	private final Object startupShutdownMonitor = new Object();

	/**
	 * 自定义线程关闭标记（用于钩子回调）
	 */
	public static volatile boolean shutdownSignal = false;

	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public ShutdownHookWorker(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
	}

	@Override
	public void run() {
		synchronized (startupShutdownMonitor) {
			ShutdownHookWorker.shutdownSignal = true;
			threadPoolTaskExecutor.shutdown();
		}

	}
}
