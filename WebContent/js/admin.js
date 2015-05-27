var uploading = false;

function uploadSuccess() { uploading = false; }
function uploadFailed()  { uploading = false; }

/*
 * op
 *  -1: Move up
 *   0: Delete/Remove
 *   1: Move down
 *
 * $selectedItem
 *   Currently selected <li> element
 */
function operateListItem(op, $selectedItem) {
    if (op == -1) {
        var prev = $selectedItem.prev();
        if (prev.length > 0) {
            prev.before($selectedItem);
        }
    } else if (op == 1) {
        var next = $selectedItem.next();
        if (next.length > 0) {
            next.after($selectedItem);
        }
    } else if (op == 0) {
        $selectedItem.remove();
    }
}

$(function () {
    // upload
    $('#button-upload').click(function (e) {
        uploading = true;
        var progressBar = $('#upload-progress');
        progressBar.css('margin-left', '-20%').css('width', '20%');
        var ss = function() {
            progressBar.animate({marginLeft: '120%'}, 5000, function () {
                if (uploading) {
                    $(this).css('margin-left', '-20%');
                    ss();
                } else { 
                    $(this).css('margin-left', '0')
                        .css('width', '1%')
                        .animate({width: '100%'}, 2000);
                }
            });
        };
        ss();
    });

    // login
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
});
