function move(target,left,top){
	//移动target元素
	var toppx = target.css('top');
	var leftpx = target.css('left');
	var topVal = toppx.replace('px' , '');
	var leftVal = leftpx.replace('px' , '');
	topVal -= top;
	leftVal -= left;
	target.css('top',topVal+'px');
	target.css('left' , leftVal+'px');
	return target;
}

function moveLeft(x){
	event.preventDefault();
	//是否有批量选择框
	var wrapper = $('.ui-selectable-helper');
	if(wrapper.length>0){
		move(wrapper , x , 0);
		var selElems = $('.ui-selected');
    	for(var i=0;i<selElems.length;i++){
    		var xx = $(selElems[i]);
			move($(selElems[i]) , x, 0);
    	}
		return;
	}
	// 是否有选择的元素
	if(currentEditComp!=null){
		move(currentEditComp , x , 0);
	}
	
}


function moveUp(y){
	event.preventDefault();
	//是否有批量选择框
	var wrapper = $('.ui-selectable-helper');
	if(wrapper.length>0){
		move(wrapper , 0 , y);
		var selElems = $('.ui-selected');
    	for(var i=0;i<selElems.length;i++){
    		var xx = $(selElems[i]);
			move($(selElems[i]) , 0, y);
    	}
		return;
	}
	// 是否有选择的元素
	if(currentEditComp!=null){
		move(currentEditComp , 0 , y);
	}
}


$(function(){
	$(document).keydown(function(event){ 
		if(event.keyCode==38){
			moveUp(1);
		}else if(event.keyCode==40){
			moveUp(-1);
		}else if(event.keyCode==37){
			moveLeft(1);
		}else if(event.keyCode==39){
			moveLeft(-1);
		}
	});
});