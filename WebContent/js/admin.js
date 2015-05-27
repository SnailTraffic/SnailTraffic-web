var uploading = false;

function uploadSuccess() { uploading = false; }
function uploadFailed()  { uploading = false; }

function newLineStationItem(stationName) {
    var str;
    str = $('#new-line-station-template').html();
    return str.replace('{stationName}', stationName);
}

$(document).ready(function (e) {
    $('#button-upload').click(function (e) {
        uploading = true;
        var progressBar = $('#upload-progress');
        progress.css('margin-left', '-20%').css('width', '20%');
        var ss = function() {
            progress.animate({marginLeft: '120%'}, 5000, function () {
                if (uploading) {
                    $(this).css('margin-left', '-20%');
                    ss();
                } else { 
                    $(this).css('margin-left', '0')
                        .css('width', '1%')
                        .animate({width: '100%'}, 2000);
                }
            });
        }
        ss();
    });
    
    $('#login-form').bind('submit', function (e) {
        ajaxSubmit(this
            , packFormDataToJson(this)
            , function (ret) {
                if (ret.success == "true") {
                    $("#login-wrapper").css('display', 'none');
                } else {
                    $("#login-error-msg").html('用户名或密码错误')
                }
            }
            , 10000
            , function (XMLHttpRequest, textStatus, errorThrown) {
                var msg = $('#login-error-msg');
                msg.html('登陆超时，请稍后再试');
                setTimeout(function () {
                    msg.html('');
                }, 2000);
            }
        );

        return false;
    });
    
    $('#new-linename-text').bind('input propertychange', function () {
        var icon = $('#new-line-status');
        var input = $('#new-line-name');
        var text = $(this).val();
        if (text.length > 0) {
            ajax('search.jsp'
                , 'POST'
                , {'query-type': '2', 'bus-line-no': text}
                , function (ret) {
                    icon.css('display', 'inline').removeClass('glyphicon-remove').removeClass('glyphicon-ok');
                    input.removeClass('has-danger').removeClass('has-success');
                    var type = parseInt(ret.type);
                    if (ret.type != -1) {
                        icon.addClass('glyphicon-remove');
                        input.addClass('has-danger');
                    } else {
                        icon.addClass('glyphicon-ok');
                        input.addClass('has-success');
                    }
                }
                
                , function (XMLHttpRequest, textStatus, errorThrown) {
                    icon.css('display', 'none').removeClass('glyphicon-ok').removeClass('glyphicon-remove');
                    input.removeClass('has-success').removeClass('has-error');
                }
            );
        } else {
            icon.css('display', 'none').removeClass('glyphicon-remove').removeClass('glyphicon-ok');
            input.removeClass('has-error').removeClass('has-success');
        }
    });
    
    $('#new-line-search').bind('input propertychange', function () {
        var listRoot = $('#new-line-station-select');
        var text = $(this).val();
        if (text.length > 0) {
            ajax('vaguesearch.jsp'
                , 'POST'
                , {'pattern': text, 'amount': '100'}
                , function (ret) {
                    listRoot.find('li').remove();
                    for (var i = 0; i < ret.list.length; i++) {
                        listRoot.append('<li class="list-group-item">' + ret.list[i] + '</li>')
                    }
                }
                , function (XMLHttpRequest, textStatus, errorThrown) {
                    listRoot.find('li').remove();
                }
            );
        }
    });

    $('#new-line-station-select').delegate('li', 'click', function (e) {
        var listRoot = $('#new-line-list');
        listRoot.append(newLineStationItem($(this).text()));
    });

    // Line station list operations
    // Move up
    $('#new-line-list').delegate('li .btn-moveup', 'click', function (e) {
        var item = $(this).parents('li');
        var prev = item.prev();
        if (prev.length > 0) {
            prev.before(item);
        }
    });

    // Move down
    $('#new-line-list').delegate('li .btn-movedown', 'click', function (e) {
        var item = $(this).parents('li');
        var next = $(this).parents('li');
        var next = item.next();
        if (next.length > 0) {
            next.after(item);
        }
    });

    // Delete
    $('#new-line-list').delegate('li .btn-delete', 'click', function (e) {
        var item = $(this).parents('li');
        item.remove();
    });
});
