package com.tangdao.modules.goods.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tangdao.core.mybatis.pagination.Pageinfo;
import com.tangdao.modules.goods.model.domain.GoodsAlbumFile;
import com.tangdao.modules.goods.model.vo.GoodsAlbumFileVo;

/**
 * <p>
 * 商品相册文件 Mapper 接口
 * </p>
 *
 * @author ruyang
 * @since 2020-09-21
 */
@Mapper
public interface GoodsAlbumFileMapper extends BaseMapper<GoodsAlbumFile> {

	public IPage<GoodsAlbumFileVo> findGoodsAlbumFilePage(Pageinfo page, @Param(Constants.ENTITY) GoodsAlbumFile et);
}
