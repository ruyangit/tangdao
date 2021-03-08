package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;

import com.tangdao.core.model.domain.sms.PriorityWords;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.SmsPriorityWordsMapper;

/**
 * 优先级词库配置ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPriorityWordsService extends BaseService<SmsPriorityWordsMapper, PriorityWords> {

}