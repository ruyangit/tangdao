/**
 * 
 */
package com.tangdao.portal.config.async;

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
}
