package org.tangdao.modules.sys.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.tangdao.common.constant.RedisConstant;
import org.tangdao.common.lang.StringUtils;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sys.constant.UserBalanceConstant;
import org.tangdao.modules.sys.constant.UserSettingsContext;
import org.tangdao.modules.sys.mapper.UserSmsConfigMapper;
import org.tangdao.modules.sys.model.domain.UserSmsConfig;
import org.tangdao.modules.sys.service.IUserSmsConfigService;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserSmsConfigServiceImpl extends CrudService<UserSmsConfigMapper, UserSmsConfig> implements IUserSmsConfigService{
	
	@Resource
    private StringRedisTemplate  stringRedisTemplate;
	
	private static String getKey(String userCode) {
        return RedisConstant.RED_USER_SMS_CONFIG + ":" + userCode;
    }
	
	@Override
    public UserSmsConfig getByUserCode(String userCode) {
        if(StringUtils.isEmpty(userCode)) {
        	return null;
        }

        try {
            String text = stringRedisTemplate.opsForValue().get(getKey(userCode));
            if (StringUtils.isNotEmpty(text)) {
                return JSON.parseObject(text, UserSmsConfig.class);
            }

        } catch (Exception e) {
            log.warn("REDIS获取用户短信配置失败", e);
        }

        return this.getOne(Wrappers.<UserSmsConfig>lambdaQuery().eq(UserSmsConfig::getUserCode, userCode));
    }
	
	@Override
	public int getSingleChars(String userCode) {
		int wordsPerNum = UserBalanceConstant.WORDS_SIZE_PER_NUM;
        try {
            UserSmsConfig userSmsConfig = getByUserCode(userCode);
            if (userSmsConfig != null) {
                wordsPerNum = userSmsConfig.getSmsWords();
            }

        } catch (Exception e) {
            log.warn("查询用户：{} 短信字数配置失败，将以默认每条字数：{}计费", userCode, wordsPerNum, e);
        }
        return wordsPerNum;
	}

    private UserSmsConfig defaultConfig() {
        UserSmsConfig userSmsConfig = new UserSmsConfig();
        userSmsConfig.setCreateTime(new Date());
        userSmsConfig.setAutoTemplate("0");
        userSmsConfig.setMessagePass("0");
        userSmsConfig.setNeedTemplate("1");
        userSmsConfig.setSmsReturnRule("0");
        userSmsConfig.setSmsTimeout(0L);

        return userSmsConfig;
    }

	@Override
	public boolean save(String userCode, int words, String extNumber) {
        UserSmsConfig userSmsConfig = defaultConfig();
        userSmsConfig.setUserCode(userCode);
        userSmsConfig.setExtNumber(extNumber);
        userSmsConfig.setSmsWords(words);

        return save(userSmsConfig);
	}
	
	@Override
	public boolean update(UserSmsConfig config) {
        if (super.updateById(config)) {
            pushToRedis(config);
            return true;
        }
        return false;
	}
	
	/**
     * TODO 添加至REDIS
     *
     * @param userSmsConfig
     */
    private void pushToRedis(UserSmsConfig userSmsConfig) {
        try {
            stringRedisTemplate.opsForValue().set(getKey(userSmsConfig.getUserCode()), JSON.toJSONString(userSmsConfig));
        } catch (Exception e) {
            log.warn("REDIS 操作用户短信配置失败", e);
        }
    }

    private void removeFromRedis(String userCode) {
        try {
            stringRedisTemplate.delete(getKey(userCode));

        } catch (Exception e) {
            log.warn("REDIS 移除用户彩信配置失败", e);
        }
    }

	@Override
	public boolean reloadToRedis() {
		List<UserSmsConfig> list = this.select();
        if (CollectionUtils.isEmpty(list)) {
            log.error("用户短信配置数据为空");
            return true;
        }

        List<Object> con = stringRedisTemplate.execute((connection) -> {

            RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
            connection.openPipeline();
            for (UserSmsConfig config : list) {
                byte[] key = serializer.serialize(getKey(config.getUserCode()));
                connection.set(key, serializer.serialize(JSON.toJSONString(config)));
            }

            return connection.closePipeline();

        }, false, true);

        return CollectionUtils.isNotEmpty(con);
	}
		
}