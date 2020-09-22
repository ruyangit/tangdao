/**
 *
 */
package com.tangdao.modules.goods.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.tangdao.core.mybatis.pagination.Pageinfo;
import com.tangdao.core.service.BaseService;
import com.tangdao.model.vo.MenuVo;
import com.tangdao.modules.goods.mapper.GoodsAlbumFileMapper;
import com.tangdao.modules.goods.mapper.GoodsAlbumMapper;
import com.tangdao.modules.goods.model.domain.GoodsAlbum;
import com.tangdao.modules.goods.model.domain.GoodsAlbumFile;
import com.tangdao.modules.goods.model.vo.GoodsAlbumFileVo;
import com.tangdao.modules.goods.model.vo.GoodsAlbumVo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年9月21日
 */
@Service
public class GoodsAlbumService extends BaseService<GoodsAlbumMapper, GoodsAlbum> {

	@Autowired
	private GoodsAlbumFileMapper goodsAlbumFileMapper;

	public List<GoodsAlbum> findGoodsAlbumList(String pid) {
		return this.baseMapper.selectList(Wrappers.<GoodsAlbum>lambdaQuery().eq(GoodsAlbum::getPId, pid));
	}

	public List<GoodsAlbumVo> findGoodsAlbumChildList() {
		List<GoodsAlbum> sourceList = this.baseMapper.selectList(Wrappers.emptyWrapper());
		Map<String, GoodsAlbumVo> dtoMap = new LinkedHashMap<String, GoodsAlbumVo>();
		for (GoodsAlbum goodsAlbum : sourceList) {
			GoodsAlbumVo goodsAlbumVo = new GoodsAlbumVo();
			goodsAlbumVo.setChildren(null);
			BeanUtil.copyProperties(goodsAlbum, goodsAlbumVo);
			dtoMap.put(goodsAlbum.getId(), goodsAlbumVo);
		}
		List<GoodsAlbumVo> targetList = CollUtil.newLinkedList();
		for (Map.Entry<String, GoodsAlbumVo> entry : dtoMap.entrySet()) {
			GoodsAlbumVo item = entry.getValue();
			String tpid = item.getPId();
			if (dtoMap.get(tpid) == null) {
				// 如果是顶层节点，直接添加到结果集合中
				targetList.add(item);
			} else {
				// 如果不是顶层节点，有父节点，然后添加到父节点的子节点中
				GoodsAlbumVo parent = dtoMap.get(tpid);
				if (parent.getChildren() == null) {
					parent.setChildren(new LinkedList<GoodsAlbumVo>());
				}
				parent.addChild(item);
			}
		}
		return targetList;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(GoodsAlbum goodsAlbum) {
		if (null != goodsAlbum) {

			GoodsAlbum parentGoodsAlbum = null;
			if (!StringUtils.checkValNull(goodsAlbum.getPId())) {
				parentGoodsAlbum = super.getById(goodsAlbum.getPId());
			}
			if (parentGoodsAlbum == null) {
				parentGoodsAlbum = new GoodsAlbum();
				parentGoodsAlbum.setId(MenuVo.ROOT_ID);
				parentGoodsAlbum.setPIds(StringUtils.EMPTY);
				goodsAlbum.setPId(parentGoodsAlbum.getId());
			}
			GoodsAlbum oldGoodsAlbum = super.getById(goodsAlbum.getId());
			
			goodsAlbum.setPIds(parentGoodsAlbum.getPIds() + parentGoodsAlbum.getId() + ",");
			super.saveOrUpdate(goodsAlbum);

			List<GoodsAlbum> children = this.list(Wrappers.<GoodsAlbum>lambdaQuery().like(GoodsAlbum::getPIds, goodsAlbum.getId()));
			children.stream().filter(e -> e.getPIds() != null && oldGoodsAlbum != null).forEach(e -> {
				e.setPIds(e.getPIds().replace(oldGoodsAlbum.getPIds(), goodsAlbum.getPIds()));
				this.updateById(e);
			});
			return true;
		}
		return false;
	}

	public IPage<GoodsAlbumFileVo> findGoodsAlbumFilePage(Pageinfo page, GoodsAlbumFile et) {
		return goodsAlbumFileMapper.findGoodsAlbumFilePage(page, et);
	}

	public boolean saveGoodsAlbumFile(GoodsAlbumFile goodsAlbumFile) {
		return SqlHelper.retBool(goodsAlbumFileMapper.insert(goodsAlbumFile));
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean saveGoodsAlbumFileBatch(List<GoodsAlbumFile> goodsAlbumFileList) {
		if (CollUtil.isNotEmpty(goodsAlbumFileList)) {
			goodsAlbumFileList.forEach(entity -> {
				if (goodsAlbumFileMapper.selectCount(
						Wrappers.<GoodsAlbumFile>lambdaQuery().eq(GoodsAlbumFile::getFileId, entity.getFileId())
								.eq(GoodsAlbumFile::getAlbumId, entity.getAlbumId())) == 0) {
					goodsAlbumFileMapper.insert(entity);
				}
			});
			return true;
		}
		return false;
	}
}
