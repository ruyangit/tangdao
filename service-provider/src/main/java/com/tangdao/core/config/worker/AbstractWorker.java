package com.tangdao.core.config.worker;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.config.worker.hook.ShutdownHookWorker;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO 抽象进程基础类
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
public abstract class AbstractWorker<E> implements Runnable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected ApplicationContext applicationContext;

	/**
	 * 批量扫描大小
	 */
	private static final int DEFAULT_SCAN_SIZE = 2000;

	/**
	 * 超时时间毫秒值(JOB一秒内处理)
	 */
	private static final int DEFAULT_TIMEOUT = 1000;

	/**
	 * 当前时间计时
	 */
	protected final AtomicLong timer = new AtomicLong(0);

	/**
	 * 备份数据恢复完成
	 */
	private final AtomicBoolean backupsRecovered = new AtomicBoolean();

	/**
	 * REDIS 队列备份KEY名称前缀
	 */
	private static final String REDIS_QUEUE_BACKUP_KEY_PREFIX = "bak_";

	/**
	 * TODO 是否终止执行(JVM 应用程序钩子HOOK设置)
	 *
	 * @return
	 */
	protected boolean isApplicationStop() {
		return ShutdownHookWorker.shutdownSignal;
	}

	protected StringRedisTemplate getStringRedisTemplate() {
		return applicationContext.getBean(StringRedisTemplate.class);
	}

	public AbstractWorker(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	/**
	 * TODO 执行具体操作
	 *
	 * @param list
	 */
	protected abstract void operate(List<E> list);

	/**
	 * TODO 获取REDIS操作值
	 *
	 * @return
	 */
	protected abstract String redisKey();

	/**
	 * 日志便签，方便查看
	 *
	 * @return
	 */
	protected abstract String jobTitle();

	/**
	 * TODO redis 备份KEY 名称
	 *
	 * @return
	 */
	protected String redisBackupKey() {
		return REDIS_QUEUE_BACKUP_KEY_PREFIX + redisKey();
	}

	/**
	 * TODO 数据失败后持久化REDIS
	 *
	 * @param list 本次失败集合数据
	 */
	private void backupIfFailed(List<E> list) {
		try {
			getStringRedisTemplate().opsForList().rightPushAll(redisBackupKey(), JSON.toJSONString(list));
			logger.info(jobTitle() + "源数据队列：{} 处理失败，加入失败队列: {} 完成，共{}条", redisKey(), redisBackupKey(), list.size());
		} catch (Exception e) {
			logger.error(jobTitle() + "源数据队列：{} 处理失败，加入失败队列: {} 异常，共{}条", redisKey(), redisBackupKey(), list.size(), e);
		}
	}

	/**
	 * TODO 每次扫描的总数量
	 *
	 * @return
	 */
	protected int scanSize() {
		return DEFAULT_SCAN_SIZE;
	}

	/**
	 * TODO 截止超时时间（单位：毫秒）
	 *
	 * @return
	 */
	protected long timeout() {
		return DEFAULT_TIMEOUT;
	}

	/**
	 * TODO 清除资源，重新开始
	 *
	 * @param list
	 */
	protected void clear(List<E> list) {
		timer.set(0);
		list.clear();
	}

	/**
	 * 
	 * TODO 检查并恢复处理失败或者JVM关闭备份数据（入队列前面，优先处理）
	 */
	protected void recoverFromBackups() {
		if (backupsRecovered.get()) {
			return;
		}

		try {
			List<String> list = new ArrayList<>();
			while (true) {
				String row = getStringRedisTemplate().opsForList().leftPop(redisBackupKey());
				if (StrUtil.isEmpty(row)) {
					break;
				}

				list.add(row);
			}

			if (CollUtil.isEmpty(list)) {
				return;
			}

			// 采用LEFT 入队，优先处理
			getStringRedisTemplate().opsForList().leftPushAll(redisKey(), list);
			logger.error(jobTitle() + "源数据队列：{} 从备份队列 :{} 恢复完成，共{}条", redisKey(), redisBackupKey(), list.size());
		} catch (Exception e) {
			logger.error(jobTitle() + "源数据队列：{} 从备份队列 :{} 恢复异常", redisKey(), redisBackupKey(), e);
		} finally {
			backupsRecovered.set(true);
		}

	}

	/**
	 * TODO 获取对象实例
	 *
	 * @param clazz
	 * @return
	 */
	protected <T> T getInstance(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	@SuppressWarnings("unchecked")
	private Class<E> getClildType() {
		Class<E> clazz = (Class<E>) getClass();
		ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
		// 3返回实际参数类型(泛型可以写多个)
		Type[] types = type.getActualTypeArguments();
		// 4 获取第一个参数(泛型的具体类) Person.class
		return (Class<E>) types[0];
	}

	/**
	 * TODO 执行任务并统计耗时
	 *
	 * @param list
	 */
	private void executeWithTimeCost(List<E> list) {
		long startTime = System.currentTimeMillis();
		try {
			operate(list);

			long timeCost = System.currentTimeMillis() - startTime;
			if (timeCost > 500 || list.size() > 50) {
				logger.info("job::[" + jobTitle() + "] 执行耗时：{} ms， 共处理：{} 个", timeCost, list.size());
			}

		} catch (Exception e) {
			logger.error("job::[" + jobTitle() + "] 执行失败", e);
			backupIfFailed(list);
		} finally {
			clear(list);
		}
	}

	/**
	 * 时间计数器
	 */
	private void timeStarter() {
		// 如果为0则表示初始化状态，则需要至
		if (timer.get() == 0) {
			timer.set(System.currentTimeMillis());
		}
	}

	@Override
	public void run() {
		List<E> list = new ArrayList<>();
		while (true) {
			if (isApplicationStop()) {
				if (CollUtil.isNotEmpty(list)) {
					logger.info("JVM关闭事件---当前线程处理数据不为空，执行最后一次后关闭线程...");
					executeWithTimeCost(list);
				}
				break;
			}

			try {
				// 先休眠1毫秒，避免cpu占用过高
				Thread.sleep(500L);
			} catch (InterruptedException e) {
			}

			try {

				// 时间启动器（开始计时）
				timeStarter();

				// 检查备份数据是否有数据产生，如果产生需要恢复（系统启动第一次恢复）
				recoverFromBackups();

				// 如果本次量达到批量取值数据，则跳出
				if (list.size() >= scanSize()) {
					executeWithTimeCost(list);
					continue;
				}

				// 如果本次循环时间超过5秒则跳出
				if (CollUtil.isNotEmpty(list) && System.currentTimeMillis() - timer.get() >= timeout()) {
					executeWithTimeCost(list);
					continue;
				}

				// 获取REDIS 队列中的数据
//                Object o = getStringRedisTemplate().opsForList().rightPopAndLeftPush(redisKey(), redisBackupKey());
				String row = getStringRedisTemplate().opsForList().leftPop(redisKey());

				// 执行到redis中没有数据为止
				if (StrUtil.isEmpty(row)) {
					if (CollUtil.isNotEmpty(list)) {
						executeWithTimeCost(list);
					}

					continue;
				}

				// 根据值对象的类型进行数据解析，填充
				Object value = JSON.parse(row);
				if (value instanceof List) {
					list.addAll(JSON.parseArray(row, getClildType()));
				} else {
					list.add(JSON.parseObject(row, getClildType()));
				}

			} catch (Exception e) {
				logger.error("自定义监听线程过程处理失败，数据为：[" + JSON.toJSONString(list) + "]", e);
			}
		}
	}

}
