function Span() {
	this.getHtml = function(){
		var html = '<span class="elem ui-widget-content" style="style:inline-block;height: 30px;line-height: 30px;width: 80px;text-align: center;cursor: move;">请输入内容</span>';
		return html;
	};
	this.attrs = [{
		name : 'text',
		text : '文本内容',
		value : '请输入内容'
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
		name : 'color',
		text : '文字颜色',
	},{
		name : 'background-color',
		text : '背景色',
	},{
		name : 'font-size',
		text : '字体大小',
	},{
		name : 'padding',
		text : '内边距',
	}];
}