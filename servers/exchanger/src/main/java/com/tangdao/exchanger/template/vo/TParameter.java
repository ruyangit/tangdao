package com.tangdao.exchanger.template.vo;

import java.util.HashMap;
import java.util.Map;

import com.tangdao.exchanger.resolver.sms.cmpp.constant.CmppConstant;
import com.tangdao.exchanger.resolver.sms.smgp.constant.SmgpConstant;

/**
 * 
 * <p>
 * TODO 请求参数组装,通道模板参数设置相关
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class TParameter extends HashMap<String, Object> implements Map<String, Object> {

    // 需要单独解析的通道（非公用配置模式）
    public static final String CUSTOM_PASSAGE    = "custom";
    // 手机号码节点名称
    public static final String MOBILE_NODE_NAME  = "mobile";
    // 任务ID
    public static final String MSGID_NODE_NAME   = "msgId";
    // 消息ID节点名称
    public static final String CONTENT_NODE_NAME = "content";

    private static final long  serialVersionUID  = -101745481533497079L;

    // requestName => parameterName
    // showName => displayName
    // parseName => expressionName

    public String customPassage() {
        return this.get(CUSTOM_PASSAGE) == null ? null : this.get(CUSTOM_PASSAGE).toString();
    }

    public String getString(String key) {
        if (super.get(key) == null) {
            return "";
        }

        return super.get(key).toString();
    }

    /**
     * 组装CMPP连接需要的属性信息
     * 
     * @return
     */
    public Map<String, Object> getCmppConnectAttrs() {
        Map<String, Object> attrs = new HashMap<>();
        // ISMG主机地址
        attrs.put("host", get("ip"));
        // ISMG主机端口号:行业网关地址8855
        attrs.put("port", getOrDefault("port", 7890));
        // (最大为六位字符) 移动提供的用户名即企业代码 （用户名）
        attrs.put("source-addr", get("username"));
        // shared-secret由中国移动与ICP事先商定，移动提供的密码
        attrs.put("shared-secret", get("password"));
        // 双方协商的版本号(大于0，小于256)，其值的计算方法为：主版本号*16+副版本号 例如：版本1.2的值为0x12
        attrs.put("version", getOrDefault("version", CmppConstant.DEFAULT_VERSION));
        // 是否属于调试状态,true表示属于调试状态，所有的消息被打印输出到屏幕，false表示不属于调试状态，所有的消息不被输出
        attrs.put("debug", getOrDefault("debug", "false"));

        // 心跳信息发送间隔时间(单位：秒)
        attrs.put("heartbeat-interval", getOrDefault("heartbeat_interval", 10));
        // 连接中断时重连间隔时间(单位：秒)
        attrs.put("reconnect-interval", getOrDefault("reconnect_interval", 30));
        // 需要重连时，连续发出心跳而没有接收到响应的个数（单位：个)
        attrs.put("heartbeat-noresponseout", getOrDefault("heartbeat_noresponseout", 5));
        // 操作超时时间(单位：秒)
        attrs.put("transaction-timeout", getOrDefault("transaction_timeout", 10));

        return attrs;
    }

    /**
     * 组装SGIP连接需要的属性信息
     * 
     * @return
     */
    public Map<String, Object> getSgipConnectAttrs() {
        Map<String, Object> attrs = new HashMap<>();
        // ISMG主机地址
        attrs.put("host", get("ip"));
        // ISMG主机端口号:行业网关地址8855
        attrs.put("port", getOrDefault("port", 8890));

        // 本地IP和端口，用于接受短信状态报告等数据
        attrs.put("local-ip", get("local_ip"));
        attrs.put("local-port", get("local_port"));

        // (最大为六位字符) 移动提供的用户名即企业代码 （用户名）
        attrs.put("login-name", get("username"));
        // shared-secret由中国移动与ICP事先商定，移动提供的密码
        attrs.put("login-pass", get("password"));
        // 是否属于调试状态,true表示属于调试状态，所有的消息被打印输出到屏幕，false表示不属于调试状态，所有的消息不被输出
        attrs.put("debug", getOrDefault("debug", "false"));

        // 心跳信息发送间隔时间(单位：秒)
        attrs.put("heartbeat-interval", getOrDefault("heartbeat_interval", 10));
        // 连接中断时重连间隔时间(单位：秒)
        attrs.put("reconnect-interval", getOrDefault("reconnect_interval", 10));
        // 需要重连时，连续发出心跳而没有接收到响应的个数（单位：个)
        attrs.put("heartbeat-noresponseout", getOrDefault("heartbeat_noresponseout", 30));
        // 操作超时时间(单位：秒)
        attrs.put("transaction-timeout", getOrDefault("transaction_timeout", 60));

        attrs.put("read-timeout", getOrDefault("read_timeout", 61));

        return attrs;
    }

    /**
     * 组装SMGP连接需要的属性信息
     * 
     * @return
     */
    public Map<String, Object> getSmgpConnectAttrs() {
        Map<String, Object> attrs = new HashMap<>();
        // ISMG主机地址
        attrs.put("host", get("ip"));
        // ISMG主机端口号:行业网关地址8855
        attrs.put("port", getOrDefault("port", 9001));
        // (最大为六位字符) 移动提供的用户名即企业代码 （用户名）
        attrs.put("clientid", get("username"));
        // shared-secret由中国移动与ICP事先商定，移动提供的密码
        attrs.put("shared-secret", get("password"));
        attrs.put("version", getOrDefault("version", SmgpConstant.DEFAULT_VERSION));
        // 是否属于调试状态,true表示属于调试状态，所有的消息被打印输出到屏幕，false表示不属于调试状态，所有的消息不被输出
        attrs.put("debug", getOrDefault("debug", "false"));
        // 心跳信息发送间隔时间(单位：秒)
        attrs.put("heartbeat-interval", getOrDefault("heartbeat_interval", 10));
        // 连接中断时重连间隔时间(单位：秒)
        attrs.put("reconnect-interval", getOrDefault("reconnect_interval", 10));
        // 需要重连时，连续发出心跳而没有接收到响应的个数（单位：个)
        attrs.put("heartbeat-noresponseout", getOrDefault("heartbeat_noresponseout", 30));
        // 操作超时时间(单位：秒)
        attrs.put("transaction-timeout", getOrDefault("transaction_timeout", 60));

        return attrs;
    }

}
