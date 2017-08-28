/**
 * Created by tkalnitskaya on 20.07.2017.
 */
var stompClient = null;
var user = null;

function join(){
    connect();
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        user = frame.headers['user-name'];
        onSuccessfulConnect();
    });
}

function onSuccessfulConnect() {
    stompClient.subscribe('/user/queue/error', function (wrappedMessage) {
        showErrorMessage(unwrapMessage(wrappedMessage));
    });

    stompClient.subscribe('/topic/chat', function (wrappedMessage) {
        showMessage(unwrapMessage(wrappedMessage));
    });

    stompClient.subscribe('/topic/system', function (wrappedMessage) {
        showSystemMessage(unwrapMessage(wrappedMessage));
    });

    sendMessageToWS('/app/join', user);
}

function showErrorMessage(errorMessage) {
    var errorMessageDiv = $('#error-message');
    errorMessageDiv.removeClass("hidden");
    errorMessageDiv.html('');
    errorMessageDiv.append(errorMessage);
}

function sendMessage(destination, message) {
    sendMessageToWS(destination, JSON.stringify({
        'content': message,
        'sender': user
    }));
}

function sendMessageToWS(destination, message) {
    stompClient.send(destination, {}, message);
}

function unwrapMessage(wrappedMessage) {
    var body = wrappedMessage.body;
    try {
        return JSON.parse(body);
    } catch (e) {
        return body;
    }
}

function showSystemMessage(message) {
    var messageDiv = '<div class = "message"><strong>' + message.content + '</strong>';
    $('#history').prepend('<div class="row system"><div class="col-xs-6 col-md-offset-3 col-xs-offset-1">'
        + messageDiv + '</div></div>');
  //  $('#history .row:first').css('margin-bottom', '10px');
}

function showMessage(message) {
    var messageDiv = '<div class = "message"><strong>' + message.sender + ': </strong>' + message.content + '</div>';
    if (message.sender)

    if (message.sender !== user) {
        var appendMargin = $('#history .row:first').hasClass('outgoing');
        $('#history').prepend(
            '<div class="row incoming"><div class="col-xs-4 col-md-offset-3 col-xs-offset-1">' + messageDiv + '</div></div>');
    } else {
        appendMargin = $('#history .row:first').hasClass('incoming');
        $('#history').prepend('<div class="row outgoing"><div class="col-xs-4 col-md-offset-5 col-xs-offset-7">' +
            messageDiv + '</div></div>');
    }
    if (appendMargin) $('#history .row:first').css('margin-bottom', '10px');
}


$(function () {
    $('#send').click(function () {
        var messageInput = $('#message');
        var message = messageInput.val();
        messageInput.val('');
        sendMessage('/app/sendMessage', message);
    });

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
        leave();
        window.location.href = "../leave";
    });

});

function leave() {
    sendMessageToWS('/app/leave', user);
    stompClient.disconnect();
}

$(window).on('beforeunload', function() {
    leave();
});