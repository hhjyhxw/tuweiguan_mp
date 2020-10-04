package com.icloud.api.small;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.AuthIgnore;
import com.icloud.annotation.LoginUser;
import com.icloud.basecommon.model.Query;
import com.icloud.basecommon.service.redis.RedisService;
import com.icloud.common.DateUtil;
import com.icloud.common.PageUtils;
import com.icloud.common.R;
import com.icloud.common.util.StringUtil;
import com.icloud.modules.bsactivity.entity.BsactivityAd;
import com.icloud.modules.bsactivity.service.BsactivityAdService;
import com.icloud.modules.shop.entity.Shop;
import com.icloud.modules.shop.service.ShopService;
import com.icloud.modules.small.entity.SmallAddress;
import com.icloud.modules.small.entity.SmallCoupon;
import com.icloud.modules.small.entity.SmallUserCoupon;
import com.icloud.modules.small.service.SmallCouponService;
import com.icloud.modules.small.service.SmallGroupShopService;
import com.icloud.modules.small.service.SmallSpuService;
import com.icloud.modules.small.service.SmallUserCouponService;
import com.icloud.modules.small.vo.GroupSkuVo;
import com.icloud.modules.small.vo.MycouponVo;
import com.icloud.modules.wx.entity.WxUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Api("优惠券相关接口")
@RestController
@RequestMapping("/api/coupon")
public class CouponApiController {

    @Autowired
    private BsactivityAdService bsactivityAdService;
    @Autowired
    private SmallGroupShopService smallGroupShopService;
    @Autowired
    private SmallSpuService smallSpuService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private SmallCouponService smallCouponService;
    @Autowired
    private SmallUserCouponService smallUserCouponService;



    /**
     * 店铺优惠券（）
     * 目前需要参数：
     *   pageNum //第几页
     *   pageSize  //每页条数
     *   supplierId //商户id (必填)
     * @return
     */
    @ApiOperation(value="店铺优惠券", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少记录", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "supplierId", value = "商户id", required = true, paramType = "query", dataType = "Long"),
    })
    @RequestMapping(value = "/shopcouponList",method = {RequestMethod.GET})
    @ResponseBody
    @AuthIgnore
    public R shopcouponList(String pageNum,String pageSize,@RequestParam Long supplierId) {
        //传入id为空则读取平台商品
        if(supplierId==null){
           return R.error("店铺id为空");
        }
        Query query = new Query(new HashMap<>());
        query.put("status",1);
        query.put("supplierId",supplierId);
        query.put("endTime",new Date());
        PageUtils<SmallCoupon> page = smallCouponService.findByPage(StringUtil.checkStr(pageNum)?Integer.parseInt(pageNum):1,
                StringUtil.checkStr(pageSize)?Integer.parseInt(pageSize):10,
                query);
        List<SmallCoupon> list = (List<SmallCoupon>) page.getList();
        if(list!=null && list.size()>0){
            list.forEach(p->{
//                p.setStartTime(DateUtil.getDateWithoutTime(DateUtil.commonFormatDate(p.getStartTime(),"yyyy-MM-dd HH:mm:ss")));
//                p.setEndTime(DateUtil.getDateWithoutTime(DateUtil.commonFormatDate(p.getEndTime(),"yyyy-MM-dd HH:mm:ss")));
                p.setStartTimeStr(DateUtil.commonFormatDateDo(p.getStartTime()));
                p.setEndTimeStr(DateUtil.commonFormatDateDo(p.getEndTime()));
            });
        }
        page.setList(list);
        return R.ok().put("page", page);
    }

//    public static void main(String[] args) {
//        Date data = new Date();
//        System.out.println(DateUtil.commonFormatDateDo(data));
//
//    }
    /**
     * 我的优惠券（）
     * 目前需要参数：
     *   status:0 未领取 1已领取 2已过期
     *   pageNum //第几页
     *   pageSize  //每页条数
     *   supplierId //商户id (必填)
     * @return
     */
    @ApiOperation(value="我的优惠券", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少记录", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "supplierId", value = "商户id", required = true, paramType = "query", dataType = "Long"),
    })
    @RequestMapping(value = "/mycouponList",method = {RequestMethod.GET})
    @ResponseBody
    public R mycouponList( @RequestParam String status,String pageNum, String pageSize, @RequestParam Long supplierId, @LoginUser WxUser user) {

        if(!StringUtil.checkStr(status)){
            status = "0";
        }
        Query query = new Query(new HashMap<>());
        query.put("shopId",supplierId);
        query.put("userId",user.getId());
        query.put("status",status);
        PageUtils<MycouponVo> page = smallUserCouponService.findByPageVo(StringUtil.checkStr(pageNum)?Integer.parseInt(pageNum):1,
                StringUtil.checkStr(pageSize)?Integer.parseInt(pageSize):10,
                query);
        List<MycouponVo> list = (List<MycouponVo>) page.getList();
        if(list!=null && list.size()>0){
            list.forEach(p->{
                if(p.getOrderId()==null && p.getEndTime().after(new Date())){
                    p.setStatus(0);//未领取
                }
                if(p.getOrderId()!=null){
                    p.setStatus(1);//已领取
                }
                if(p.getOrderId()==null && p.getEndTime().before(new Date())){
                    p.setStatus(2);//已过期
                }
                p.setStartTimeStr(DateUtil.commonFormatDateDo(p.getStartTime()));
                p.setEndTimeStr(DateUtil.commonFormatDateDo(p.getEndTime()));

            });
        }
        page.setList(list);
        return R.ok().put("page", page);
    }


    /**
     * 领取优惠券（）
     * 目前需要参数：
     *   supplierId //商户id (必填)
     *   couponId //优惠券id (必填)
     * @return
     */
    @ApiOperation(value="领取优惠券", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "supplierId", value = "商户id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "couponId", value = "优惠券id", required = true, paramType = "query", dataType = "Long"),
    })
    @RequestMapping(value = "/getCoupon",method = {RequestMethod.GET})
    @ResponseBody
    public R getCoupon( @RequestParam Long couponId, @RequestParam Long supplierId, @LoginUser WxUser user) {
        SmallCoupon smallCoupon = (SmallCoupon) smallCouponService.getById(couponId);
        List<SmallUserCoupon> totallist = smallUserCouponService.list(new QueryWrapper<SmallUserCoupon>()
                .eq("shop_id",supplierId)
                .eq("coupon_id",couponId));
        if(totallist!=null && totallist.size()>=smallCoupon.getTotal()){
            return R.error("已领取该优惠券已被领取完");
        }

        List<SmallUserCoupon> list = smallUserCouponService.list(new QueryWrapper<SmallUserCoupon>()
        .eq("user_id",user.getId())
        .eq("shop_id",supplierId)
        .eq("coupon_id",couponId));
        if(smallCoupon.getLimits()!=null && smallCoupon.getLimits()>0 && list!=null && list.size()>=smallCoupon.getLimits().intValue()){
            return R.error("已领取该优惠券不能再领取");
        }
        SmallUserCoupon userCoupon = new SmallUserCoupon();
        userCoupon.setCouponId(couponId);
        userCoupon.setShopId(smallCoupon.getSupplierId());
        userCoupon.setDeptId(smallCoupon.getDeptId());
        userCoupon.setUserId(user.getId().longValue());
        userCoupon.setCreateTime(new Date());
        if(smallCoupon.getValidateType().intValue()==0){
            userCoupon.setStartTime(new Date());
            Date newdate = DateUtil.getBeforeNDate(userCoupon.getStartTime(),smallCoupon.getDays());
            userCoupon.setEndTime(newdate);
        }else{
            userCoupon.setStartTime(smallCoupon.getStartTime());
            userCoupon.setEndTime(smallCoupon.getEndTime());
        }
        boolean result = smallUserCouponService.addUserCoupon(userCoupon,smallCoupon);
        return result?R.ok():R.error("领取失败");
    }

}
