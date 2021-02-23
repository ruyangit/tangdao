/**
 *
 */
package com.tangdao.developer.web.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangdao.core.constant.CommonContext.AppType;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
public class BaseApi {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

	 /**
     * TODO 根据Header中传递值判断调用方式
     *
     * @return
     */
    protected int getAppType() {
        String appType = request.getHeader("apptype");
        if (StrUtil.isEmpty(appType)) {
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
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StrUtil.isNotEmpty(ip)) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
