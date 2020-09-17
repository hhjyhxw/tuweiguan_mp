package com.icloud.modules.shop.service;

import com.icloud.modules.shop.entity.ShopTradeDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.shop.dao.ShopTradeDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
}

