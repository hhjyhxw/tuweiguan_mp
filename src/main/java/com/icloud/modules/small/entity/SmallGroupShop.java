package com.icloud.modules.small.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 团购商品
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-09-18 09:08:35
 */
@Data
@TableName("t_small_group_shop")
public class SmallGroupShop implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /*  */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 关联商品id */
       @TableField("spu_id")
       private Long spuId;
   	   	   /* 团购价 */
       @TableField("min_price")
       private Integer minPrice;
   	   	   /* 单买价 */
       @TableField("max_price")
       private Integer maxPrice;
   	   	   /* 团购开始时间 */
       @TableField("gmt_start")
       private Date gmtStart;
   	   	   /* 团购结束时间 */
       @TableField("gmt_end")
       private Date gmtEnd;
   	   	   /* 团购基础人数 */
       @TableField("minimum_number")
       private Integer minimumNumber;
   	   	   /* 团购已经购买人数 */
       @TableField("already_buy_number")
       private Integer alreadyBuyNumber;
   	   	   /* 团购结束时购买人数未达到基础人数,是否自动退款（0 不自动退 1自动退款） */
       @TableField("automatic_refund")
       private Integer automaticRefund;
   	   	   /* 判断团购商品是否在活动期间（0 停用 1使用） */
       @TableField("status")
       private Integer status;
   	   	   /* 创建时间 */
       @TableField("create_time")
       private Date createTime;
   	   	   /* 修改时间 */
       @TableField("modify_time")
       private Date modifyTime;
   	   	   /* 商户id */
       @TableField("supplier_id")
       private Long supplierId;
   	
}
