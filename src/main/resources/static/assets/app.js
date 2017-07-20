$(function () {
    $('#send').click(function () {});

    $('#user').keyup(function (event) {
        if (event.keyCode === 13) {
            $('#join').click();
        }
    });

    $('#message').keyup(function (event) {
        if (event.keyCode === 13) {
            $('#send').click();
        }
    });

    $('#leave').click(function () {
        window.location.href = "../leave";
    });
});