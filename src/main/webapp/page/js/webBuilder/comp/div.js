function Div() {
	this.getHtml = function(){
		var html = '<div style="height:200px;width:400px;" class="droppable"></div>';
		return html;
	}
	this.attrs = [];
	this.css = [ {
		name : 'position',
		text : '定位',
	},{
		name : 'height',
		text : '高度',
	},{
		name : 'line-height',
		text : '行高',
	},{
		name : 'width',
		text : '宽度',
	},{
		name : 'color',
		text : '文字颜色',
	},{
		name : 'font-size',
		text : '字体大小',
	},{
		name : 'padding',
		text : '内边距',
	},{
		name : 'text-align',
		text : '显示方式',
	},{
		name : 'background-color',
		text : '背景色',
	},{
		name : 'margin',
		text : '外边距',
	}];
}