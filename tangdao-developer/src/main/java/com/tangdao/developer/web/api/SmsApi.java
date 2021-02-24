/**
 *
 */
package com.tangdao.developer.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.constant.CommonApiCode;
import com.tangdao.core.exception.BusinessException;
import com.tangdao.core.model.domain.sms.SmsApiFailedRecord;
import com.tangdao.developer.model.dto.SmsSendDTO;
import com.tangdao.developer.service.SmsApiFaildRecordService;
import com.tangdao.developer.service.SmsSendService;
import com.tangdao.developer.web.validate.SmsSendValidator;

import cn.hutool.core.bean.BeanUtil;

/**
 * <p>
 * TODO 短信接口
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
@RestController
@RequestMapping(value = "/sms")
public class SmsApi extends BaseApi {

	@Autowired
	private SmsSendValidator smsValidator;

	@Autowired
	private SmsApiFaildRecordService smsApiFaildRecordService;

	@Autowired
	private SmsSendService smsSendService;

	@RequestMapping(value = "/send", method = { RequestMethod.POST, RequestMethod.GET })
	public CommonResponse send(SmsSendDTO smsSendDTO) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		try {
			// 填充ip和应用类型
			smsSendDTO.setIp(getClientIp());
			smsSendDTO.setAppType(getAppType());
			// 前置校验
			smsSendDTO = this.smsValidator.validate(smsSendDTO);
			return commonResponse.setData(smsSendService.sendMessage(smsSendDTO));
		} catch (BusinessException e) {
			SmsApiFailedRecord record = new SmsApiFailedRecord();
			try {
				JSONObject jsonObj = JSON.parseObject(e.getMessage());
				record.setCode(jsonObj.getString("status"));
				commonResponse.fail(jsonObj.getIntValue("status"), jsonObj.getString("message"));
			} catch (Exception e2) {
				commonResponse.fail(CommonApiCode.INTERNAL_ERROR);
			}

			try {
				BeanUtil.copyProperties(smsSendDTO, record);

				record.setSubmitUrl(request.getRequestURL().toString());
				record.setRemarks(JSON.toJSONString(request.getParameterMap()));
				// save
				smsApiFaildRecordService.save(record);
			} catch (Exception e2) {
			}
			return commonResponse.fail(CommonApiCode.INTERNAL_ERROR);
		} catch (Exception e) {
			log.error("用户短信发送失败", e);
			return commonResponse.fail(CommonApiCode.INTERNAL_ERROR);
		}
	}
}
