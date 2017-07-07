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
	},
	getAttr:function(comp, name){
		
	},
	getComp : function(tag){
		if(tag=='INPUT'){
			return new Input();
		}
		if(tag=='SPAN'){
			return new Span();
		}
	},
	
	buildPropsEditor : function(elem){
		var comp = this.getComp(elem[0].tagName);
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
			var row = '<div class="row"> <span class="label">'+css.text+'</span><input scope="css" name="'+css.name+'" value="'+value+'"/></div><br/>';
			html += row;
		}
		return html;
	}
}

