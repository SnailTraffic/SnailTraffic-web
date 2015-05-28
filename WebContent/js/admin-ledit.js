/**
 * Created by JimmyBen on 5/27.
 */

var $ledit_root; // Div (ROOT)

var $ledit_template; // Ul
var $ledit_panelToggle; // Input - Checkbox
var $ledit_msg; // Span
var $ledit_loadButton; // Button
var $ledit_lineNameLoaded; // B

var $ledit_lineName; // Input - Text
var $ledit_lineStatus; // Span

var $ledit_lineAltToggle; // Input - Checkbox
var $ledit_lineNewNameWrap; // Div
var $ledit_lineNewName; // Input - Text
var $ledit_lineNewStatus; // Span

var $ledit_stationSelList; // Ul
var $ledit_lineStationList; // Ol

var $ledit_submitButtons; // . Button

$(document).ready(function () {
    $ledit_root = $('#ledit-pannel-root');

    $ledit_template = $('#ledit-station-template');
    $ledit_panelToggle = $('.open-close-checkbox');
    $ledit_msg = $('#ledit-msg');

    $ledit_loadButton = $('#ledit-line-load > button');
    $ledit_lineNameLoaded = $('#ledit-linename-loaded');

    $ledit_lineName = $('#ledit-linename');
    $ledit_lineStatus = $('#ledit-line-status');

    $ledit_lineAltToggle = $('#ledit-linename-alt');

    $ledit_lineNewNameWrap = $('#ledit-linename-new-wrapper');
    $ledit_lineNewName = $('#ledit-linename-new');
    $ledit_lineNewStatus = $('#ledit-line-new-status');

    $ledit_stationSelList = $('#ledit-station-select');
    $ledit_lineStationList = $('#ledit-station-list');

    $ledit_submitButtons = $('.ledit-submit');
});

function newLineStationItem(stationName, left, right) {
    var str = $ledit_template.html();
    return str.replace('{stationName}', stationName)
        .replace('{left}', left ? 'checked' : '')
        .replace('{right}', right ? 'checked' : '')
        .replace(/(<!--)|(-->)/ig, '');
}

function feedbackLineNameStatus(fbStatus) {
    $ledit_loadButton.addClass('disabled');
    $ledit_lineStatus.css('display', 'none');

    var loadVal = $('#ledit-linename-loaded').text();
    var lineVal = $('#ledit-linename').val();

    if (lineVal != '' && loadVal == lineVal) {
        $ledit_lineNewNameWrap.css('display', 'inherit');
    } else {
        $ledit_lineNewNameWrap.css('display', 'none');
    }

    if (fbStatus == true) {
        $ledit_lineStatus.css('display', 'table-cell');
    } else if (fbStatus == false) {
        $ledit_lineStatus.css('display', 'none');
        $ledit_loadButton.removeClass('disabled');
    }
}

function feedbackNewLineNameStatus(fbStatus) {
    if ($ledit_lineNewName.val() == null) { return; }

    $ledit_lineNewNameWrap.removeClass('has-success').removeClass('has-error');
    $ledit_lineNewStatus.removeClass('glyphicon-ok').removeClass('glyphicon-remove');

    if (fbStatus == true) {
        $ledit_lineNewNameWrap.addClass('has-success');
        $ledit_lineNewStatus.addClass('glyphicon-ok');
    } else if (fbStatus == false) {
        $ledit_lineNewNameWrap.addClass('has-error');
        $ledit_lineNewStatus.addClass('glyphicon-remove');
    }
}

$(function () {
    // Line edit thingy...

    // Find if bus line is in database
    var loadData = null;
    $ledit_lineName.bind('input propertychange', function () {
        var text = $ledit_lineName.val();
        if (text.length > 0) {
            ajax('search.jsp'
                , 'POST'
                , {'query-type': '2', 'bus-line-no': text}
                , function (ret) {
                    var type = parseInt(ret.type);
                    if (type == -1) {
                        loadData = null;
                        feedbackLineNameStatus(true);
                    } else {
                        loadData = ret;
                        feedbackLineNameStatus(false);
                    }
                }
                , function (XMLHttpRequest, textStatus, errorThrown) {
                    feedbackLineNameStatus(null);
                }
            );
        } else {
            feedbackLineNameStatus(null);
        }
    });

    $ledit_loadButton.click(function (e) {
        $ledit_lineNameLoaded.text($ledit_lineName.val());
        $ledit_lineNewNameWrap.css('display', 'inherit');
        unpackLineData(loadData);
    });

    $ledit_lineAltToggle.change(function (e) {
        var checked = $ledit_lineAltToggle.is(':checked');
        if (checked) {
            $ledit_lineNewName.attr('readonly', false);
        } else {
            $ledit_lineNewName.attr('readonly', true);
        }
    });

    $ledit_lineNewName.bind('input propertychange', function () {
        var text = $ledit_lineNewName.val();
        if (text.length > 0) {
            ajax('search.jsp'
                , 'POST'
                , {'query-type': '2', 'bus-line-no': text}
                , function (ret) {
                    var type = parseInt(ret.type);
                    if (type != -1) {
                        feedbackNewLineNameStatus(false);
                    } else {
                        feedbackNewLineNameStatus(true);
                    }
                }
                , function (XMLHttpRequest, textStatus, errorThrown) {
                    feedbackNewLineNameStatus(null);
                }
            );
        } else {
            feedbackNewLineNameStatus(null);
        }
    });

    $('#ledit-station-search').bind('input propertychange', function () {
        var text = $(this).val();
        if (text.length > 0) {
            ajax('vaguesearch.jsp'
                , 'POST'
                , {'pattern': text, 'amount': '100'}
                , function (ret) {
                    $ledit_stationSelList.find('li').remove();
                    for (var i = 0; i < ret.list.length; i++) {
                        $ledit_stationSelList.append('<li class="list-group-item">' + ret.list[i] + '</li>')
                    }
                }
                , function (XMLHttpRequest, textStatus, errorThrown) {
                    $ledit_stationSelList.find('li').remove();
                }
            );
        }
    });

    $ledit_stationSelList.delegate('li', 'click', function (e) {
        $ledit_lineStationList.append( newLineStationItem( $(this).text(), true, true) );
    });

    // Line station list operations
    // Because of items in this list are created dynamically, so should use .delegate method

    // Move up
    $ledit_lineStationList.delegate('li .btn-moveup', 'click', function (e) {
        operateListItem(-1, $(this).parents('li'));
    });

    // Move down
    $ledit_lineStationList.delegate('li .btn-movedown', 'click', function (e) {
        operateListItem(1, $(this).parents('li'));
    });

    // Delete
    $ledit_lineStationList.delegate('li .btn-delete', 'click', function (e) {
        operateListItem(0, $(this).parents('li'));
    });

    $ledit_panelToggle.change(function (e) {
        var checked = $(this).is(':checked');
        var $panelBody = $(this).parents('.panel-heading').next('.panel-body');
        if (checked) {
            $panelBody.css('display', 'block');
        } else {
            $panelBody.css('display', 'none');
        }
    });

    $ledit_submitButtons.click(function (e) {
       packLineDataAndSend(parseInt($(this).val()))
    });
});

function packLineDataAndSend(mode) {
    var data = { mode: mode, title: $ledit_lineName.val() };
    var lArr = [];
    var rArr = [];
    var msg;

    if (mode == 0 || mode == 1) {
        // pack station list:

        var $lis = $ledit_lineStationList.find('li');
        var $li;
        var l, r, t;
        var __flag = false;

        for (var i = 0; i < $lis.length; i++) {
            $li = $($lis[i]);
            l = $li.find('.line-stop-left').is(':checked');
            r = $li.find('.line-stop-right').is(':checked');
            if (!l && !r) {
                $li.addClass('has-error');
                flag = true;
                break;
            } else {
                $li.removeClass('has-error');
                t = $li.find('.line-station-name').val();

                if (l) { lArr.push(t); }
                if (r) { rArr.push(t); }
            }
        }

        if ($lis.length <= 0) {
            alert('请向列表中添加至少两个站点');
            return false;
        } else if (lArr.length < 2) {
            alert('请为至少两个站点勾选[左行停靠]选项');
            return false;
        } else if (rArr.length < 2) {
            alert('请为至少两个站点勾选[右行停靠]选项');
            return false;
        } else if (__flag) {
            var stillSubmit = confirm('列表中部分站点既未选择[左行停靠]选项，也未选择[右行停靠]选项。' +
                '\n这虽然不影响本次提交的过程，但是在提交的数据中，这些站点将会被忽略。' +
                '\n是否仍然要提交？');

            if (!stillSubmit) { return false; }
        }

        data['left'] = lArr;
        data['right'] = rArr;

        msg = '新增';

        if (mode == 0) {
            if ($ledit_lineAltToggle.is(':checked')) {
                data['title-new'] = $ledit_lineNewName.val();
            } else {
                data['title-new'] = $ledit_lineName.val();
            }
            msg = '修改';
        }
    } else {
        msg = '删除';
    }

    ajax('admin.jsp'
        , 'POST'
        , data
        , function (ret) {
            if (ret.success) {
                $ledit_msg.css('color', '#080').text(msg + '<b>' + $ledit_lineName.val() + '</b>成功！' )
            } else {
                $ledit_msg.css('color', '#c10').text(msg + '<b>' + $ledit_lineName.val() + '</b>失败！' + ret.msg);
            }
            setTimeout( function () { $ledit_msg.css('display', 'none'); }, 2000);
        }
        , function (XMLHttpRequest, textStatus, errorThrown) {
            $ledit_msg.css('color', '#c10').text( '网络出现异常，请稍后再试。');
            setTimeout( function () { $ledit_msg.css('display', 'none'); }, 2000);
        }
    );
}

function unpackLineData(json) {
    if (json) {
        $ledit_lineNameLoaded.val(json.title);

        for (var i = 0; i < json.stations.length; i++) {
            var station = json.stations[i];
            newLineStationItem(station.title, station.left, station.right);
        }
    }
}