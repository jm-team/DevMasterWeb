function randomNumber() {
	return randomFromInterval(1, 1e6)
}
function randomFromInterval(e, t) {
	return Math.floor(Math.random() * (t - e + 1) + e)
}
function gridSystemGenerator() {
	$(".lyrow .preview input").bind("keyup", function() {
		var e = 0;
		var t = "";
		var n = false;
		var r = $(this).val().split(" ", 12);
		$.each(r, function(r, i) {
			if (!n) {
				if (parseInt(i) <= 0)
					n = true;
				e = e + parseInt(i);
				t += '<div class="col-md-' + i + ' column"></div>'
			}
		});
		if (e == 12 && !n) {
			$(this).parent().next().children().html(t);
			$(this).parent().prev().show()
		} else {
			$(this).parent().prev().hide()
		}
	})
}

function removeElm() {
	$(".container").delegate(".remove", "click", function(e) {
		e.preventDefault();
		$(this).parent().remove();

	})
}

function doResize(){
	$(".container").css("min-height", $(window).height() - 32);
}

function removeMenuClasses() {
    $(".navbar-layoutit button").removeClass("active");
}

function changeStructure(e, t) {
    $("#download-layout ." + e).removeClass(e).addClass(t)
}

function cleanHtml(e) {
    $(e).parent().append($(e).children().html())
}

function downloadLayoutSrc() {
    var e = "";
    $("#download-layout").children().html($(".container").html());
    var t = $("#download-layout").children();
    t.find(".preview, .configuration, .drag, .remove").remove();
    t.find(".lyrow").addClass("removeClean");
    t.find(".box-element").addClass("removeClean");
    t.find(".lyrow .lyrow .lyrow .lyrow .lyrow .removeClean").each(function() {
        cleanHtml(this)
    });
    t.find(".lyrow .lyrow .lyrow .lyrow .removeClean").each(function() {
        cleanHtml(this)
    });
    t.find(".lyrow .lyrow .lyrow .removeClean").each(function() {
        cleanHtml(this)
    });
    t.find(".lyrow .lyrow .removeClean").each(function() {
        cleanHtml(this)
    });
    t.find(".lyrow .removeClean").each(function() {
        cleanHtml(this)
    });
    t.find(".removeClean").each(function() {
        cleanHtml(this)
    });
    t.find(".removeClean").remove();
    $("#download-layout .column").removeClass("ui-sortable");
    $("#download-layout .row-fluid").removeClass("clearfix").children().removeClass("column");
    if ($("#download-layout .container").length > 0) {
        changeStructure("row-fluid", "row")
    }
    formatSrc = $.htmlClean($("#download-layout").html(), {
        format: true,
        allowedAttributes: [
            ["id"],
            ["class"],
            ["data-toggle"],
            ["data-target"],
            ["data-parent"],
            ["role"],
            ["data-dismiss"],
            ["aria-labelledby"],
            ["aria-hidden"],
            ["data-slide-to"],
            ["data-slide"]
        ]
    });
    
    $.ajax({
        type: "POST",
        //url: '${ServiceName}' + "/component/doAddComponent",
        url: "/component/doAddComponent",
        data: {
            groupId:'test',
            name:'test',
            version:'1.0.0',
            type:'html',
            remark:formatSrc
        },
        dataType:'json'
    }).done(function (data) {
        if (data.code == -1) {
            layer.msg(data.desc);
        } else {
            layer.msg('保存成功');
            //closeWindowAndRefreshParent();
        }
    });
    
    $("#download-layout").html(formatSrc);
}

$(window).resize(function() {
	doResize();
});


$(document).ready(function() {
	$(".ui-layout-east").tabs();
	doResize();
	$(".container, .container .column").sortable({
		connectWith : ".column",
		opacity : .35,
		handle : ".drag"
	});

	$(".sidebar-nav .lyrow").draggable({
		connectToSortable : ".container",
		helper : "clone",
		handle : ".drag",
		drag : function(e, t) {
			t.helper.width(400)
		},
		stop : function(e, t) {
			$(".container .column").sortable({
				opacity : .35,
				connectWith : ".column"
			})
		}
	});

	/*$(".sidebar-nav .box").draggable({
		connectToSortable : ".column",
		helper : "clone",
		handle : ".drag",
		drag : function(e, t) {
			t.helper.width(400)
		},
		stop : function(e, t) {
		}
	});*/
	
	/*$("#layout_save").click(function(e) {
        e.preventDefault();
        savePlugin();
    });*/
	
	$("#layout_preview").click(function() {
        removeMenuClasses();
        $(this).addClass("active");
        
        $('#west').hide();
        $('#east').hide();
        $(".container").find(".configuration, .drag, .remove").hide();
        $(".container").find(".view").attr("style", "padding-top: 10px;");
        
        return false;
    });
	
	$("#layout_edit").click(function() {
	    removeMenuClasses();
        $(this).addClass("active");
        
        $('#west').show();
        $('#east').show();
        $(".container").find(".configuration, .drag, .remove").show();
        $(".container").find(".view").removeAttr("style");
        
        return false;
	});

	removeElm();
	gridSystemGenerator();

})