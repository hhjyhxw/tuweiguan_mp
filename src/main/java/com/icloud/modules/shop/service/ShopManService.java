package com.icloud.modules.shop.service;

import com.icloud.modules.shop.entity.ShopMan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.shop.dao.ShopManMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 店员 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-09-17 16:07:50
 */
@Service
@Transactional
public class ShopManService extends BaseServiceImpl<ShopManMapper,ShopMan> {

    @Autowired
    private ShopManMapper shopManMapper;
}

