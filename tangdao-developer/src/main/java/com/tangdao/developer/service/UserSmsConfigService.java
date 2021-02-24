/**
 *
 */
package com.tangdao.developer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangdao.core.constant.UserBalanceConstant;
import com.tangdao.core.model.domain.UserSmsConfig;
import com.tangdao.core.service.BaseService;
import com.tangdao.developer.dao.UserSmsConfigMapper;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月24日
 */
@Service
public class UserSmsConfigService extends BaseService<UserSmsConfigMapper, UserSmsConfig>{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public int getSingleChars(String appId) {
		int wordsPerNum = UserBalanceConstant.WORDS_SIZE_PER_NUM;
        try {
        	QueryWrapper<UserSmsConfig> queryWrapper = new QueryWrapper<UserSmsConfig>();
        	queryWrapper.eq("app_id", appId);
            UserSmsConfig userSmsConfig = this.getOne(queryWrapper);
            if (userSmsConfig != null) {
                wordsPerNum = userSmsConfig.getSmsWords();
            }

        } catch (Exception e) {
            log.error("应用：{} 短信字数配置失败，将以默认每条字数：{}计费", appId, wordsPerNum, e);
        }
        return wordsPerNum;
	}
}
