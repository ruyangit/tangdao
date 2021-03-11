package org.tangdao.modules.exchanger.resolver.sms.sgip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tangdao.modules.exchanger.resolver.sms.sgip.constant.SgipConstant;

import com.alibaba.fastjson.JSON;
import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
import com.huawei.insa2.util.Args;
import com.huawei.smproxy.SGIPSMProxy;

public class SgipManageProxy extends SGIPSMProxy {

	private String passageId;
	private SgipProxySender sgipProxySender;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public SgipManageProxy(SgipProxySender sgipProxySender, String passageId, Args args) {
		super(args);
		try {
			// SGIP 短连接需要调用 startService，主要是为了开启短信上行报告及短信状态回执监听服务端口
			super.startService(getLocalIp(args), getLocalPort(args));
			this.passageId = passageId;
			this.sgipProxySender = sgipProxySender;
		} catch (IllegalStateException e) {
			logger.error("Initial http passage[" + passageId + "] failed by args[" + JSON.toJSONString(args) + "] ", e);
		}
	}

	private String getLocalIp(Args args) {
		return args.get(SgipConstant.NODE_NAME_LOCAL_IP, SgipConstant.DATA_LOCAL_IP);
	}

	private int getLocalPort(Args args) {
		return args.get(SgipConstant.NODE_NAME_LOCAL_PORT, SgipConstant.DATA_LOCAL_PORT);
	}

	@Override
	public SGIPMessage onDeliver(SGIPDeliverMessage msg) {
		sgipProxySender.moReceive(msg);
		return super.onDeliver(msg);
	}

	@Override
	public SGIPMessage onReport(SGIPReportMessage msg) {
		sgipProxySender.mtDeliver(msg);
		return super.onReport(msg);
	}

	@Override
	public void onTerminate() {
		sgipProxySender.onTerminate(passageId);
	}

}
