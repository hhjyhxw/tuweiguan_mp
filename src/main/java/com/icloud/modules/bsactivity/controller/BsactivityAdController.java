package com.icloud.modules.bsactivity.controller;

import com.icloud.annotation.DataFilter;
import com.icloud.annotation.SysLog;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.modules.bsactivity.entity.BsactivityAd;
import com.icloud.modules.bsactivity.service.BsactivityAdService;
import com.icloud.modules.shop.entity.Shop;
import com.icloud.modules.shop.service.ShopService;
import com.icloud.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 
 *
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-04-26 16:19:44
 * 菜单主连接： modules/bsactivity/bsactivityad.html
 */
@RestController
@RequestMapping("bsactivity/bsactivityad")
public class BsactivityAdController extends AbstractController {
    @Autowired
    private BsactivityAdService bsactivityAdService;
    @Autowired
    private ShopService shopService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("bsactivity:bsactivityad:list")
    @DataFilter
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = bsactivityAdService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("bsactivity:bsactivityad:info")
    public R info(@PathVariable("id") Long id){
        BsactivityAd bsactivityAd = (BsactivityAd)bsactivityAdService.getById(id);
        Shop shop = new Shop();
        if(bsactivityAd.getSupplierId()!=null){
            shop = (Shop) shopService.getById(bsactivityAd.getSupplierId());
        }
        bsactivityAd.setShop(shop);

        return R.ok().put("bsactivityAd", bsactivityAd);
    }

    /**
     * 保存
     */
    @SysLog("保存广告")
    @RequestMapping("/save")
    @RequiresPermissions("bsactivity:bsactivityad:save")
    public R save(@RequestBody BsactivityAd bsactivityAd){
        Shop shop = (Shop) shopService.getById(bsactivityAd.getSupplierId());
        bsactivityAd.setDeptId(shop.getDeptId());
        bsactivityAdService.save(bsactivityAd);
        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改广告")
    @RequestMapping("/update")
    @RequiresPermissions("bsactivity:bsactivityad:update")
    public R update(@RequestBody BsactivityAd bsactivityAd){
        ValidatorUtils.validateEntity(bsactivityAd);
        Shop shop = (Shop) shopService.getById(bsactivityAd.getSupplierId());
        bsactivityAd.setDeptId(shop.getDeptId());
        bsactivityAdService.updateById(bsactivityAd);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除广告")
    @RequestMapping("/delete")
    @RequiresPermissions("bsactivity:bsactivityad:delete")
    public R delete(@RequestBody Long[] ids){
        bsactivityAdService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
