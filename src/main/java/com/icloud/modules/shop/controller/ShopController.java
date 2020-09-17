package com.icloud.modules.shop.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.icloud.basecommon.model.Query;
import com.icloud.modules.small.entity.SmallRetail;
import com.icloud.modules.small.vo.RetailVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.icloud.modules.shop.entity.Shop;
import com.icloud.modules.shop.service.ShopService;
import com.icloud.basecommon.model.Query;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.validator.ValidatorUtils;


/**
 * 店铺 
 *
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-09-17 16:07:50
 * 菜单主连接： modules/shop/shop.html
 */
@RestController
@RequestMapping("shop/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("shop:shop:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        PageUtils page = shopService.findByPage(query.getPageNum(),query.getPageSize(), query);

        return R.ok().put("page", page);
    }

    /**
     * 选择所属店铺
     */
    @RequestMapping("/select")
    @RequiresPermissions("shop:shop:update")
    public R select(){
        List<Shop> retailList = shopService.list();
        List<RetailVo> list =  new ArrayList<RetailVo>();
        RetailVo vo = null;
        if(list!=null){
            for (Shop shop : retailList) {
                vo =  new RetailVo();
                vo.setId(shop.getId());
                vo.setName(shop.getShopName());
                vo.setParentId(null);
                vo.setParentName(null);
                list.add(vo);
            }
        }
        return R.ok().put("retailList", list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("shop:shop:info")
    public R info(@PathVariable("id") Long id){
        Shop shop = (Shop)shopService.getById(id);

        return R.ok().put("shop", shop);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("shop:shop:save")
    public R save(@RequestBody Shop shop){
        shopService.save(shop);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("shop:shop:update")
    public R update(@RequestBody Shop shop){
        ValidatorUtils.validateEntity(shop);
        shopService.updateById(shop);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("shop:shop:delete")
    public R delete(@RequestBody Long[] ids){
        shopService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
