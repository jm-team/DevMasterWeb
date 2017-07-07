function Input() {
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