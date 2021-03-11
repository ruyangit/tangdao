package org.tangdao.developer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tangdao.common.constant.CommonContext.AppType;
import org.tangdao.common.network.IpUtils;
import org.tangdao.developer.validator.AuthorizationValidator;

import com.alibaba.fastjson.JSON;

public class BasicApiSupport {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;	
    
    @Autowired
    protected AuthorizationValidator passportValidator;

    @Override
    public String toString() {
        return JSON.toJSONString(request.getParameterMap());
    }

    /**
     * TODO 根据Header中传递值判断调用方式
     *
     * @return
     */
    protected int getAppType() {
        String appType = request.getHeader("apptype");
        if (StringUtils.isEmpty(appType)) {
            return AppType.DEVELOPER.getCode();
        }

        try {
            if (String.valueOf(AppType.WEB.getCode()).equals(appType)) {
                return AppType.WEB.getCode();
            }

            if (String.valueOf(AppType.BOSS.getCode()).equals(appType)) {
                return AppType.BOSS.getCode();
            }
        } catch (Exception e) {
        }

        return AppType.DEVELOPER.getCode();
    }
    
    /**
     * 
     * TODO 获取客户端请求IP
     * 
     * @return
     */
    protected String getClientIp() {
        return IpUtils.getRemoteAddr(request);
    }

}
