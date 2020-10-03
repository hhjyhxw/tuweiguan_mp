var editor1;
KindEditor.ready(function(K) {
    editor1 = K.create('textarea[name="detail"]',{
            //参数配置
            width : '95%',
            filePostName: "file",
            uploadJson:  baseURL + "sys/oss/uploadFrontFoylay",
            minHeight: '450',
            resizeType : 0,//禁止拉伸，1可以上下拉伸，2上下左右拉伸
            filterMode: false,//true时过滤HTML代码，false时允许输入任何代码。
            itmes:  [
                'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
                'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                'anchor', 'link', 'unlink', '|', 'about'
            ]
        }

    );
    //  prettyPrint();
});

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'small/smallcoupon/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '优惠券名称', name: 'title', index: 'title', width: 80 },
			/*{ label: '使用类型，如满减', name: 'coupType', index: 'coup_type', width: 80 }, 	*/
			/*{ label: '描述', name: 'description', index: 'description', width: 80 },*/
			{ label: '发行总数', name: 'total', index: 'total', width: 80 },
            { label: '已领取数', name: 'total', index: 'total', width: 80 },
            { label: '优惠券类型', name: 'orderStatus', width: 60, formatter: function(value, options, row){
                    return value === 0 ?
                        '<span class="label label-danger">默认类型</span>' :
                        (value===1?'<span class="label label-success">新用户专用</span>':
                            '其他');
                }},
			{ label: '每人限领', name: 'limits', index: 'limits', width: 80 },
			{ label: '满多少（元）', name: 'min', index: 'min', width: 80 },
            { label: '减多少（元）', name: 'discount', index: 'discount', width: 80 },
			{ label: '是否可用 0不用 1可用', name: 'status', index: 'status', width: 80 }, 			
			{ label: '可用分类', name: 'categoryId', index: 'category_id', width: 80 },
		/*	{ label: '过期天数', name: 'days', index: 'days', width: 80 },
			{ label: '领取开始时间', name: 'startTime', index: 'start_time', width: 80 }, 			
			{ label: '领取/使用结束时间', name: 'endTime', index: 'end_time', width: 80 }, 	*/
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80 }, 			
			/*{ label: '商户id', name: 'supplierId', index: 'supplier_id', width: 80 },
			{ label: '企业Id', name: 'deptId', index: 'dept_id', width: 80 }			*/
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
		smallCoupon: {
            coupType:1,
            surplus:0,
            startTime:null,
            endTime:null,
        },
        shopList:[],
        shopName:'',
        q:{
            name:'',
        },
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
			vm.smallCoupon = {
                coupType:1,
                surplus:0,
                startTime:null,
                endTime:null,
            };
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
            vm.smallCoupon.description=editor1.html();
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.smallCoupon.id == null ? "small/smallcoupon/save" : "small/smallcoupon/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.smallCoupon),
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
                        url: baseURL + "small/smallcoupon/delete",
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
			$.get(baseURL + "small/smallcoupon/info/"+id, function(r){
                vm.smallCoupon = r.smallCoupon;
                editor1.html(vm.smallCoupon.description);
                vm.deptId = r.smallCoupon.deptId;
                vm.setDeptName(vm.deptId);
                vm.getShopList(r.smallCoupon.supplierId);
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        //加载getShopList
        getShopList:function(id){
            console.log("id======"+id)
            $.ajaxSettings.async = false;
            $.get(baseURL + "shop/shop/selectlist?deptId="+vm.deptId, function(r){
                vm.shopList = r.list;
                if(id!=null && id!=''){
                    vm.setShopName(vm.smallCoupon.supplierId);
                }
            });
            $.ajaxSettings.async = true;
        },
        //选择卡店铺
        selectShop: function (index) {
            vm.smallCoupon.supplierId = vm.shopList[index].id;
            vm.shopName = vm.shopList[index].shopName;
        },
        setShopName:function(shopId){
            if(vm.shopList!=null && vm.shopList.length>0 && shopId!=null){
                vm.shopList.forEach(p=>{
                    if(p.id===shopId){
                        vm.shopName = p.shopName;
                    }
                });
            }
        },
        //加载企业列表
        getDeptList:function(){
            $.get(baseURL + "/sys/dept/selectlist", function(r){
                vm.deptList = r.deptList;
            });
        },
        //选择企业
        selectDept: function (index) {
            vm.smallCoupon.deptId = vm.deptList[index].deptId;
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
	}
});