var uploading = false;

function uploadSuccess() {
	uploading = false;
	
}

function uploadFailed() {
	uploading = false;
}

$(document).ready(function (e) {
	$('#button-upload').click(function (e) {
		uploading = true;
		$('#upload-progress').css('margin-left', '-20%');
		$('#upload-progress').css('width', '20%');
		var ss = function() {
			$('#upload-progress').animate({marginLeft: '120%'}, 5000, function () {
				if (uploading) {
					$(this).css('margin-left', '-20%');
					ss();
				} else { 
					$(this).css('margin-left', '0')
					$(this).css('width', '1%');
					$(this).animate({width: '100%'}, 2000);
				}
			});
		}
		ss();
	});
});
