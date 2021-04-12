/**
 * 
 */
package com.tangdao.web.controller;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.config.Global;
import com.tangdao.core.web.BaseController;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.OshiUtil;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

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
//		data.put("currentPID", SystemUtil.getCurrentPID());
		data.put("hostInfo", SystemUtil.getHostInfo());
		data.put("javaInfo", SystemUtil.getJavaInfo().toString());
//		data.put("javaRuntimeInfo", SystemUtil.getJavaRuntimeInfo().toString());
//		data.put("jvmInfo", SystemUtil.getJvmInfo());
		// 堆内存
		MemoryUsage heapMemoryUsage = SystemUtil.getMemoryMXBean().getHeapMemoryUsage();
		data.put("jvmHeapMemoryInit", byteToStr(heapMemoryUsage.getInit()));
		data.put("jvmHeapMemoryMax", byteToStr(heapMemoryUsage.getMax()));
		data.put("jvmHeapMemoryUsed", byteToStr(heapMemoryUsage.getUsed()));
		data.put("jvmHeapMemoryCommitted", byteToStr(heapMemoryUsage.getCommitted()));
//		System.out.println(heapMemoryUsage.toString());

		// 非堆内存
		MemoryUsage nonHeapMemoryUsage = SystemUtil.getMemoryMXBean().getNonHeapMemoryUsage();
//		System.out.println(nonHeapMemoryUsage.toString());
		data.put("jvmNonHeapMemoryInit", byteToStr(nonHeapMemoryUsage.getInit()));
		data.put("jvmNonHeapMemoryMax", byteToStr(nonHeapMemoryUsage.getMax()));
		data.put("jvmNonHeapMemoryUsed", byteToStr(nonHeapMemoryUsage.getUsed()));
		data.put("jvmNonHeapMemoryCommitted", byteToStr(nonHeapMemoryUsage.getCommitted()));

		// 虚拟机总内存
		data.put("jvmMemoryTotal", byteToStr(SystemUtil.getTotalMemory()));
//		data.put("jvmMaxMemory", SystemUtil.getMaxMemory());
		data.put("jvmMemoryAvailable", byteToStr(SystemUtil.getFreeMemory()));
		data.put("jvmMemoryUsage", NumberUtil.mul(NumberUtil
				.div((SystemUtil.getTotalMemory() - SystemUtil.getFreeMemory()), SystemUtil.getTotalMemory(), 2), 100));
//		data.put("totalThreadCount", SystemUtil.getTotalThreadCount());
//		data.put("osInfo", SystemUtil.getOsInfo());
//		data.put("userInfo", SystemUtil.getUserInfo());
//		data.put("oshiMemory", OshiUtil.getMemory());
		// 服务器总内存
		GlobalMemory globalMemory = OshiUtil.getMemory();
		data.put("oshiMemoryTotal", byteToStr(globalMemory.getTotal()));
		data.put("oshiMemoryAvailable", byteToStr(globalMemory.getAvailable()));
		data.put("oshiMemoryUsage", NumberUtil.mul(
				NumberUtil.div((globalMemory.getTotal() - globalMemory.getAvailable()), globalMemory.getTotal(), 2),
				100));
//		data.put("oshiMemoryVirtualMemory", OshiUtil.getMemory().getVirtualMemory());
//		data.put("oshiOsFileSystem", OshiUtil.getOs().getFileSystem());
		OperatingSystem os = OshiUtil.getOs();
		List<OSFileStore> list = os.getFileSystem().getFileStores();
		List<JSONObject> oshiOsFileSystemDisks = new LinkedList<JSONObject>();
		for (OSFileStore fs : list) {
			if (StrUtil.isEmpty(fs.getType())) {
				continue;
			}
			JSONObject json = new JSONObject();
			json.put("mount", fs.getMount());
			json.put("type", fs.getType());
			json.put("name", fs.getName());
			json.put("diskTotal", byteToStr(fs.getTotalSpace()));
			json.put("diskAvailable", byteToStr(fs.getUsableSpace()));
			if (fs.getUsableSpace() == 0) {
				json.put("diskUsage", 0);
			} else {
				json.put("diskUsage", NumberUtil
						.mul(NumberUtil.div((fs.getTotalSpace() - fs.getUsableSpace()), fs.getTotalSpace(), 2), 100));
			}
			oshiOsFileSystemDisks.add(json);
		}
		data.put("oshiOsFileSystemDisks", oshiOsFileSystemDisks);
//		data.put("oshiOsSystemUptime", TimeUtil.formatDateAgo(os.getSystemUptime()));
		data.put("oshiOsName", os.toString());
		data.put("oshiOsArch", SystemUtil.getOsInfo().getArch());

//		data.put("oshiOsProcessCount", OshiUtil.getOs().getProcessCount());
//		data.put("oshiOsVersionInfo", OshiUtil.getOs().getVersionInfo());
//		data.put("oshiProcessor", OshiUtil.getProcessor());

		// 处理器数量
		CentralProcessor centralProcessor = OshiUtil.getProcessor();

		data.put("oshiProcessorIdentifierName", centralProcessor.getProcessorIdentifier().getName());
		data.put("oshiProcessorLogicalProcessorCount", centralProcessor.getLogicalProcessorCount());
		// 总使用率
//		long[] ticksBegin = centralProcessor.getSystemCpuLoadTicks();
//		ThreadUtil.sleep(1000);
//		long[] ticksEnd = centralProcessor.getSystemCpuLoadTicks();
//		long nice = ticksEnd[TickType.NICE.getIndex()] - ticksBegin[TickType.NICE.getIndex()];
//		long irq = ticksEnd[TickType.IRQ.getIndex()] - ticksBegin[TickType.IRQ.getIndex()];
//		long softirq = ticksEnd[TickType.SOFTIRQ.getIndex()] - ticksBegin[TickType.SOFTIRQ.getIndex()];
//		long steal = ticksEnd[TickType.STEAL.getIndex()] - ticksBegin[TickType.STEAL.getIndex()];
//		long system = ticksEnd[TickType.SYSTEM.getIndex()] - ticksBegin[TickType.SYSTEM.getIndex()];
//		long user = ticksEnd[TickType.USER.getIndex()] - ticksBegin[TickType.USER.getIndex()];
//		long iowait = ticksEnd[TickType.IOWAIT.getIndex()] - ticksBegin[TickType.IOWAIT.getIndex()];
//		long idle = ticksEnd[TickType.IDLE.getIndex()] - ticksBegin[TickType.IDLE.getIndex()];
//		long total = user + nice + system + idle + iowait + irq + softirq + steal;
//		data.put("oshiProcessorTotalUsage", NumberUtil.round(NumberUtil.mul(total, 100), 2));
//		data.put("oshiSensors", OshiUtil.getSensors());
//		data.put("oshiSystem", OshiUtil.getSystem());

		data.put("javaStartTime", getStartTime());
		data.put("javaRunTime", getRunTime());

		return renderResult(Global.TRUE, "系统信息", data);
	}

	/**
	 * JDK启动时间
	 */
	private String getStartTime() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		Date date = new Date(time);
		return DateUtil.formatDateTime(date);
	}

	/**
	 * JDK运行时间
	 */
	private String getRunTime() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		Date date = new Date(time);

		// 运行多少分钟
		long runMS = DateUtil.between(date, new Date(), DateUnit.MS);

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;

		long day = runMS / nd;
		long hour = runMS % nd / nh;
		long min = runMS % nd % nh / nm;
		return day + "天" + hour + "小时" + min + "分钟";
	}

	/**
	 * 字节转换
	 * 
	 * @param size 字节大小
	 * @return 转换后值
	 */
	private String byteToStr(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;
		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else {
			return String.format("%d B", size);
		}
	}

}
