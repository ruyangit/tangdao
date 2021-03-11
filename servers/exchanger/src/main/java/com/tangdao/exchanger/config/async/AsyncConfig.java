/**
 * 
 */
package com.tangdao.exchanger.config.async;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>
 * TODO 异步线程池
 * </p>
 *
 * @author ruyang
 * @since 2020年10月16日
 */
@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean("asyncTaskExecutor")
	public AsyncTaskExecutor asyncTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("async-executor-");
		executor.setCorePoolSize(100);
		executor.setMaxPoolSize(500);
		executor.setQueueCapacity(20);

		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		return executor;
	}

	@Bean(name = "threadPoolTaskExecutor")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

		ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();

		poolTaskExecutor.setThreadNamePrefix("worker-executor-");
		// 线程池所使用的缓冲队列
		poolTaskExecutor.setQueueCapacity(200);
		// 线程池维护线程的最少数量
		poolTaskExecutor.setCorePoolSize(64);
		// 线程池维护线程的最大数量
		poolTaskExecutor.setMaxPoolSize(200);
		// 线程池维护线程所允许的空闲时间
		poolTaskExecutor.setKeepAliveSeconds(10000);

		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
		poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		poolTaskExecutor.initialize();

		return poolTaskExecutor;
	}

	@Bean(name = "pushPoolTaskExecutor")
	public ThreadPoolTaskExecutor pushPoolTaskExecutor() {

		ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();

		poolTaskExecutor.setQueueCapacity(100);
		// 线程池维护线程的最少数量
		poolTaskExecutor.setCorePoolSize(10);
		// 线程池维护线程的最大数量
		poolTaskExecutor.setMaxPoolSize(80);
		// 线程池维护线程所允许的空闲时间
		poolTaskExecutor.setKeepAliveSeconds(10000);

		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
		poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		poolTaskExecutor.initialize();

		return poolTaskExecutor;
	}
}
