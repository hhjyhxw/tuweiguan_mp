package com.icloud.api.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuerySkuCategoryVo {
    private Long[] skuId;//商品id
    private Long supplierId;//商户id
}
