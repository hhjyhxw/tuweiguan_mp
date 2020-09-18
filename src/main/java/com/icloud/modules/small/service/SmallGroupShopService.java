package com.icloud.modules.small.service;

import com.icloud.modules.small.entity.SmallGroupShop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.small.dao.SmallGroupShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
}

