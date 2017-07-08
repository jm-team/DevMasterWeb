function Input() {
	this.getHtml = function(){
		var html = '<span class="wrap elem ui-widget-content" style="cursor: move;padding:5px;"><input style="display:inline-block;height: 30px;line-height: 30px;width: 200px;padding-left: 10px;" placeholder="请输入内容"></input></span>';
		return html;
	};
	this.attrs = [{
		name : 'placeholder',
		text : '提示内容',
		value : '请输入内容'
	},{
		name : 'name',
		text : '字段名',
		value : 'field1'
	},{
		name : 'maxLength',
		text : '最大长度',
		value : ''
	},{
		name : 'minLength',
		text : '最小长度',
		value : ''
	}];
	this.css = [ {
		name : 'height',
		text : '高度',
	},{
		name : 'line-height',
		text : '行高',
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