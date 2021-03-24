/**
 *
 */
package com.tangdao.core.config.runner;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.tangdao.core.config.worker.hook.ShutdownHookWorker;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public abstract class AbstractInitializeRunner implements CommandLineRunner {

	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public static final Lock LOCK = new ReentrantLock();
	public static final Condition CONDITION = LOCK.newCondition();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 自定义初始化资源是否完成（因有些服务强依赖某些资源初始化完成，如 rabbit listener 消费）
	 */
	public static volatile boolean isResourceInitFinished = false;

	@Override
	public void run(String... arg0) throws Exception {
		try {
			initData();
			registShutdownHook();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}

		try {
			boolean isSuccess = initMessageQueues();
			if (!isSuccess) {
				throw new RuntimeException("初始化MQ失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}

		initSignal();

	}

	/**
	 * 
	 * TODO 初始化数据
	 */
	public abstract void initData();

	/**
	 * 
	 * TODO 初始化待提交消息队列信息
	 * 
	 * @return
	 */
	public abstract boolean initMessageQueues();

	/**
	 * 
	 * TODO 初始化信号源控制
	 */
	private void initSignal() {
		LOCK.lock();
		try {
			isResourceInitFinished = true;
			CONDITION.signalAll();
			logger.info("初始化资源信号源标记完成，开始消费..");
		} finally {
			LOCK.unlock();
		}
	}

	/**
	 * TODO 注册JVM关闭钩子函数
	 */
	private void registShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHookWorker(threadPoolTaskExecutor)));
		logger.info("Jvm hook thread has registed");
	}
}
