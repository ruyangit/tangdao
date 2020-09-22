/**
 *
 */
package com.tangdao.modules.goods.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.common.CommonResponse;
import com.tangdao.core.mybatis.pagination.Pageinfo;
import com.tangdao.core.web.BaseController;
import com.tangdao.modules.goods.model.domain.GoodsAlbum;
import com.tangdao.modules.goods.model.domain.GoodsAlbumFile;
import com.tangdao.modules.goods.service.GoodsAlbumService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年9月21日
 */
@RestController
@RequestMapping(value = { "/admin/goods" })
public class GoodsAlbumController extends BaseController {

	@Autowired
	GoodsAlbumService goodsAlbumService;

	@GetMapping("/album-tree")
	public CommonResponse tree() {
		return success(goodsAlbumService.findGoodsAlbumChildList());
	}

	@GetMapping("/albums")
	public CommonResponse list(String pid) {
		return success(goodsAlbumService.findGoodsAlbumList(pid));
	}

	@PostMapping("/albums")
	public CommonResponse save(@RequestBody GoodsAlbum goodsAlbum) {
		return success(goodsAlbumService.saveOrUpdate(goodsAlbum));
	}

	@GetMapping("/album-files")
	public CommonResponse page(Pageinfo page, GoodsAlbumFile et) {
		return success(goodsAlbumService.findGoodsAlbumFilePage(page, et));
	}
	
	@PostMapping("/album-files")
	public CommonResponse saveFiles(@RequestBody List<GoodsAlbumFile> goodsAlbumFileList) {
		return success(goodsAlbumService.saveGoodsAlbumFileBatch(goodsAlbumFileList));
	}
}
