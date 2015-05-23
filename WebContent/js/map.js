/**
 * Created by Jimmy Ben Klieve on 5/18.
 */

$(document).ready(function (e) {
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