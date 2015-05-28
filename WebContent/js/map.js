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

function drawBusLine(lineName, left) {
    if (map) {
        var busline = new BMap.BusLineSearch(map, {
            renderOptions: {map: map},
            onGetBusListComplete: function (result) {
                if (result) {
                    var line = result.getBusListItem(left ? 0 : 1);
                    busline.getBusLine(line);
                }
            }
        });

        busline.getBusList(lineName);
    }
}

function drawBusStation(stationName) {
    // Not so accurate
    if (map) {
        var geoCoder = new BMap.Geocoder();
        geoCoder.getPoint('武汉市' + stationName, function (point) {
            if (point) {
                map.centerAndZoom(point, 15);
                map.addOverlay(new BMap.Marker(point));
            } else {
                map.clearOverlays();
            }
        });
    }
}

function mapClearOverlay() {
	map.clearOverlays();
}

var map = null; // global
$(document).ready(function (e) {
    $('#sidebar-button-close').click(function (e) {
        closeSidebar();
    });

    $('#sidebar-button-open').click(function (e) {
        openSidebar();
    });

    try {
        map = new BMap.Map('map-canvas', {minZoom: 12});

        map.setCurrentCity('武汉');
        map.centerAndZoom('武汉', 11);
        map.enableScrollWheelZoom(true);

        map.addControl(new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_LEFT}));
        map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT}));
        map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT}));
        map.addControl(new BMap.GeolocationControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT}));

    } catch (exception) {
        map = null;
    }
});