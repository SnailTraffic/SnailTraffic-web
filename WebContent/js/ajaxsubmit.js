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

function ajaxSubmit(frm, data, successfn, timeout, errorfn) {
    $.ajax({
        url: frm.action,
        type: frm.method,
        data: data,
        success: successfn,
        timeout: timeout,
        error: errorfn
    });
}

function ajax(url, meth, data, successfn, errorfn) {
	$.ajax({
		url: url,
		type: meth,
		data: data,
		success: successfn,
		error: errorfn
	});
}