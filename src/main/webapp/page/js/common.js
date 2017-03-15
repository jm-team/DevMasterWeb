var serviceName="";

/**
 * 获取根路径
 * 
 * @returns
 */
function getRootName() {
	var pathName = window.document.location.pathname;
	var projectName = pathName
			.substring(0, pathName.substr(1).indexOf('/') + 1);
	return projectName;
}

/**
 * 获取跟路径URL
 * 
 * @returns
 */
function getRootPath() {
	var curWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPaht = curWwwPath.substring(0, pos);	
	return (localhostPaht + serviceName);
}

/**
 * 获取配置中心管理页面接口路径前缀
 * @returns {String}
 */
function getDisconfMgrPath() {
	return getRootPath() + "/disconf/mgr";
}

/**
 * 获取配置中心api接口路径前缀
 * @returns {String}
 */
function getDisconfApiPath() {
	return getRootPath() + "/disconf/api";
}


function openPage(url){
	$.get(url, function(data) {
		$(".main-content").html(data);
	});
}

function closeWindowAndRefreshParent(){
	window.parent.listData(0); //刷新父页面
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.close(index);  // 关闭layer
}

function createPager(pageId , totalPages ,onchange){
	pager = $.jqPaginator(pageId, {
		totalPages: totalPages,
        visiblePages: 3,
        currentPage: 1,
        prev: '<li class="prev"><a href="javascript:;">上一页</a></li>',
        next: '<li class="next"><a href="javascript:;">下一页</a></li>',
        page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
        last: '<li class="page"><a href="javascript:;">{{totalPages}}</a></li>',
        onPageChange: function (num, type) {
        	if('init'!=type){
        		onchange(num,type);
        	}
        }
    });
	return pager;
}