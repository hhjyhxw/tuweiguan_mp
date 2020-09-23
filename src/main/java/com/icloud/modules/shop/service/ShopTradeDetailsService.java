package com.icloud.modules.shop.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.shop.entity.ShopTradeDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.shop.dao.ShopTradeDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 店铺账号交易明细 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-09-17 16:07:50
 */
@Service
@Transactional
public class ShopTradeDetailsService extends BaseServiceImpl<ShopTradeDetailsMapper,ShopTradeDetails> {

    @Autowired
    private ShopTradeDetailsMapper shopTradeDetailsMapper;

    @Override
    public PageUtils<ShopTradeDetails> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<ShopTradeDetails> list = shopTradeDetailsMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<ShopTradeDetails> pageInfo = new PageInfo<ShopTradeDetails>(list);
        PageUtils<ShopTradeDetails> page = new PageUtils<ShopTradeDetails>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

