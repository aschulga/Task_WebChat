var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var numberTab = "1";

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

var socket = new WebSocket('ws://localhost:8080/user');

socket.onopen = function () {
    console.log('open');
};

socket.onerror = function (event) {
    alert('Sorry, there was an error. ' +
        'Click OK and close the page.');
};

socket.onclose = function (event) {
    alert('Sorry, there was an error. ' +
        'Click OK and close the page.');
};

socket.onmessage = function (event) {
    var str = event.data.split("|");
    if (str[0] == '4') {
        var parent = document.getElementById(str[4]);
        var user = document.createElement("div");
        user.classList.add("user");

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(str[2][0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(str[2]);
        user.appendChild(avatarElement);
        var p = document.createElement('p');
        if (str[1] == "agent") {
            p.classList.add('message-agent');
            p.innerHTML = str[3];
        }
        else {
            p.classList.add('message-client');
            p.innerHTML = str[3];
        }
        user.appendChild(p);
        parent.appendChild(user);
    }
    else {
        addUserStatusRecord(str);
        if (str[0] == "5") {
            var message = {
                code: "5",
                tabId: '#tab' + getNumberTab()
            };
            socket.send(JSON.stringify(message));
        }
    }
};

function addUserStatusRecord(str){
    var message;

    if (str[0] == '1') {
        message = str[1] + " " + str[2] + ' in the system';
    }
    else if (str[0] == '2') {
        message = str[1] + " " +str[2] + ' left in the system';
        deleteFormForInputInTab(str[3]);
    }
    else if (str[0] == '3') {
        message = str[1] + " " + str[2] + ' exit in the system';
        deleteFormForInputInTab(str[3]);
    }
    else if (str[0] == '5') {
        addTab();
        message = 'You connected with ' + str[1] + " " + str[2];
    }

    var parent = document.getElementById('#tab'+getNumberTab());
    var status = document.createElement("div");
    status.classList.add("status");

    var p = document.createElement('p');
    p.innerHTML = message;

    status.appendChild(p);
    parent.appendChild(status);
}

$(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function (e) {
    var tab = $(e.target);
    var contentId = tab.attr("href");
    if (tab.parent().hasClass('active')) {
        var str = contentId.substr(4);
        console.log("qqqq "+str);
        setNumberTab(str);
    }
});

document.forms["usernameForm"].onsubmit = function () {
    var status = "agent";
    var message = {
        code: "1",
        status: status,
        username: this.username.value,
        numberClient:this.numberClient.value,
        tabId: '#tab'+getNumberTab()
    };
    socket.send(JSON.stringify(message));
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');
    return false;
};

function sendMessage(){
    var mes = document.getElementById("textarea"+getNumberTab()).value;
    var message = {
        code: "4",
        status: "agent",
        message: mes,
        tabId: '#tab'+getNumberTab()
    };

    if(message.message != "") {
        socket.send(JSON.stringify(message));
        document.getElementById("textarea"+getNumberTab()).value = '';
    }
}

function endChatWithClient(){
    var message = {
        code: "3",
        tabId: '#tab'+getNumberTab()
    };
    deleteFormForInputInTab('#tab'+getNumberTab());
    socket.send(JSON.stringify(message));
    return false;
}

function addTab(){
    var nextTab = $('#tabs li').size()+1;

    // create the tab
    $('<li><a id="tab'+nextTab+'-Id" href="#tab'+nextTab+'" data-toggle="tab">Tab '+nextTab+'</a></li>').appendTo('#tabs');

    // create the tab content
    $('<div class="tab-pane" id="tab'+nextTab+'">' +
        '<div class="chatlogs" id="#tab'+nextTab+'">' +
        '</div>' +
        '</div>').appendTo('.tab-content');

    $('<form class="sendForm'+nextTab+'" id="sendForm">' +
        ' <textarea id="textarea'+nextTab+'" placeholder="Type a message...">' +
        '</textarea>' +
        '</form>').appendTo('#tab'+nextTab);

    $('<button class="sendButton'+nextTab+'" id="sendButton" onclick="sendMessage()">Send'+
        '</button>').appendTo('#tab'+nextTab);

    $('<button class="exitButton'+nextTab+'" id="exitButton" onclick="endChatWithClient()">Exit'+
        '</button>').appendTo('#tab'+nextTab);

    // make the new tab active
    $('#tabs a:last').tab('show');
}

function deleteTab(name){
    $('#tab'+getNumberTab()).remove();
    $('#tab'+getNumberTab()+'-Id').remove();
}

function deleteFormForInputInTab(name){
    var str = name.substr(4);
    setNumberTab(str);
    $('.sendForm'+str).remove();
    $('.sendButton'+str).remove();
    $('.exitButton'+str).remove();

    $('<button class="exitButton" id = "exitButtonTab" onclick="deleteTab()">Exit' +
        '</button>').appendTo('#tab'+str);
}

function getNumberTab(){
    return numberTab;
}

function setNumberTab(number){
    numberTab = number;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}