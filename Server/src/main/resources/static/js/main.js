var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var socket = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];


window.onload = function () {

    var url = document.location.pathname+'/page';
    console.log(url);

    socket = new WebSocket('ws://localhost:8080/user');

    socket.onopen = function () {
        console.log('open');
    };

    socket.onclose = function (event) {
        console.log('close');
    };

    socket.onmessage = function (event) {

        console.log('receive');

        var str = event.data.split("|");
        var messageElement = document.createElement('li');
        var message;

        if (str[0] == '1') {
            messageElement.classList.add('event-message');
            message = str[1] + " " + str[2] + ' in the system';
        }else if (str[0] == '2') {
            messageElement.classList.add('event-message');
            message = str[1] + " " + str[2] + ' left in the system';
        }else if (str[0] == '3') {
            messageElement.classList.add('event-message');
            message = str[1] + " " + str[2] + ' exit in the system';
        }
        else if (str[0] == '5') {
            messageElement.classList.add('event-message');
            message = 'You connected with ' + str[1] + " " + str[2];

        }
        else {
            message = str[3];
            messageElement.classList.add('chat-message');

            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(str[2][0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(str[2]);

            messageElement.appendChild(avatarElement);

            var usernameElement = document.createElement('span');
            var usernameText = document.createTextNode(str[2]);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message);
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;

        connectingElement.classList.add('hidden');
    };

    socket.onerror = function (event) {
        console.log('error');
    };

    document.forms["usernameForm"].onsubmit = function () {

        console.log('sendUsername');

        var status;
        if(document.location.pathname == "/client")
            status = "client";
        else
            status = "agent";

        var message = {
            code: "1",
            status: status,
            username: this.username.value
        }
        socket.send(JSON.stringify(message));
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');
        return false;
    };

    document.forms["messageFormLeave"].onsubmit = function () {
        var message = {
            code: "2"
        }
        socket.send(JSON.stringify(message));
        return false;
    };

    document.forms["messageFormExit"].onsubmit = function () {
        var message = {
            code: "3"
        }
        socket.send(JSON.stringify(message));
        socket.close();
        return true;
    };

    document.forms["messageFormSend"].onsubmit = function () {
        var message = {
            code: "4",
            message: this.message.value
        }
        if(message.message != "") {
            socket.send(JSON.stringify(message));
            document.forms["messageFormSend"].message = "";
        }
        return false;
    };
};

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}
