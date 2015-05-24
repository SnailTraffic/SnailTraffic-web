/* this script rely on jQuery */

$(document).ready(function (e) {
	
	function showHint(elem) {
		elem.css('display', 'block');
	}
	
	function hideHint(elem) {
		elem.css('display', 'none');
	}
	
	$('.ben-text').focus(function(e) {
		if ($(this).val().length > 0) {
			var elem = $(this).next('.ben-texthint');
			setTimeout(function () {
				showHint(elem);
			}, 750);
		}
	});
	
	$('.ben-text').on('input', function (e) {
		inputString = $(this).val();
		var elem = $(this).next('.ben-texthint')
		if (inputString.length > 0) {
			setTimeout(function () {
				showHint(elem);
			}, 300);
		} else {
			hideHint(elem);	
		}
	});
	
	$('.ben-texthint ul li').click(function (e) {
		if ($(this).text() != $('.ben-texthint ul li:last').text()) {
			var elem = $(this).parents('.ben-texthint');
			hideHint(elem);
			elem.prev('.ben-text').val($(this).text());
		}
	})
	
	$('.ben-texthint .ben-texthint-spec a').click(function (e) {
		hideHint( $(this).parents('.ben-texthint') );
	});
});