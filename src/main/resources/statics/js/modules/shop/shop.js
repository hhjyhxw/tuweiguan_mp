var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'shop/shop/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '上级店铺名称', name: 'parentName', index: 'parentName', width: 80 },
			{ label: '名称', name: 'shopName', index: 'shop_name', width: 80 },
            { label: '系统店铺', name: 'status', width: 60, formatter: function(value, options, row){
                    return value === '0' ?
                        '<span class="label label-danger">不是</span>' :
                        '<span class="label label-success">是</span>';
                }},
			{ label: '级别', name: 'shopLevel', index: 'shop_level', width: 80 }, 			
			{ label: '覆盖范围(米)', name: 'coverScope', index: 'cover_scope', width: 80 },
			{ label: '状态', name: 'status', width: 60, formatter: function(value, options, row){
					return value === '0' ?
						'<span class="label label-danger">关闭</span>' :
						'<span class="label label-success">开启</span>';
				}},
			{ label: '审核', name: 'review', width: 60, formatter: function(value, options, row){
					return value === '0' ?
						'<span class="label label-danger">未审核</span>' :
						(value==='1'?'<span class="label label-success">审核通过</span>':'审核失败');
				}},
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


    new AjaxUpload('#upload', {
        action: baseURL + "sys/oss/uploadFront",
        name: 'file',
        autoSubmit:true,
        responseType:"json",
        onSubmit:function(file, extension){
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))){
                alert('只支持jpg、png、gif格式的图片！');
                return false;
            }
        },
        onComplete : function(file, r){
            console.log("r=="+JSON.stringify(r));
            console.log("file=="+file);
            if(r.code == 0){
                alert("上传成功!");
                vm.shop.shopImg = r.url;
                console.log("vm.shop.shopImg=="+vm.shop.shopImg);
                //vm.reload();
            }else{
                alert(r.msg);
            }
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
            vm.getShopTree();
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id);

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
                vm.getShopTree();
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        //加载店铺树
        getShopTree: function(){
            //加载分类树
            $.get(baseURL + "shop/shop/select", function(r){
                // console.info("r==="+JSON.stringify(r))
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.retailList);
                // console.log("ztree====="+JSON.stringify(ztree))
                var node = ztree.getNodeByParam("id", vm.shop.parentId);
                console.log("加载node====="+JSON.stringify(node))
                if(node!=null){
                    ztree.selectNode(node);
                    vm.shop.parentName = node.name;
                }
            })
        },
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择父类",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    console.log("node====="+JSON.stringify(node))
                    vm.shop.parentId = node[0].id;
                    vm.shop.parentName = node[0].name;

                    layer.close(index);
                }
            });
        },
	}
});
