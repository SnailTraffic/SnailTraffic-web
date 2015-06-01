/**
 * Created by JimmyBen on 2015/6/1.
 */

// Common
var $sedit_panel_root; // div
var $sedit_tab_pane; // ul/ol
var $sedit_tabs; // div[]

var $sedit_add_submit;
var $sedit_alter_submit;
var $sedit_remove_submit;

$(document).ready(function () {
    $sedit_panel_root = $('#sedit-panel-root');
    $sedit_tab_pane = $('#sedit-tabs');
    $sedit_tabs = $('.sedit-tab');

    $sedit_add_submit = $('#sedit-add-submit');
    $sedit_alter_submit = $('#sedit-alter-submit');
    $sedit_remove_submit = $('#sedit-remove-submit');

    // Tab init
    $sedit_tabs.hide();
    $sedit_tabs.first().show();
});

$(function () {
    $sedit_tab_pane.find('a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');

        var activeTab = $(this).attr('href');
        $sedit_tabs.hide();
        $(activeTab).show();

        return false;
    });

    $sedit_panel_root.find('button[type^="submit"]').click(function (e) {
        var form = $(this).parents('form')[0];
        var data = packFormDataToJson(form);

        var mode = e.target.value;
        data['mode'] = mode;

        var str = mode == -1 ? '删除' : (mode == 0 ? '修改' : '添加');

        ajaxSubmit(form
            , data
            , function (ret) {
                if (ret.success) {
                    alert(str + ' ' + data.name + ' 成功！');
                } else {
                    alert(str + ' ' + data.name + ' 失败！\n' + data.msg);
                }
            }
            , 10000
            , null
        );

        return false;
    });

    $('#sedit-add-sname').bind('input propertychange', function (e) {
        vagueStationSearch(function (ret) {
            var $icon = $('#sedit-add-sname-feedback');
            $icon.removeClass('glyphicon-ok').removeClass('glyphicon-remove');
            if (ret.list.length <= 0) {
                $icon.addClass('glyphicon-ok');
            } else {
                $icon.addClass('glyphicon-remove');
            }
        });
    });

    $('#sedit-alter-sname').bind('input propertychange', function (e) {
        vagueStationSearch(function (ret) {
            var $icon = $('#sedit-alter-sname-feedback');
            $icon.removeClass('glyphicon-ok').removeClass('glyphicon-remove');
            if (ret.list.length > 0) {
                $icon.addClass('glyphicon-ok');
            } else {
                $icon.addClass('glyphicon-remove');
            }
        });
    });

    $('#sedit-alter-sname-new').bind('input propertychange', function (e) {
        vagueStationSearch(function (ret) {
            var $icon = $('#sedit-alter-sname-new-feedback');
            $icon.removeClass('glyphicon-ok').removeClass('glyphicon-remove');
            if (ret.list.length <= 0) {
                $icon.addClass('glyphicon-ok');
            } else {
                $icon.addClass('glyphicon-remove');
            }
        });
    });

    $('#sedit-remove-sname').bind('input propertychange', function (e) {
        vagueStationSearch(function (ret) {
            var $icon = $('#sedit-rename-sname-feedback');
            $icon.removeClass('glyphicon-ok').removeClass('glyphicon-remove');
            if (ret.list.length <= 0) {
                $icon.addClass('glyphicon-ok');
            } else {
                $icon.addClass('glyphicon-remove');
            }
        });
    });
});

function vagueStationSearch(text, fn) {
    ajax('vaguesearch.jsp'
        , 'POST'
        , {'pattern': text, 'amount': 100}
        , fn
        , null
    );
}