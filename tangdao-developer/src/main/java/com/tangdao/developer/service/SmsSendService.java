/**
 *
 */
package com.tangdao.developer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tangdao.core.constant.CommonApiCode;
import com.tangdao.developer.exception.QueueProcessException;
import com.tangdao.developer.model.dto.SmsSendDTO;
import com.tangdao.developer.model.vo.SmsSendVo;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月23日
 */
@Service
public class SmsSendService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 * TODO 发送短信
	 * 
	 * @param smsSendDTO
	 * @return
	 */
	public SmsSendVo sendMessage(SmsSendDTO smsSendDTO) {
		try {
			return new SmsSendVo(0, "1");
		} catch (Exception e) {
			log.error("发送短信至队列错误， {}", e);
			throw new QueueProcessException(CommonApiCode.INTERNAL_ERROR);
		}
	}
}