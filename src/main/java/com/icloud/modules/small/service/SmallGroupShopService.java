package com.icloud.modules.small.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.DateUtil;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.small.entity.SmallGroupShop;
import com.icloud.modules.small.vo.GroupSkuVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.small.dao.SmallGroupShopMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-09-18 09:08:35
 */
@Service
@Transactional
public class SmallGroupShopService extends BaseServiceImpl<SmallGroupShopMapper,SmallGroupShop> {

    @Autowired
    private SmallGroupShopMapper smallGroupShopMapper;

    public PageUtils<GroupSkuVo> findByFrontPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
//        List<SmallGroupShop> list = smallGroupShopMapper.selectList(new QueryWrapper<SmallGroupShop>()
//                .eq("status",1)
//                .eq("supplier_id",query.get("supplierId")));
//                .ge("gmt_start", new Date())
//                .le("gmt_end",new Date()));
        List<GroupSkuVo> list = smallGroupShopMapper.queryGroupAndSku(query);
        PageInfo<GroupSkuVo> pageInfo = new PageInfo<GroupSkuVo>(list);
        PageUtils<GroupSkuVo> page = new PageUtils<GroupSkuVo>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }

}

