package com.icloud.modules.small.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import com.icloud.basecommon.model.Query;
import com.icloud.modules.shop.entity.Shop;
import com.icloud.modules.shop.service.ShopService;
import com.icloud.modules.small.entity.SmallSku;
import com.icloud.modules.small.service.SmallSkuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.icloud.modules.small.entity.SmallGroupShop;
import com.icloud.modules.small.service.SmallGroupShopService;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;


/**
 * 
 *
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-09-18 09:08:35
 * 菜单主连接： modules/small/smallgroupshop.html
 */
@RestController
@RequestMapping("small/smallgroupshop")
public class SmallGroupShopController {
    @Autowired
    private SmallGroupShopService smallGroupShopService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SmallSkuService smallSkuService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("small:smallgroupshop:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = smallGroupShopService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("small:smallgroupshop:info")
    public R info(@PathVariable("id") Long id){
        SmallGroupShop smallGroupShop = (SmallGroupShop)smallGroupShopService.getById(id);
        Shop shop = new Shop();
        if(smallGroupShop.getSupplierId()!=null){
            shop = (Shop) shopService.getById(smallGroupShop.getSupplierId());
        }
        smallGroupShop.setShop(shop);
        SmallSku sku = new SmallSku();
        if(smallGroupShop.getSkuId()!=null){
            sku = (SmallSku) smallSkuService.getById(smallGroupShop.getSkuId());
        }
        smallGroupShop.setSku(sku);
        return R.ok().put("smallGroupShop", smallGroupShop);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("small:smallgroupshop:save")
    public R save(@RequestBody SmallGroupShop smallGroupShop){
        ValidatorUtils.validateEntity(smallGroupShop);
        smallGroupShop.setCreateTime(new Date());
        smallGroupShopService.save(smallGroupShop);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("small:smallgroupshop:update")
    public R update(@RequestBody SmallGroupShop smallGroupShop){
        ValidatorUtils.validateEntity(smallGroupShop);
        smallGroupShop.setModifyTime(new Date());
        smallGroupShopService.updateById(smallGroupShop);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("small:smallgroupshop:delete")
    public R delete(@RequestBody Long[] ids){
        smallGroupShopService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
