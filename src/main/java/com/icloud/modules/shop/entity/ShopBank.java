package com.icloud.modules.shop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺银行卡 用于店铺提现
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-09-17 16:07:50
 */
@Data
@TableName("shop_bank")
public class ShopBank implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 银行卡ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 所属店铺 */
       @TableField("shop_id")
       private String shopId;
   	   	   /* 银行名称 */
       @TableField("bank_name")
       private String bankName;
   	   	   /* 支行名称 */
       @TableField("sub_branch")
       private String subBranch;
   	   	   /* 银行卡号 */
       @TableField("card_no")
       private String cardNo;
   	   	   /* 用户姓名 */
       @TableField("user_name")
       private String userName;
   	   	   /* 手机号 */
       @TableField("mobile")
       private String mobile;
   	   	   /* 状态 0：禁用，1：正常 */
       @TableField("status")
       private String status;
   	   	   /* 创建人 */
       @TableField("created_by")
       private String createdBy;
   	   	   /* 创建时间 */
       @TableField("created_time")
       private Date createdTime;
   	   	   /* 更新人 */
       @TableField("updated_by")
       private String updatedBy;
   	   	   /* 更新时间 */
       @TableField("updated_time")
       private Date updatedTime;
   	
}
