package com.tangdao.core.model.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tangdao.core.utils.TimeUtil;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author ruyang
 * @since 2019-07-02
 */
@Getter
@Setter
@TableName("sys_log")
public class Log implements Serializable {
	
	// 日志类型（access：接入日志；update：修改日志；select：查询日志；loginLogout：登录登出；）
	public static final String TYPE_ACCESS = "access";
	public static final String TYPE_UPDATE = "update";
	public static final String TYPE_SELECT = "select";
	public static final String TYPE_LOGIN_LOGOUT = "loginLogout";

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    private String id;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 日志标题
     */
    private String logTitle;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 用户名称
     */
    private String createByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 操作方式
     */
    private String requestMethod;

    /**
     * 操作提交的数据
     */
    private String requestParams;

    /**
     * 新旧数据比较结果
     */
    private String diffModifyData;

    /**
     * 描述
     */
    private String description;

    /**
     * 操作IP地址
     */
    private String remoteAddr;

    /**
     * 请求服务器地址
     */
    private String serverAddr;

    /**
     * 是否异常
     */
    private String isException;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 设备名称/操作系统
     */
    private String deviceName;

    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 执行时间
     */
    private Long executeTime;

    // 操作提交的数据，临时存储用
    @TableField(exist=false)
    private Map<String, String[]> paramsMap; 	
    
    // 
    public String getExecuteTimeFormat(){
		return TimeUtil.formatDateAgo(executeTime);
	}
    
    public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	
	/**
	 * 设置请求参数
	 * @param paramMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setRequestParams(Map paramsMap){
		if (paramsMap == null){
			return;
		}
		if (this.paramsMap == null){
			this.paramsMap = new HashMap<>();
		}
		StringBuilder params = new StringBuilder();
		for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramsMap).entrySet()){
			params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
			String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
			params.append(StrUtil.endWithIgnoreCase(param.getKey(), "password") ? "*" : paramValue);
			this.paramsMap.put(param.getKey(), param.getValue());
		}
		this.requestParams = params.toString();
	}

	/**
	 * 根据名称获取参数（只有先执行setParams(Map)后才有效）
	 * @param name
	 * @return
	 */
	public String getRequestParam(String name) {
		if (paramsMap == null){
			return null;
		}
        String[] values = (String[])paramsMap.get(name);
        return values != null && values.length > 0 ? values[0] : null;
    }
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
