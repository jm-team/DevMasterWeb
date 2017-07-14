function Table() {
	this.getHtml = function(){
		var html = '<table accept="tr" class="default-table droppable">';
		for(var i=0;i<2;i++){
			html+= this.createRow();
		}
		html += '</table>';
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
	}];
	this.data = [{
		name : 'cols',
		text : '列数'
	},{
		name : 'dataUrl',
		text : '数据接口'
	}];
	this.cols = 4;
	this.createRow = function(){
		var row = '<tr>';
		for(var i=0;i<this.cols;i++){
			row += '<td onclick="clickSpecificElem(this);" class="default-td droppable"></td>';
		}
		row += '</tr>';
		return row;
	}
	
	this.setData = function(key , value , targetElem){
		if(key=='cols'){
			var currentCols = $(targetElem).find('tr').first().find('td').length;
			if(value>currentCols){
				// add columns
				var td = '<td onclick="clickSpecificElem(this);" class="default-td droppable"></td>';
				for(var i=0;i<value-currentCols;i++){
					$(targetElem).find('tr').append(td);
				}
			}else{
				//remove columns
				for(var i=0;i<currentCols-value;i++){
					var trs = $(targetElem).find('tr');
					for(var j=0;j<trs.length;j++){
						$(trs[j]).find('td').last().remove();
					}
				}
			}
		}
	}
}