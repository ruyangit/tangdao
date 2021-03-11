/**
 * 
 */
package com.tangdao.developer.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.tangdao.core.config.runner.AbstractInitializeRunner;

/**
 * <p>
 * TODO 初始化加载
 * </p>
 *
 * @author ruyang
 * @since 2020年11月6日
 */
@Component
public class MyStarterInitializer extends AbstractInitializeRunner implements ApplicationListener<ApplicationReadyEvent> {

	@Override
	@Async(value = "asyncTaskExecutor")
	public void onApplicationEvent(ApplicationReadyEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean initMessageQueues() {
		return true;
	}

}
