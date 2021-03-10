/**
 *
 */
package com.tangdao.core.utils;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangdao.core.context.CommonContext.CMCP;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月3日
 */
public class MobileCatagoryUtil {

	private static Logger logger = LoggerFactory.getLogger(MobileCatagoryUtil.class);

	// 数据分隔符
	public static final String DATA_SPLIT_CHARCATOR = ",";

	/**
	 * 
	 * TODO 针对手机号码进行分类,如移动号码,联通号码,电信号码
	 * 
	 * @param mobileNumbers EXCEL或者TXT或者其他途径来的手机号码
	 * @return
	 */
	public static MobileCatagory doCatagory(String mobileNumber) {
		if (StrUtil.isEmpty(mobileNumber)) {
			{
				return null;
			}
		}

		MobileCatagory response = new MobileCatagory();
		try {
			String[] numbers = mobileNumber.split(DATA_SPLIT_CHARCATOR);
			if (numbers.length == 0) {
				{
					return null;
				}
			}

			// 移动号码
			StringBuilder cmNumbers = new StringBuilder();
			// 电信号码
			StringBuilder ctNumbers = new StringBuilder();
			// 联通号码
			StringBuilder cuNumbers = new StringBuilder();
			// 已过滤号码
			StringBuilder filterNumbers = new StringBuilder();
			for (String number : numbers) {
				switch (CMCP.local(number)) {
				case CHINA_MOBILE: {
					cmNumbers.append(number).append(DATA_SPLIT_CHARCATOR);
					response.setCmSize(response.getCmSize() + 1);
					break;
				}
				case CHINA_TELECOM: {
					ctNumbers.append(number).append(DATA_SPLIT_CHARCATOR);
					response.setCtSize(response.getCtSize() + 1);
					break;
				}
				case CHINA_UNICOM: {
					cuNumbers.append(number).append(DATA_SPLIT_CHARCATOR);
					response.setCuSize(response.getCuSize() + 1);
					break;
				}
				default:
					filterNumbers.append(number).append(DATA_SPLIT_CHARCATOR);
					response.setFilterSize(response.getFilterSize() + 1);
					break;
				}

			}
			response.setCmNumbers(cutTail(cmNumbers.toString()));
			response.setCtNumbers(cutTail(ctNumbers.toString()));
			response.setCuNumbers(cutTail(cuNumbers.toString()));
			response.setFilterNumbers(cutTail(filterNumbers.toString()));
			response.setSuccess(true);

		} catch (Exception e) {
			logger.error("{} 号码分流错误, 信息为: {}", mobileNumber, e.getMessage());
			response.setSuccess(false);
			response.setMsg("号码分流错误");
		}
		logger.info(response.toString());
		return response;
	}

	/**
	 * 
	 * TODO 针对手机号码进行分类,如移动号码,联通号码,电信号码
	 * 
	 * @param mobileNumbers EXCEL或者TXT或者其他途径来的手机号码
	 * @return
	 */
	public static MobileCatagory doCatagory(List<String> mobiles) {
		if (CollUtil.isEmpty(mobiles)) {
			{
				return null;
			}
		}

		MobileCatagory response = new MobileCatagory();
		try {

			// 移动号码
			StringBuilder cmNumbers = new StringBuilder();
			// 电信号码
			StringBuilder ctNumbers = new StringBuilder();
			// 联通号码
			StringBuilder cuNumbers = new StringBuilder();
			// 已过滤号码
			StringBuilder filterNumbers = new StringBuilder();
			for (String number : mobiles) {
				switch (CMCP.local(number)) {
				case CHINA_MOBILE: {
					cmNumbers.append(number).append(DATA_SPLIT_CHARCATOR);
					response.setCmSize(response.getCmSize() + 1);
					break;
				}
				case CHINA_TELECOM: {
					ctNumbers.append(number).append(DATA_SPLIT_CHARCATOR);
					response.setCtSize(response.getCtSize() + 1);
					break;
				}
				case CHINA_UNICOM: {
					cuNumbers.append(number).append(DATA_SPLIT_CHARCATOR);
					response.setCuSize(response.getCuSize() + 1);
					break;
				}
				default:
					filterNumbers.append(number).append(DATA_SPLIT_CHARCATOR);
					response.setFilterSize(response.getFilterSize() + 1);
					break;
				}

			}
			response.setCmNumbers(cutTail(cmNumbers.toString()));
			response.setCtNumbers(cutTail(ctNumbers.toString()));
			response.setCuNumbers(cutTail(cuNumbers.toString()));
			response.setFilterNumbers(cutTail(filterNumbers.toString()));
			response.setSuccess(true);

		} catch (Exception e) {
			logger.error("{} 号码分流错误, 信息为: {}", StrUtil.join(DATA_SPLIT_CHARCATOR, mobiles), e.getMessage());
			response.setSuccess(false);
			response.setMsg("号码分流错误");
		}
		logger.info(response.toString());
		return response;
	}

	/**
	 * 
	 * TODO 去除结尾符号信息
	 * 
	 * @param mobile
	 * @return
	 */
	private static String cutTail(String mobile) {
		if (StrUtil.isEmpty(mobile)) {
			{
				return "";
			}
		}

		return mobile.substring(0, mobile.length() - 1);
	}

	public static class MobileCatagory implements Serializable {

		private static final long serialVersionUID = -4960868073810642628L;
		// 处理结果: true or false
		private boolean success;
		// 结果描述，处理错误才有值
		private String msg;
		// 移动号码总数
		private int cmSize;
		// 移动号码，以半角逗号分隔
		private String cmNumbers;
		// 联通号码总数
		private int cuSize;
		// 联通号码
		private String cuNumbers;
		// 电信号码总数
		private int ctSize;
		// 电信号码
		private String ctNumbers;
		// 过滤号码总数
		private int filterSize;
		// 过滤信息
		private String filterNumbers;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public int getCmSize() {
			return cmSize;
		}

		public void setCmSize(int cmSize) {
			this.cmSize = cmSize;
		}

		public String getCmNumbers() {
			return cmNumbers;
		}

		public void setCmNumbers(String cmNumbers) {
			this.cmNumbers = cmNumbers;
		}

		public int getCuSize() {
			return cuSize;
		}

		public void setCuSize(int cuSize) {
			this.cuSize = cuSize;
		}

		public String getCuNumbers() {
			return cuNumbers;
		}

		public void setCuNumbers(String cuNumbers) {
			this.cuNumbers = cuNumbers;
		}

		public int getCtSize() {
			return ctSize;
		}

		public void setCtSize(int ctSize) {
			this.ctSize = ctSize;
		}

		public String getCtNumbers() {
			return ctNumbers;
		}

		public void setCtNumbers(String ctNumbers) {
			this.ctNumbers = ctNumbers;
		}

		public String getFilterNumbers() {
			return filterNumbers;
		}

		public void setFilterNumbers(String filterNumbers) {
			this.filterNumbers = filterNumbers;
		}

		public int getFilterSize() {
			return filterSize;
		}

		public void setFilterSize(int filterSize) {
			this.filterSize = filterSize;
		}

		@Override
		public String toString() {
			return "MobileCatagory [处理结果=" + success + ", 移动号码数=" + cmSize + ", 联通号码数=" + cuSize + ", 电信号码数=" + ctSize
					+ ", 过滤号码数=" + filterSize + ", 过滤信息=" + filterNumbers + "]";
		}

	}
}
