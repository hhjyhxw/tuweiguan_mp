package com.icloud.modules.shop.service;

import com.icloud.modules.shop.entity.ShopStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.shop.dao.ShopStoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 店铺仓库 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-09-17 16:07:50
 */
@Service
@Transactional
public class ShopStoreService extends BaseServiceImpl<ShopStoreMapper,ShopStore> {

    @Autowired
    private ShopStoreMapper shopStoreMapper;
}

