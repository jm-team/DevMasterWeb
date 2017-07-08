function Select() {
	this.getHtml = function(){
		var html = '<span class="wrap elem ui-widget-content" style="cursor: move;padding:5px;"><select style="display:inline-block;height: 30px;padding:0px 10px;width:100px;">';
		var elem = $(html);
		for(var i=0;i<this.defaultData.length;i++){
			var option = this.defaultData[i];
			var str = '<option value="'+option.value+'">'+option.text+'</option>';
			html += str;
		}
		html +='</select></span>';
		return html;
	}
	this.attrs = [{
		name : 'name',
		text : '字段名称',
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
	this.defaultData=[{
		value:'val1',
		text : '选项一'
	},{
		value:'val2',
		text : '选项二'
	}];
	this.dataUrl = '';
	this.setDefaultData = function(value){
		var elem = CompUtils.getEditElem();
		
		var arr;
		try{
			arr = JSON.parse(value);
		}catch(ex){
			alert('数据格式不正确');
		}
		elem.empty();
		if(!arr){
			return;
		}
		for(var i=0;i<arr.length;i++){
			var option = arr[i];
			var html = '<option value="'+option.value+'">'+option.text+'</option>';
			elem.append(html);
		}
	};
	this.setDataUrl = function(dataUrl){

	};
	this.hasData = true;
	this.getCurrentValue = function(comp){
		var options = $(comp).find('option');
		var arr = [];
		for(var i=0;i<options.length;i++){
			var opt = $(options[i]);
			var json = JSON.parse('{}');
			json.value=opt.val();
			json.text = opt.text();
			arr.push(json);
		}
		if(arr.length==0){
			return this.defaultData;
		}
		return arr;
	}
}