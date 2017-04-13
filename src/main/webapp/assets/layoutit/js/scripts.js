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

	removeElm();
	gridSystemGenerator();

})