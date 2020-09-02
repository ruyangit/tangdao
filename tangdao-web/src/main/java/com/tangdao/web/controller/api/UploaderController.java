/**
 *
 */
package com.tangdao.web.controller.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tangdao.common.CommonResponse;
import com.tangdao.common.utils.WebUtils;
import com.tangdao.core.web.BaseController;
import com.tangdao.web.config.TangdaoProperties;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月24日
 */
@Slf4j
@Controller
public class UploaderController extends BaseController {

	@Autowired
	private TangdaoProperties tangdaoProperties;

	@PostMapping(value = { "/uploader" })
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
		String uploadPath = WebUtils.getReplacePathDkh(tangdaoProperties.getFile().getUploadPath());

		List<Map<String, Object>> results = new LinkedList<Map<String, Object>>();
		String relativePath = WebUtils.getPath(uploadPath);
		String[] allowSuffixes = tangdaoProperties.getFile().getAllowSuffixes();
		for (MultipartFile multipartFile : files) {
			Map<String, Object> result = new LinkedHashMap<String, Object>();
			try {
				String fileName = multipartFile.getOriginalFilename();
				String fileExt = FileUtil.extName(fileName);
				if (allowSuffixes != null && !StrUtil.containsAny(fileExt, allowSuffixes)) {
					// 文件格式不正确
				}
				if (maxFileSize != null && multipartFile.getSize() > maxFileSize.longValue()) {
					// 文件大小限制
				}
				String fileRealPath = getFileRealPath(tangdaoProperties.getFile().getBaseDir(), relativePath, fileName);
				File file;
				if (!(file = new File(fileRealPath)).getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					multipartFile.transferTo(file);
				}
//				result.put("fileRealPath", fileRealPath);
				result.put("filePath", relativePath);
				result.put("fileUrl", getFileUrl(relativePath, fileName));
				result.put("fileName", fileName);
				result.put("fileExt", fileExt);
				result.put("fileContentType", multipartFile.getContentType());

			} catch (Exception e) {
				log.error(e.getMessage(), e);
				result.put("status", "1");
				result.put("message", "上传失败");
			}
			results.add(result);
		}
		return success("上传完成", results);
	}
	
	@GetMapping("/userfiles/**")
	public void userfiles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filePath = StringUtils.substringAfter(request.getRequestURI(), "/userfiles");
		String fileName = FileUtil.getName(filePath);
//		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
		
		try {
			filePath = WebUtils.getUserfilesBaseDir(tangdaoProperties.getFile().getBaseDir(), filePath);
			WebUtils.downFile(new File(filePath), request, response, fileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
//		ServletOutputStream out = response.getOutputStream();
//		BufferedInputStream input = null;
//		try {
//			input = FileUtil.getInputStream(filePath);
//			IoUtil.copy(input, out);
//			input.close();
//			out.close();
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}
	}
	
	private String getFileRealPath(String baseDir, String relativePath, String fileName) {
		return WebUtils.getUserfilesBaseDir(baseDir, "/fileupload/" + relativePath) + fileName;
	}

	private String getFileUrl(String relativePath, String fileName) {
		return WebUtils.getPath("/userfiles/fileupload/" + relativePath) + fileName;
	}
}
