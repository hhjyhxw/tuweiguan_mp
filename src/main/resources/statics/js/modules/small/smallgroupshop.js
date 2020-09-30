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
        user: {
            userId:null
        },
        deptId:null,
        deptList:[],
        deptName:'',
	},
    created: function(){
        this.getUser();
        this.getDeptList();
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
            vm.deptName = '',
            vm.deptId = null,
            vm.shopName = null;
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
                vm.deptId = r.smallGroupShop.deptId;
                vm.setDeptName(vm.deptId);
                vm.getShopList(r.smallGroupShop.supplierId);
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
        getShopList:function(id){
            $.get(baseURL + "shop/shop/selectlist?deptId="+vm.deptId, function(r){
                vm.shopList = r.list;
                if(id!=null && id!=''){
                    vm.setShopName(vm.smallGroupShop.supplierId);
                }
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
        //选择自营或者公共商品
        selectCommonFlag: function (commonFlag) {
		    if(commonFlag==0){
		        vm.smallGroupShop.commonFlag = 0;
                //vm.getGoodsList(vm.smallGroupShop.supplierId,false);//加载店铺sku列表
            }else {
                vm.smallGroupShop.commonFlag = 1;
                //vm.getGoodsList(vm.smallGroupShop.supplierId,true);//加载店铺sku列表
            }
            vm.goodName = '';
            vm.smallGroupShop.spuId = null;
            vm.smallGroupShop.skuId = null;

        },
        //加载商品列表
        getGoodsList:function(supplierId,sysFlag){
            $.get(baseURL + "small/smallsku/skulistForGroup?supplierId="+supplierId+"&sysFlag="+sysFlag, function(r){
                vm.skuList = r.list;
            });
        },
        //选择sku
        selectSku_bak: function (index) {
		    var goods = vm.skuList[index];
            vm.goodName = goods.title;
            vm.smallGroupShop.spuId = goods.spuId;
            vm.smallGroupShop.skuId = goods.id;
            vm.smallGroupShop.minPrice = goods.price;//团购价
            vm.smallGroupShop.maxPrice = goods.originalPrice;//原价
        },
        //加载企业列表
        getDeptList:function(){
            $.get(baseURL + "/sys/dept/selectlist", function(r){
                vm.deptList = r.deptList;
            });
        },
        //选择企业
        selectDept: function (index) {
            vm.smallGroupShop.deptId = vm.deptList[index].deptId;
            vm.deptName = vm.deptList[index].name;
            vm.deptId = vm.deptList[index].deptId;
            vm.getShopList();
        },
        setDeptName:function(deptId){
            if(vm.deptList!=null && vm.deptList.length>0 && deptId!=null){
                vm.deptList.forEach(p=>{
                    if(p.deptId===deptId){
                        vm.deptName = p.name;
                    }
                });
            }
        },
        //获取用户信息
        getUser: function(){
            $.getJSON(baseURL+"sys/user/info?_"+$.now(), function(r){
                vm.user = r.user;
            });
        },
        //打开添加sku弹窗 选择需要上团购 是航拍
        selectSku: function () {
            if(vm.smallGroupShop.commonFlag===0){
                sysFlag = false;
            }else {
                sysFlag = true;
            }
            this.skuWinIndex = layer.open({
                title: '选择sku',
                type: 2,
                maxmin: true,
                move:true,
                shadeClose: true,
                area: ['65%', '65%'],
                btn: ['<i class="fa fa-check"></i> 确定', '<i class="fa fa-close"></i> 关闭'],
                content: baseURL + "modules/small/smallskuForGroup.html?supplierId="+vm.smallGroupShop.supplierId+"&sysFlag="+sysFlag,
                yes: function (index, layero) {
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    var smallSku = iframeWin.vm.smallSku;
                    if($.trim(smallSku.id) == '') {
                        layer.msg("请选择sku",{icon: 0,time: 1000});return;
                    }
                    console.log(smallSku);
                    vm.smallGroupShop.spuId = smallSku.spuId;
                    vm.smallGroupShop.skuId = smallSku.id;
                    vm.smallGroupShop.minPrice = smallSku.price;//团购价
                    vm.smallGroupShop.maxPrice = smallSku.originalPrice;//原价
                    layer.close(index);
                },
                success: function (layero, index) {
                    /*var info = '<font color="red" class="pull-left mt10">提示：双击可快速选择。</font>';
                    layero.find('.layui-layer-btn').append(info);*/
                }
            });
        },

        skuforgroupWinDblClick: function (smallSku) {
            vm.goodName = smallSku.title;
            vm.smallGroupShop.spuId = smallSku.spuId;
            vm.smallGroupShop.skuId = smallSku.id;
            vm.smallGroupShop.minPrice = smallSku.price;//团购价
            vm.smallGroupShop.maxPrice = smallSku.originalPrice;//原价
            layer.close(vm.skuWinIndex);
        },
	}
});
vm.getShopList();