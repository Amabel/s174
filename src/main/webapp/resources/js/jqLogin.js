$(document).ready(function() {
    $.addBtnLoginListener();
});

$.addBtnLoginListener = function() {
    $('#btnLogin').click(function(event) {
        event.preventDefault();
        var pwd = $('#pwdInput').val();
        pwd = sha1.hex(pwd);
        $('#pwdInput').val(pwd);
        $('#formLogin').submit();
    });
}
