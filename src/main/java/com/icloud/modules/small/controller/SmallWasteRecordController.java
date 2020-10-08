package com.icloud.modules.small.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.icloud.annotation.DataFilter;
import com.icloud.basecommon.model.Query;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.icloud.modules.small.entity.SmallWasteRecord;
import com.icloud.modules.small.service.SmallWasteRecordService;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.modules.sys.controller.AbstractController;


/**
 * 资金流水记录表
 *
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-10-07 20:18:12
 * 菜单主连接： modules/small/smallwasterecord.html
 */
@RestController
@RequestMapping("small/smallwasterecord")
public class SmallWasteRecordController extends AbstractController{
    @Autowired
    private SmallWasteRecordService smallWasteRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("small:smallwasterecord:list")
    @DataFilter
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = smallWasteRecordService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/shenhelist")
    @RequiresPermissions("small:smallwasterecord:shenhelist")
    @DataFilter
    public R shenhelist(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        query.put("wasteFlag","2");//提现类型
        PageUtils page = smallWasteRecordService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("small:smallwasterecord:info")
    public R info(@PathVariable("id") Long id){
        SmallWasteRecord smallWasteRecord = (SmallWasteRecord)smallWasteRecordService.getById(id);

        return R.ok().put("smallWasteRecord", smallWasteRecord);
    }
    /**
     * 用于提现审核信息
     */
    @RequestMapping("/shenheinfo/{id}")
    @RequiresPermissions("small:smallwasterecord:shenheinfo")
    public R shenheinfo(@PathVariable("id") Long id){
        SmallWasteRecord smallWasteRecord = (SmallWasteRecord)smallWasteRecordService.getById(id);

        return R.ok().put("smallWasteRecord", smallWasteRecord);
    }

    /**
     * 审核通过 或者不通过
     */
    @RequestMapping("/shenhe")
    @RequiresPermissions("small:smallwasterecord:shenhe")
    public R shenhe(@RequestBody SmallWasteRecord smallWasteRecord){
        ValidatorUtils.validateEntity(smallWasteRecord);
        smallWasteRecord.setModifyTime(new Date());
        smallWasteRecord.setApproveBy(getUser().getUsername());
        smallWasteRecord.setApproveTime(new Date());
        //判断是否审核通过，通过发起企业付款
        smallWasteRecordService.updateById(smallWasteRecord);

        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("small:smallwasterecord:save")
    public R save(@RequestBody SmallWasteRecord smallWasteRecord){
        smallWasteRecordService.save(smallWasteRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("small:smallwasterecord:update")
    public R update(@RequestBody SmallWasteRecord smallWasteRecord){
        ValidatorUtils.validateEntity(smallWasteRecord);
        smallWasteRecordService.updateById(smallWasteRecord);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("small:smallwasterecord:delete")
    public R delete(@RequestBody Long[] ids){
        smallWasteRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
