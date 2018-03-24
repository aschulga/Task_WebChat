var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var sendForm = document.querySelector('#sendForm');

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

var socket = null;

window.onload = function () {

    socket = new WebSocket('ws://localhost:8080/user');

    socket.onopen = function () {
        console.log('open');
    };

    socket.onerror = function (event) {
        console.log('error');
    };

    socket.onclose = function (event) {
        console.log('close');
    };

    socket.onmessage = function (event) {
        var str = event.data.split("|");

        if(str[0] == '4'){
            var parent = document.getElementById('chatlogs');

            var user = document.createElement("div");
            user.classList.add("user");

            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(str[2][0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(str[2]);
            user.appendChild(avatarElement);

            var p = document.createElement('p');

            if(str[1] == "agent"){
                p.classList.add('message-client');
                p.innerHTML = str[3];
            }
            else{
                p.classList.add('message-agent');
                p.innerHTML = str[3];
            }

            user.appendChild(p);
            parent.appendChild(user);
        }
        else{
            addUserStatusRecord(str);
        }
    };

    document.forms["usernameForm"].onsubmit = function () {

        var status = "client";

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

    document.forms["exitForm"].onsubmit = function () {
        var message = {
            code: "3"
        }
        socket.send(JSON.stringify(message));
        socket.close();
        return true;
    };

    document.forms["leaveForm"].onsubmit = function () {
        var message = {
            code: "2"
        }
        socket.send(JSON.stringify(message));
        return false;
    };

    document.forms["sendForm"].onsubmit = function () {
        var message = {
            code: "4",
            message: this.textarea.value
        }

        if(message.message != "") {
            socket.send(JSON.stringify(message));
            this.textarea.value = '';
        }
        return false;
    };
};

function addUserStatusRecord(str){

    var message;

    if (str[0] == '1') {
        message = str[1] + " " + str[2] + ' in the system';
    }
    else if (str[0] == '2') {
        message = str[1] + " " + str[2] + ' left in the system';
    }
    else if (str[0] == '3') {
        message = str[1] + " " + str[2] + ' exit in the system';
    }
    else if (str[0] == '5') {
        message = 'You connected with ' + str[1] + " " + str[2];
    }

    var parent = document.getElementById('chatlogs');
    var status = document.createElement("div");
    status.classList.add("status");

    var p = document.createElement('p');
    p.innerHTML = message;

    status.appendChild(p);
    parent.appendChild(status);
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}