$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'small/smallgroupshop/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '关联商品id', name: 'spuId', index: 'spu_id', width: 80 }, 			
			{ label: '团购价', name: 'minPrice', index: 'min_price', width: 80 }, 			
			{ label: '单买价', name: 'maxPrice', index: 'max_price', width: 80 }, 			
			{ label: '团购开始时间', name: 'gmtStart', index: 'gmt_start', width: 80 }, 			
			{ label: '团购结束时间', name: 'gmtEnd', index: 'gmt_end', width: 80 }, 			
			{ label: '团购基础人数', name: 'minimumNumber', index: 'minimum_number', width: 80 }, 			
			{ label: '团购已经购买人数', name: 'alreadyBuyNumber', index: 'already_buy_number', width: 80 }, 			
			{ label: '团购结束时购买人数未达到基础人数,是否自动退款（0 不自动退 1自动退款）', name: 'automaticRefund', index: 'automatic_refund', width: 80 }, 			
			{ label: '判断团购商品是否在活动期间（0 停用 1使用）', name: 'status', index: 'status', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80 }, 			
			{ label: '商户id', name: 'supplierId', index: 'supplier_id', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#icloudapp',
	data:{
		showList: true,
		title: null,
		smallGroupShop: {},
        goodName:'',
        shopName:'',
        shopList:[],//店铺列表
        skuList :[],//对应店铺商品
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.smallGroupShop = {
                gmtStart:null,
                gmtEnd:null
            };
            vm.goodName='',
            vm.shopName='',
            vm.getShopList();
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.smallGroupShop.id == null ? "small/smallgroupshop/save" : "small/smallgroupshop/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.smallGroupShop),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "small/smallgroupshop/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "small/smallgroupshop/info/"+id, function(r){
                vm.smallGroupShop = r.smallGroupShop;
                vm.shopName = r.smallGroupShop.shop.shopName;
                vm.goodName = r.smallGroupShop.sku.title;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        //加载AttibutList
        getShopList:function(){
            $.get(baseURL + "shop/shop/selectlist", function(r){
                vm.shopList = r.list;
            });
        },
        //选择店铺
        selectShop: function (index) {
            vm.smallGroupShop.supplierId = vm.shopList[index].id;
            vm.shopName = vm.shopList[index].shopName;
            vm.getGoodsList(vm.shopList[index].id);//加载店铺sku列表
            vm.goodName = '';
            vm.smallGroupShop.spuId = null;
            vm.smallGroupShop.skuId = null;

        },
        //加载商品列表
        getGoodsList:function(supplierId){
            $.get(baseURL + "small/smallsku/skulistForGroup?supplierId="+supplierId, function(r){
                vm.skuList = r.list;
            });
        },
        //选择sku
        selectSku: function (index) {
		    var goods = vm.skuList[index];
            vm.goodName = goods.title;
            vm.smallGroupShop.spuId = goods.spuId;
            vm.smallGroupShop.skuId = goods.id;
            vm.smallGroupShop.minPrice = goods.price;//团购价
            vm.smallGroupShop.maxPrice = goods.originalPrice;//原价
        },

	}
});
vm.getShopList();