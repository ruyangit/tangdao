package com.tangdao.modules.goods.model.vo;

import com.tangdao.common.utils.WebUtils;
import com.tangdao.model.domain.FileInfo;
import com.tangdao.modules.goods.model.domain.GoodsAlbumFile;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品相册文件
 * </p>
 *
 * @author ruyang
 * @since 2020-09-21
 */
@Getter
@Setter
public class GoodsAlbumFileVo extends GoodsAlbumFile {

	private static final long serialVersionUID = 1L;

	private FileInfo fileInfo;

	public String getFileUrl() {
		if (fileInfo != null) {
			String fileName = getFileId() + "." + fileInfo.getFileExt();
			return WebUtils.getPath("/userfiles/fileupload/" + fileInfo.getFilePath()) + fileName;
		}
		return null;
	}
}
