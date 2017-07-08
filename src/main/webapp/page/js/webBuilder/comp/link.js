function Link() {
	this.getHtml = function(){
		var html = '<a href="#" style="display:inline-block;">链接描述</a>';
		
		return html;
	}
	this.attrs = [{
		name : 'text',
		text : '文本内容',
		value : ''
	},{
		name : 'dataHref',
		text : '链接地址',
		value : ''
	},{
		name : 'target',
		text : '打开方式',
		value : '_blank',
	},{
		name : 'title',
		text : '悬停提示',
		value : ''
	}];
	this.css = [ {
		name : 'height',
		text : '高度',
	},{
		name : 'line-height',
		text : '行高',
	},{
		name : 'width',
		text : '宽度',
	},{
		name : 'text-align',
		text : '对齐',
	},{
		name : 'padding',
		text : '内边距',
	},{
		name : 'border',
		text : '边框',
	},{
		name : 'background-color',
		text : '背景色',
	},{
		name : 'font-family',
		text : '字体',
	},{
		name : 'font-weight',
		text : '字体大小',
	}];
	this.defaultSrc = 'page/img/xiong.jpg';
	
}