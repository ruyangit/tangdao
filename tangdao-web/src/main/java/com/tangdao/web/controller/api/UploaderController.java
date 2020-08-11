/**
 *
 */
package com.tangdao.web.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tangdao.common.CommonResponse;
import com.tangdao.web.config.TangdaoProperties;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月24日
 */
@Controller
@RequestMapping(value = { "/uploader" })
public class UploaderController {

	@Autowired
	private TangdaoProperties tangdaoProperties;

	@PostMapping
	public @ResponseBody CommonResponse uploadFile(HttpServletRequest request) throws Exception {
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest mhsr;
			Iterator<String> fileName = (mhsr = (MultipartHttpServletRequest) request).getFileNames();
			while (fileName.hasNext()) {
				files.add(mhsr.getFile(fileName.next()));
			}
		}
		Long maxFileSize = tangdaoProperties.getFile().getMaxFileSize();
		String uploadPath = tangdaoProperties.getFile().getUploadPath();
		String[] fmts = StringUtils.substringsBetween(uploadPath, "{", "}");
		if(uploadPath != null) {
			for (String fmt : fmts) {
				if(StrUtil.isNotBlank(fmt) && StrUtil.containsAny(fmt, "yyyy", "MM", "dd", "HH", "mm", "ss", "E")) {
					uploadPath = StringUtils.replace(uploadPath, "{"+fmt+"}", DateUtil.format(new Date(), fmt));
				}
			}
		}
		
		String[] allowSuffixes = tangdaoProperties.getFile().getAllowSuffixes();
		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			String fileExtension = FileUtil.extName(fileName);
			if(allowSuffixes!=null && !StrUtil.containsAny(fileExtension, allowSuffixes)) {
				// 文件格式不正确
			}
			if(maxFileSize!=null&&file.getSize() > maxFileSize.longValue()) {
				// 文件大小限制
			}
			String fileRealPath = "";
			String filePath = "";
			
		}
		
		return CommonResponse.createCommonResponse();
	}
}
