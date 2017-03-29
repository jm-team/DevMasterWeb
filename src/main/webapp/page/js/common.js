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

//表单FORM序列化成json
$.fn.serializeJson=function(){
	var serializeObj={};
	var array=this.serializeArray();
	$(array).each(function(){
		if(serializeObj[this.name]){
			if($.isArray(serializeObj[this.name])){
				serializeObj[this.name].push(this.value);
			}else{
				serializeObj[this.name]=[serializeObj[this.name],this.value];
			}
		}else{
			serializeObj[this.name]=this.value;
		}
	});
	return serializeObj;
};

function validateForm(formId) {
	if (formId == null || formId == undefined || formId === '') {
		return true;
	}

	var msg = '';
	var elements = $('#' + formId + ' :input');

	$.each(elements, function(index, ele) {
		var text = '';
		var element = $(ele);

		if (ele.tagName == 'TEXTAREA') {
			text = element.text();
		} else {
			text = element.val();
		}

		if (hasAttr(element, 'data-null-msg')
				&& element.attr('data-null-msg') != '' && text == '') {
			msg += element.attr('data-null-msg') + '<br />';
		} else {
			if (hasAttr(element, 'data-min-length')
					&& element.attr('data-min-length') != ''
					&& element.attr('data-min-length').indexOf(',') > -1) {
				var minArray = element.attr('data-min-length').split(',', 2);
				var minLength = parseInt(minArray[0]);

				if (!isNaN(minLength) && text.length < minLength) {
					msg += minArray[1] + '<br />';
				}
			}
		}

		if (hasAttr(element, 'data-mobile-msg')
				&& element.attr('data-mobile-msg') != ''
				&& text != ''
				&& !/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/.test(text)) {
			msg += element.attr('data-mobile-msg') + '<br />';
		}
	});

	if (msg != '') {
		layer.msg(msg);
		return false;
	}

	return true;
}

function hasAttr(ele, name) {
	return (ele.attr(name) != null && ele.attr(name) != undefined);
}