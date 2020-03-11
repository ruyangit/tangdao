/**
 * 
 */
package com.tangdao.framework.persistence.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * TODO 查询时的一些预处理
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年3月10日
 */
public class QueryPreWrapper<T> extends QueryWrapper<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * todo 预处理租户内的数据
	 */
	public QueryPreWrapper<T> preCorpFilter() {
		logger.info("corp filter...");
		return this;
	}
	
}
