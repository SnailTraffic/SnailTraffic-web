/**
 * Created by Jimmy Ben Klieve on 5/18.
 */

function _autobr(str) {
    return str.replace(/\n|\r|\n\r/ig, '<br>');
}

function clearResult () {
    $('#search-result-title').html('&nbsp;');
    $('#search-result-desc').html('&nbsp;');
    $('#search-result-container').removeClass('line-icon').removeClass('station-icon');
    $('#search-result-container li').remove();
    $('.list-wrap').css('display', 'inline-block');
    $('.list-left-wrap').css('width', '43%');
    $('.list-right-wrap').css('width', '57%');
}

function showBusExchangeResult (json) {
    $('#search-result-title').html(json.start + ' &rarr;<br/>' + json.end);
    $('#search-result-desc').html('');

    var $template_root = $('#bus-exchange-scheme-template');
    var $template_item_root = $('#bus-exchange-scheme-part-template');
    
    var newSchemeItem = function (line, start, end) {
    	var str = $template_item_root.html();
    	return str.replace('{lineName}', line).replace('{start}', start).replace('{end}', end);
    };

    var newScheme = function (name, scheme) {
    	var str = $template_root.html();
    	return str.replace('{schemeName}', name).replace('{listItem}', scheme);
    };

    $('.list-left-wrap').css('width', '100%');
    $('.list-right-wrap').css('display', 'none');

    var $list = $('.list-left-wrap ul');
    var scheme;
    var section;
    var str;
    
    for (var i = 0; i < json.schemes.length; i++) {
    	scheme = json.schemes[i];
    	str = '';
    	for (var j = 0; j < scheme.sections.length; j++) {
    		section = scheme.sections[j];
    		str += newSchemeItem(section.name, section.start, section.end);
    	}
    	
    	$list.append(newScheme(scheme.title, str));
    }
}

function showBusLineResult(json) {
    $('#search-result-title').html(json.title);
    $('#search-result-desc').html(_autobr(json.description));

    var i;

    var leftList = $('#search-result-container .list-left-wrap ul');
    leftList.append('<li class="list-header">左行线路经过站点</li>');
    for (i = 0; i < json.left.length; i++) {
        leftList.append('<li><span>' + (i+1) + '</span><a>' + json.left[i] + '</a></li>')
    }

    var rightList = $('#search-result-container .list-right-wrap ul');
    rightList.append('<li class="list-header">右行线路经过站点</li>');
    rightList.addClass('station-icon');
    for (i = 0; i < json.right.length; i++) {
       rightList.append('<li><span>' + (i+1) + '</span><a>' + json.right[i] + '</a></li>');
    }

    var emptyItems = json.left.length - json.right.length;
    for (i = 0; i < emptyItems; i++) {
        rightList.append('<li>&nbsp;</li>');
    }

    map.clearOverlays();
    drawBusLine(json.title, true);
}

function showBusStationResult(json) {
    $('#search-result-title').html(json.title);
    $('#search-result-desc').html(_autobr(json.description));

    var i;

    var leftList = $('#search-result-container .list-left-wrap ul');
    leftList.append('<li class="list-header">站点左行线路</li>');
    for (i = 0; i < json.left.length; i++) {
        leftList.append('<li><span>' + (i+1) + '</span><a>' + json.left[i] + '</a></li>');
    }
    
    var rightList = $('#search-result-container .list-right-wrap ul');
    rightList.addClass('line-icon');
    rightList.append('<li class="list-header">站点右行线路</li>');
    for (i = 0; i < json.right.length; i++) {
        rightList.append('<li><span>' + (i+1) + '</span><a>' + json.right[i] + '</a></li>');
    }
    
    var emptyItems = json.left.length - json.right.length;
    for (i = 0; i < emptyItems; i++) {
        rightList.append('<li>&nbsp;</li>');
    }

    map.clearOverlays();
    drawBusStation(json.title);
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

$(function () {
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
    var $inputControl = $('.want-text-hint');
    $inputControl.focus(function () {
        editingText = $(this); 
        var l = editingText.offset().left;
        var t = editingText.offset().top;
        var w = editingText.outerWidth();
        var h = editingText.outerHeight();
        $('#text-hint').width(w).offset({left: l, top: t + h});
    });

    $inputControl.blur(function() {
        setTimeout(function () {
            collapseTextHint();
        }, 200);
    });

    $inputControl.bind('input propertychange', function () {
        var text = $(this).val();
        if (text.length > 0) {
            ajax('vaguesearch.jsp'
                , 'POST'
                , {'pattern': text, 'amount': '10'}
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
        mapClearOverlay();
        ajaxSubmit(
            this
            , data
            , function (ret) {
                var type = parseInt(ret.type);
                clearResult();
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

    $('.list-wrap ul').delegate('.line-exchange-scheme-title', 'click', function (e) {
    	var $body = $(this).next('.line-exchange-scheme-body');
    	$('.line-exchange-scheme-body').hide();
    	$body.toggle();
    	
    	var list = [];
    	$body.find('.l-ex-si-start').each(function (i, val) {
    		list.push(val.innerText);
    	});
    	
    	list.push($body.find('.l-ex-si-stop').last().text());

        map.clearOverlays();
    	drawBusTransit(list);
    });
});