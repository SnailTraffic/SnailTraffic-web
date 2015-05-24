/*!
 * Created by JimmyBen on 5/21.
 *
 * Submit form using ajax
 */

if (typeof jQuery === 'undefined') {
    throw new Error('ajaxSubmit\'s JavaScript requires jQuery')
}

function packFormDataToJson(frm) {
    var o = {};
    var a = $(frm).serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });

    return o;
}

function ajaxSubmit(frm, data, fn) {
    $.ajax({
        url: frm.action,
        type: frm.method,
        data: data,
        success: fn
    });
}