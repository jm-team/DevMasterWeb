function Image() {
	this.getHtml = function(){
		var html = '<img src="'+this.defaultSrc+'" style="display:inline-block;height:150px;width:150px;">';
		
		return html;
	}
	this.attrs = [{
		name : 'src',
		text : '图片地址',
		value : ''
	},{
		name : 'title',
		text : '悬停提示',
		value : ''
	}];
	this.css = [ {
		name : 'height',
		text : '高度',
	},{
		name : 'width',
		text : '宽度',
	},{
		name : 'padding',
		text : '内边距',
	},{
		name : 'border',
		text : '边框',
	},{
		name : 'background-color',
		text : '背景色',
	}];
	this.defaultSrc = 'img/xiong.jpg';
	
}