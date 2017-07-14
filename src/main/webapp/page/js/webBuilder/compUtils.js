var CompUtils = {
	setAttr:function(input){
		if(editElem.hasClass('wrap')){
			editElem = $(editElem.children()[0]);
		}
		var scope = input.attr('scope');
		var name = input.attr('name');
		if(scope=='attr'){
			if(name=='text'){
				editElem.text(input.val());
			}else{
				editElem.attr(name,input.val());
			}
				
		}
		if(scope=='css'){
			editElem.css(name,input.val());
		}
		if(scope=='data'){
			var comp = this.getCompByTagName(editElem[0].tagName);
			comp.setData(name , input.val() , editElem);
		}
	},
	getAttr:function(comp, name){
		
	},
	getCompByTagName : function(tag){
		if(tag=='INPUT'){
			return new Input();
		}
		if(tag=='SPAN'){
			return new Span();
		}
		if(tag=='SELECT'){
			return new Select();
		}
		if(tag=='IMG'){
			return new Image();
		}
		if(tag=='A'){
			return new Link();
		}
		if(tag=='TABLE'){
			return new Table();
		}
		if(tag=='TR'){
			return new Tr();
		}
		if(tag=='TD'){
			return new Td();
		}
		if(tag=='DIV'){
			return new Div();
		}
	},
	
	buildPropsEditor : function(elem){
		var comp = this.getCompByTagName(elem[0].tagName);
		var html = '';
		for(var i=0;i<comp.attrs.length;i++){
			var attr = comp.attrs[i];
			var value = elem.attr(attr.name);
			if(attr.name=='text'){
				value = elem.text();
			}
			if(!value){
				value='';
			}
			var row = '<div class="row"> <span class="label">'+attr.text+'</span><input scope="attr" name="'+attr.name+'" value="'+value+'"/></div><br/>';
			html += row;
		}
		for(var i=0;i<comp.css.length;i++){
			var css = comp.css[i];
			var value = elem.css(css.name);
			if(!value){
				value='';
			}
			var row = '<div class="row"> <span class="label">'+css.text+'</span><input class="propEdit" scope="css" name="'+css.name+'" value="'+value+'"/></div><br/>';
			html += row;
		}
		
		if(comp.data){
			for(var i=0;i<comp.data.length;i++){
				var data = comp.data[i];
				var row = '<div class="row"> <span class="label">'+data.text+'</span><input class="propEdit" scope="data" name="'+data.name+'" value=""/></div><br/>';
				html += row;
			}
		}
		
		if(comp.hasData){
			var staticData = comp.getCurrentValue(this.getEditElem());
			var row = '<div class="row"> <span class="label">静态数据</span><textarea class="propEdit" rows="5" onchange="CompUtils.setDefaultData(this)" scope="data" name="defaultData">'+JSON.stringify(staticData)+'</textarea></div><br/>';
			html += row;
			if(comp.dataUrl!=undefined){
				var row = '<div class="row"> <span class="label">数据接口</span><textarea class="propEdit" scope="data" onchange="CompUtils.setDataUrl(this)" name="dataUrl">'+comp.dataUrl+'</textarea></div><br/>';
				html += row;
			}	
		}
		
		return html;
	},
	setDefaultData : function(textarea){
		var value = textarea.value;
		var target = this.getEditElem();
		var comp = this.getCompByTagName(target[0].tagName);
		comp.setDefaultData(value);
	},
	setDataUrl : function(textarea){
		var value = textarea.value;
		var target = this.getEditElem();
		var comp = this.getCompByTagName(target[0].tagName);
		comp.setDataUrl(value);
	},
	getEditElem : function(){
		if(editElem.hasClass('wrap')){
			editElem = $(editElem.children()[0]);
		}
		return editElem;
	}
}

