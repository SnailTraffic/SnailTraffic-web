/**
 * Created by Jimmy Ben Klieve on 5/18.
 */

// Sidebar Control
function setSidebarVisibility(visible) {
    $('#sidebar-window').css('visibility', visible ? 'visible' : 'hidden' );
}

function closeSidebar() {
    $('#sidebar-window').stop(true).animate({left: "-420px"}, 380, 'easeOutBounce', function () {
       $('#sidebar-button-open').stop(true).animate({opacity: 1}, 50);
    });
}

function openSidebar() {
    $('#sidebar-button-open').stop(true).animate({opacity: 0}, 50, function () {
       $('#sidebar-window').stop(true).animate({left: "0"}, 380, 'easeOutBounce');
    });
}

$(document).ready(function (e) {
	$('#sidebar-button-close').click(function (e) {
        closeSidebar();
    });

    $('#sidebar-button-open').click(function (e) {
        openSidebar();
    });
	
    var map;

    try {
        map = new BMap.Map('map-canvas', {minZoom: 12});

        map.centerAndZoom('武汉', 11);
        map.enableScrollWheelZoom(true);

        map.addControl(new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_LEFT}));
        map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT}));
        map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT}));

    } catch (exception) {
        // Do nothing
    }
});