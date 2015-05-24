// This script relys on jQuery

$(document).ready(function (e) {
	var map;
	// Map sidebar event actions
	/*
	$('#_sidebar-expand-button').click(function (e) { expandSidebar(); });
	$('#_sidebar-collapse-button').click(function(e) { collapseSidebar(); });
	
	function expandSidebar() {
		$('#_sidebar-expand-button').fadeOut(50, null, function (e) {
			$('#_sidebar-content-wrapper').animate({left: '480px', opacity: '1'}, 200);
		});
	}
	
	function collapseSidebar() {
		$('#_sidebar-content-wrapper').animate({left: '0', opacity: '0'}, 100, null, function () {
			$('#_sidebar-expand-button').fadeIn(100);
		});
	}*/

try {
	map = new BMap.Map('_map', {minZoom: 12});
	map.centerAndZoom('武汉', 11);
	map.enableScrollWheelZoom(true);

	map.addControl(new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_LEFT}));
	map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT}));
	map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT}));
} catch (e) {
	$('#_sidebar').css('display', 'none');
}

});