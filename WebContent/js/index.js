/**
 * Created by Jimmy Ben Klieve on 5/18.
 */

function clearResult () {
	$('#search-result-title').html('&nbsp;');
	$('#search-result-desc').html('&nbsp;');
	$('#search-result-container').removeClass('line-icon');
	$('#search-result-container ul li').remove();
}

function showBusExchangeResult (json) {
    // leave this blank temporarily
}

function showBusLineResult(json) {
	$('#search-result-title').html(json.title);
    $('#search-result-desc').html(json.description);

    var leftList = $('#search-result-container .list-left-wrap ul');

    for (var i = 0; i < json.left.length; i++) {
    	leftList.append('<li><span>' + (i+1) + '</span><a>' + json.left[i] + '</a></li>')
    }

    var rightList = $('#search-result-container .list-right-wrap ul');
    rightList.addClass('line-icon');
    for (var i = 0; i < json.right.length; i++) {
       rightList.append('<li><span>' + (i+1) + '</span><a>' + json.right[i] + '</a></li>');
    }

    var emptyItems = json.left.length - json.right.length;
    for (var i = 0; i < emptyItems; i++) {
        rightList.append('<li>&nbsp;</li>');
    }
}

function showBusStationResult(json) {
	
}


$(document).ready(function () {

    changeTab('#tab-group1-tab1');

    // Tab change
    function changeTab(activeTab) {
        $('.tab-group1').addClass('hidden');
        $(activeTab).removeClass('hidden');
        $(activeTab).find('input[type^="text"]:first').focus();
    }

    $('#tab-group1 a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');

        var activeTab = $(this).attr('href');
        changeTab(activeTab);
    });
    
    $('form').bind('submit', function() {
    	var data = packFormDataToJson(this);
    	ajaxSubmit (this, data, function (ret) {
    		var type = parseInt(ret.type);
            clearResult();
            alert(ret.type);
            switch (type) {
    		case 1: // Exchange
    			showBusExchangeResult(ret);
                break;
    		case 2: // Line
    			showBusLineResult(ret);
                break;
    		case 3: // Station
                showBusStationResult(ret);
    			break;
    		default:
    			// Wrong !
    		}
    	});

        return false;
    });
    
});