/**
 * Created by Jimmy Ben Klieve on 5/18.
 */

function _autobr(str) {
	return str.replace(/\n|\r|\n\r/ig, '<br>');
}

function clearResult () {
	$('#search-result-title').html('&nbsp;');
	$('#search-result-desc').html('&nbsp;');
	$('#search-result-container').removeClass('line-icon');
	$('#search-result-container').removeClass('station-icon');
	$('#search-result-container li').remove();
    $('.list-wrap').css('display', 'inline-block');
    $('.list-left-wrap').css('width', '43%');
    $('.list-right-wrap').css('width', '57%');
}

function showBusExchangeResult (json) {
    // leave this blank temporarily
}

function showBusLineResult(json) {
	$('#search-result-title').html(json.title);
    $('#search-result-desc').html(_autobr(json.description));

    var leftList = $('#search-result-container .list-left-wrap ul');
    for (var i = 0; i < json.left.length; i++) {
    	leftList.append('<li><span>' + (i+1) + '</span><a>' + json.left[i] + '</a></li>')
    }

    var rightList = $('#search-result-container .list-right-wrap ul');
    rightList.addClass('station-icon');
    for (var i = 0; i < json.right.length; i++) {
       rightList.append('<li><span>' + (i+1) + '</span><a>' + json.right[i] + '</a></li>');
    }

    var emptyItems = json.left.length - json.right.length;
    for (var i = 0; i < emptyItems; i++) {
        rightList.append('<li>&nbsp;</li>');
    }
}

function showBusStationResult(json) {
	$('#search-result-title').html(json.title);
	$('#search-result-desc').html(_autobr(json.description));
	
	var leftList = $('#search-result-container .list-left-wrap ul');
	leftList.append('<li style="text-align: center;"><b>站点左行线路</b></li>');
	for (var i = 0; i < json.left.length; i++) {
		leftList.append('<li><span>' + (i+1) + '</span><a>' + json.left[i] + '</a></li>');
	}
	
	var rightList = $('#search-result-container .list-right-wrap ul');
	rightList.addClass('line-icon');
	rightList.append('<li style="text-align: center; background: none;"><b>站点右行线路</b></li>');
	for (var i = 0; i < json.right.length; i++) {
		rightList.append('<li><span>' + (i+1) + '</span><a>' + json.right[i] + '</a></li>');
	}
	
	var emptyItems = json.left.length - json.right.length;
    for (var i = 0; i < emptyItems; i++) {
        rightList.append('<li>&nbsp;</li>');
    }
}

function showBusQueryNoResult() {
	$('#search-result-title').html('无结果');
	$('#search-result-desc').html('很抱歉，没有检索到与指定的关键字相关的结果。');
}

// tabs
function changeTab(activeTab) {
    $('.tab-group1').addClass('hidden');
    $(activeTab).removeClass('hidden');
    $(activeTab).find('input[type^="text"]:first').focus();
}

// text hint
function emptyTextHint() {
	$('#text-hint li:not(#text-hint-close)').remove();
}

function fillTextHint(array) {
	var hint = $('#text-hint');
	
	for (var i = 0; i < array.length; i++) {
		hint.prepend('<li>' + array[i] + '</li>');
	}
}

function expandTextHint() {
	$('#text-hint').css('visibility', 'visible');
}

function collapseTextHint() {
	$('#text-hint').css('visibility', 'collapse');
}

$(document).ready(function () {
	// Tab change
    $('#tab-group1 a').click(function (e) {
        e.preventDefault();
        if ($(this).parent('li').hasClass('disabled') == true) { return; }
        $(this).tab('show');

        var activeTab = $(this).attr('href');
        changeTab(activeTab);
    });

    // Vague text hint
    var editingText = null;
    
    $('.want-text-hint').focus(function () {
    	editingText = $(this); 
    	var l = editingText.offset().left;
    	var t = editingText.offset().top;
    	var w = editingText.outerWidth();
    	var h = editingText.outerHeight();
    	$('#text-hint').width(w);
    	$('#text-hint').offset({left: l, top: t + h});
    });
    
    $('.want-text-hint').blur(function() {
    	setTimeout(function () {
    		collapseTextHint();
    	}, 200);
    })
    
    $('.want-text-hint').bind('input propertychange', function () {
    	var text = $(this).val();
    	if (text.length > 0) {
    		ajax('vaguesearch.jsp'
    			, 'POST'
    			, {'pattern' : text}
    			, function (ret) {
    				emptyTextHint();
    				
    				if (ret.list.length > 0) {
    					fillTextHint(ret.list);
    					expandTextHint();
    				} else { 
    					collapseTextHint();
    				}
    			}
    			, function (XMLHttpRequest, textStatus, errorThrown) {
    				emptyTextHint();
    				collapseTextHint();
    			}
    		);   	
    	} else {
    		collapseTextHint();
    	}
    });
    
    $('#text-hint').delegate('li:not(#text-hint-close)', 'click', function () {
    	editingText.val($(this).text()); 
    	collapseTextHint();
    });
    
    $('#text-hint a').click(function (e) { collapseTextHint(); });
    
    // Ajax form submit
    $('form').bind('submit', function () {
        var data = packFormDataToJson(this);
        ajaxSubmit(
            this
            , data
            , function (ret) {
                var type = parseInt(ret.type);
                clearResult();
                //alert(ret.type);
                switch (type) {
			    case 1: // Exchange
			        setSidebarVisibility(true);
			        openSidebar();
			        showBusExchangeResult(ret);
			        break;
			    case 2: // Line
			        setSidebarVisibility(true);
			        openSidebar();
			        showBusLineResult(ret);
			        break;
			    case 3: // Station
			        setSidebarVisibility(true);
			        openSidebar();
			        showBusStationResult(ret);
			        break;
			    case -1:
			    	setSidebarVisibility(true);
			    	openSidebar();
			    	showBusQueryNoResult();           
			    	break;
			    default:
			    	closeSidebar();
			        setSidebarVisibility(false);
                }
            }
            , 5000
            , function (XMLHttpRequest, textStatus, errorThrown) {
                closeSidebar();
                setSidebarVisibility(false);
                $('#has-error').stop(true)
                    .animate({top: "0"}, 400, function () {
                    setTimeout(function () {
                        $('#has-error').stop(true).animate({top: "-48px"}, 300);
                    }, 2000)
                });
            }
        );

        return false;
    });
});