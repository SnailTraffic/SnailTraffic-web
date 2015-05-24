function ajaxSubmit(frm, fn) {
	var dataParams = getFromJson(frm);
	$.ajax({
		url: frm.action,
		type: frm.method,
		data: dataParams,
		success: fn
	});
}

function getFromJson(frm) {
	var o = {};
	var a= $(frm).serializeArray();
	$.each(a, function () {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});

	return o;
}

$(document).ready(function (e) {
	var currentTab = 0;
	initialize();
	
	function initialize() {
		$('.top-list div').removeClass('tab-selected');
		$('.top-list div:eq(' + currentTab +')').addClass('tab-selected');
		$('.page').css({'visibility': 'collapse', 'display': 'none'});
		$('.page:eq(' + currentTab + ')').css({visibility: 'visible', display: 'block'});
	}
	
	function changeTabAndPage(tabElem, index) {
		$('.top-list div').removeClass('tab-selected');
		$(tabElem).addClass('tab-selected');
		$('.page').css({'visibility': 'collapse', 'display': 'none'});
		$('.page:eq(' + currentTab + ')').css({visibility: 'visible', display: 'block'});
	}
	
	$('.top-list div').click(function(e) {
		currentTab = parseInt($(this).find('input').val());
		changeTabAndPage(this, currentTab);
	});
	
	// Request the server	
	$('form').bind('submit', function () {
		ajaxSubmit(this, function (retData) {
			alert(retData);
		});
		return false;
	});
});