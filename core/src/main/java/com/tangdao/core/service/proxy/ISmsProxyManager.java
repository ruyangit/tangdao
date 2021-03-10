/**
 *
 */
package com.tangdao.core.service.proxy;

import com.tangdao.core.model.domain.SmsPassageParameter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface ISmsProxyManager {

	/**
     * TODO 加载CMPP/SMGP 代理
     * 
     * @param parameter
     * @return
     */
    boolean startProxy(SmsPassageParameter parameter);

    /**
     * TODO 根据通道ID判断代理是否可用
     * 
     * @param passageId
     * @return
     */
    boolean isProxyAvaiable(String passageId);

    /**
     * TODO 停用代理
     * 
     * @param passageId
     * @return
     */
    boolean stopProxy(String passageId);

    /**
     * TODO 累加发送错误次数
     * 
     * @param passageId
     */
    void plusSendErrorTimes(String passageId);

    /**
     * TODO 清空通道发送错误次数
     * 
     * @param passageId
     */
    void clearSendErrorTimes(String passageId);
}
