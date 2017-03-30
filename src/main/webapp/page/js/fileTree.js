var serviceName='';
var projectName='';
function initExtensionClassMapper(){
	var extensionClassMapper = {};
    extensionClassMapper['folder']  = 'my-tree-folder-icon';
    extensionClassMapper['html']    = 'my-tree-html-icon';
    extensionClassMapper['js']      = 'my-tree-js-icon';
    extensionClassMapper['css']     = 'my-tree-css-icon';
    extensionClassMapper['xml']     = 'my-tree-xml-icon';
    extensionClassMapper['sql']     = 'my-tree-sql-icon';
    extensionClassMapper['default'] = 'my-tree-default-icon';
    
    extensionClassMapper['jpg']     = 'my-tree-picture-icon';
    extensionClassMapper['png']     = 'my-tree-picture-icon';
}


function getIconClass(fileName, isFile){
	if(!isFile){
		return extensionClassMapper['folder'];
	}
	
	if(fileName == null || fileName == undefined || fileName == '' || fileName.lastIndexOf('.') == -1){
		return extensionClassMapper['default'];
	}
	
	var extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
	var iconClass = extensionClassMapper[extension];
	
	if(iconClass == null || iconClass == undefined || iconClass == ''){
		iconClass = extensionClassMapper['default'];
	}
	
	return iconClass;
}


/**
 * 双击文件后，打开tab
 * 如果已经打开，则选中
 */
function onNodeDblClick(node){
	if(!node.folder){
		openCodeFrame(node.id, node.text);
	}
}

/**
 * 双击文件后，打开tab
 * 如果已经打开，则选中
 */
function openCodeFrame(path, name){
	var ele = $('#contentTabs');
	var tabs = ele.tabs('tabs');
	
	for(var i = 0; i < tabs.length; i++){
		if(tabs[i][0].id == path){
			var index = ele.tabs('getTabIndex', tabs[i]);
			ele.tabs('select', index);
			return;
		}
	}
	
	var imgExtends = ['jpg', 'png'];
	var extend = name.substring(name.lastIndexOf('.') + 1);
	
	if(jQuery.inArray(extend, imgExtends) > 0){
	
		ele.tabs('add', {
    		id : path,
    		title : name,
    		selected : true,
    		closable : true,
            content : '<div><img src="'+serviceName+'/project/picture?path=' + path + '"></div>'
    	});
		
		return;
	}
	
	ele.tabs('add', {
		id : path,
		title : name,
		selected : true,
		closable : true,
		content : '<iframe src="'+serviceName+'/code/viewFile?projectName='+projectName+'&path='+path+'" width="100%" height="98%"></iframe>'
	});
}