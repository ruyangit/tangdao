/**
 *
 */
package com.tangdao.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月5日
 */
public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

//	/**
//	 * 
//	 * TODO 状态更新
//	 * @param pkVal  主鍵值
//	 * @param status 更新状态
//	 * @return
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public boolean updateStatus(String pkVal, String status) {
//		UpdateWrapper<T> updateWrapper = new UpdateWrapper<T>();
//		updateWrapper.set("status", status);
//		updateWrapper.eq(TableTreeColumnScannerRegistrar.getPrimaryKey(getEntityClass()), pkVal);
//		return super.update(updateWrapper);
//	}
}
