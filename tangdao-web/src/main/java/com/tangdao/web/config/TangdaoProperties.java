/**
 *
 */
package com.tangdao.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月11日
 */
@Data
@ConfigurationProperties(prefix = "tangdao")
public class TangdaoProperties {

	/**
	 * 是否演示模式
	 */
	private Boolean demo = false;

	/**
	 * 用戶配置
	 */
	private UserProperties user = new UserProperties();
	
	/**
	 * 文件配置
	 */
	private FileProperties file = new FileProperties();
	
	@Data
	public class UserProperties {

		private String superAdmin = "demo";

		/**
		 * 是否超管理员
		 * 
		 * @param username 用户登录账号
		 * @return
		 */
		public boolean isSuperAdmin(String username) {
			return StrUtil.equals(superAdmin, username);
		}
	}

	@Data
	public class FileProperties {

		private String baseDir;
		
		private String uploadPath = "{yyyy}{MM}/";
		
		private Long maxFileSize = 524288000L;
		
		private String[] allowSuffixes = { ".jpeg", ".jpg" };
		
		private Boolean isFileStreamDown = false;
	}
}
