package com.icloud.modules.small.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.small.entity.SmallPurorder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.small.dao.SmallPurorderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * 采购单
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-10-06 20:50:58
 */
@Service
@Transactional
public class SmallPurorderService extends BaseServiceImpl<SmallPurorderMapper,SmallPurorder> {

    @Autowired
    private SmallPurorderMapper smallPurorderMapper;

    @Override
    public PageUtils<SmallPurorder> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<SmallPurorder> list = smallPurorderMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<SmallPurorder> pageInfo = new PageInfo<SmallPurorder>(list);
        PageUtils<SmallPurorder> page = new PageUtils<SmallPurorder>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

