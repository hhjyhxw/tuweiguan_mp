package com.icloud.modules.small.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.small.entity.SmallRefund;
import com.icloud.modules.small.entity.SmallUserCoupon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.small.dao.SmallRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 退款
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-08-13 14:34:02
 */
@Service
@Transactional
public class SmallRefundService extends BaseServiceImpl<SmallRefundMapper,SmallRefund> {

    @Autowired
    private SmallRefundMapper smallRefundMapper;

    @Override
    public PageUtils<SmallRefund> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);

        List<SmallRefund> list = smallRefundMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<SmallRefund> pageInfo = new PageInfo<SmallRefund>(list);
        PageUtils<SmallRefund> page = new PageUtils<SmallRefund>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

