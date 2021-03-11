package com.tangdao.exchanger.resolver.sms.cmpp.v2;

import com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.insa2.util.Args;
import com.huawei.smproxy.SMProxy;

public class CmppManageProxy extends SMProxy {

	// 通道ID
	private String passageId;
	private CmppProxySender cmppProxySender;

	public CmppManageProxy(CmppProxySender proxySend, String passageId, Args args) {
		super(args);
		this.passageId = passageId;
		this.cmppProxySender = proxySend;
	}

	@Override
	public CMPPMessage onDeliver(CMPPDeliverMessage msg) {
		// logger.info("华时SMProxyRec接收短信\r\n");
		cmppProxySender.doProcessDeliverMessage(msg);
		return super.onDeliver(msg);
	}

	@Override
	public void onTerminate() {
		cmppProxySender.onTerminate(passageId);
	}

}
