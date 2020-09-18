package com.icloud.modules.small.vo;

import lombok.Data;

@Data
public class ShopTreeVo {
    private Long id;
    private Long parentId;
    private String name;
    private String parentName;

}
