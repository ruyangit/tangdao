/**
 *
 */
package com.tangdao.exchanger.web.config;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.tangdao.exchanger.web.config.worker.hook.ShutdownHookWorker;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
@Component
public class SmsInitializeRunner implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public static final Lock LOCK = new ReentrantLock();

	public static final Condition CONDITION = LOCK.newCondition();

	/**
	 * 自定义初始化资源是否完成（因有些服务强依赖某些资源初始化完成，如 rabbit listener 消费）
	 */
	public static volatile boolean isResourceInitFinished = false;

	@Override
	public void run(String... arg0) throws Exception {
		try {

			registShutdownHook();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		initSignal();
	}

	/**
	 * 初始化信号源控制
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
