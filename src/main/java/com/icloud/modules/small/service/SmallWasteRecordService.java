package com.icloud.modules.small.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.common.MapEntryUtils;
import com.icloud.common.PageUtils;
import com.icloud.modules.small.entity.SmallWasteRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.small.dao.SmallWasteRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * 资金流水记录表
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-10-07 20:18:12
 */
@Service
@Transactional
public class SmallWasteRecordService extends BaseServiceImpl<SmallWasteRecordMapper,SmallWasteRecord> {

    @Autowired
    private SmallWasteRecordMapper smallWasteRecordMapper;

    @Override
    public PageUtils<SmallWasteRecord> findByPage(int pageNo, int pageSize, Map<String, Object> query) {
        PageHelper.startPage(pageNo, pageSize);
        List<SmallWasteRecord> list = smallWasteRecordMapper.queryMixList(MapEntryUtils.clearNullValue(query));
        PageInfo<SmallWasteRecord> pageInfo = new PageInfo<SmallWasteRecord>(list);
        PageUtils<SmallWasteRecord> page = new PageUtils<SmallWasteRecord>(list,(int)pageInfo.getTotal(),pageSize,pageNo);
        return page;
    }
}

