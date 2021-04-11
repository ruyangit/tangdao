/**
 * 
 */
package com.tangdao.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.config.Global;
import com.tangdao.core.web.BaseController;
import com.tangdao.service.model.domain.User;

import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.OshiUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2021年4月11日
 */
@RestController
@RequestMapping("/api")
public class ServerController extends BaseController {

	
	@GetMapping("/rt")
	public CommonResponse rt() {
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("currentPID", SystemUtil.getCurrentPID());
		data.put("hostInfo", SystemUtil.getHostInfo());
		data.put("javaInfo", SystemUtil.getJavaInfo());
//		data.put("javaRuntimeInfo", SystemUtil.getJavaRuntimeInfo().toString());
		data.put("jvmInfo", SystemUtil.getJvmInfo());
		data.put("jvmHeapMemoryUsage", SystemUtil.getMemoryMXBean().getHeapMemoryUsage());
		data.put("jvmNonHeapMemoryUsage", SystemUtil.getMemoryMXBean().getNonHeapMemoryUsage());
		data.put("jvmTotalMemory", SystemUtil.getTotalMemory());
		data.put("jvmMaxMemory", SystemUtil.getMaxMemory());
		data.put("jvmfreeMemory", SystemUtil.getFreeMemory());
		data.put("totalThreadCount", SystemUtil.getTotalThreadCount());
		data.put("osInfo", SystemUtil.getOsInfo());
		data.put("userInfo", SystemUtil.getUserInfo());
		data.put("oshiMemory", OshiUtil.getMemory());
		data.put("oshiOs", OshiUtil.getOs());
		data.put("oshiProcessor", OshiUtil.getProcessor());
		data.put("oshiSensors", OshiUtil.getSensors());
		data.put("oshiSystem", OshiUtil.getSystem());
		
		return renderResult(Global.TRUE,"系统信息", data);
	}
}
