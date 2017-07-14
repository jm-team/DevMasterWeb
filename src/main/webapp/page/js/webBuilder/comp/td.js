function Td() {
	this.getHtml = function(){
		var html = '<td style="height:40px;width:50px;"></td>';
		return html;
	}
	this.attrs = [];
	this.css = [ {
		name : 'height',
		text : '高度',
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
	}];
}