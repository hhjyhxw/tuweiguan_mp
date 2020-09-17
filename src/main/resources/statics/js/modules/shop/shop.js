$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'shop/shop/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '上级店铺ID', name: 'parentId', index: 'parent_id', width: 80 }, 			
			{ label: '编码', name: 'shopCode', index: 'shop_code', width: 80 }, 			
			{ label: '名称', name: 'shopName', index: 'shop_name', width: 80 }, 			
			{ label: '系统店铺标志', name: 'sysFlag', index: 'sys_flag', width: 80 }, 			
			{ label: '级别', name: 'shopLevel', index: 'shop_level', width: 80 }, 			
			{ label: '电话', name: 'shopTel', index: 'shop_tel', width: 80 }, 			
			{ label: '图片', name: 'shopImg', index: 'shop_img', width: 80 }, 			
			{ label: '简介', name: 'description', index: 'description', width: 80 }, 			
			{ label: '余额', name: 'balance', index: 'balance', width: 80 }, 			
			{ label: '店铺地址', name: 'untitled4', index: 'untitled4', width: 80 }, 			
			{ label: '省', name: 'province', index: 'province', width: 80 }, 			
			{ label: '市', name: 'city', index: 'city', width: 80 }, 			
			{ label: '县', name: 'county', index: 'county', width: 80 }, 			
			{ label: '详细地址', name: 'address', index: 'address', width: 80 }, 			
			{ label: '经度', name: 'lnt', index: 'lnt', width: 80 }, 			
			{ label: '纬度', name: 'lat', index: 'lat', width: 80 }, 			
			{ label: '覆盖范围(米)', name: 'coverScope', index: 'cover_scope', width: 80 }, 			
			{ label: '状态 0：关闭，1：开启', name: 'status', index: 'status', width: 80 }, 			
			{ label: '审核 0：未审核，1：审核通过，2：审核失败', name: 'review', index: 'review', width: 80 }, 			
			{ label: '创建人', name: 'createdBy', index: 'created_by', width: 80 }, 			
			{ label: '创建时间', name: 'createdTime', index: 'created_time', width: 80 }, 			
			{ label: '更新人', name: 'updatedBy', index: 'updated_by', width: 80 }, 			
			{ label: '更新时间', name: 'updatedTime', index: 'updated_time', width: 80 }			
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
		shop: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.shop = {};
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
                var url = vm.shop.id == null ? "shop/shop/save" : "shop/shop/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.shop),
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
                        url: baseURL + "shop/shop/delete",
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
			$.get(baseURL + "shop/shop/info/"+id, function(r){
                vm.shop = r.shop;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});