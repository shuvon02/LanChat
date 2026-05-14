let stompClient = null;
let username = "";
let users = new Set();
let connected = false;
let selectedUser = null;

// set username
function setName() {
    username = document.getElementById("username").value;

    if (!username.trim()) {
        alert("Enter a name");
        return;
    }

    localStorage.setItem("chatUser", username);

    if (!connected) {
        connect();
    } else {
        sendJoin();
    }
}

// load saved name
window.onload = function () {
    const saved = localStorage.getItem("chatUser");

    if (saved) {
        username = saved;
        document.getElementById("username").value = saved;
    }

    connect();
};

function connect() {
    if (connected) return;

    const socket = new SockJS('/chat?username=' + username);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        console.log("Connected");

        connected = true;

        // group messages
        stompClient.subscribe('/topic/messages', function (msg) {
            const data = JSON.parse(msg.body);
            showGroupMessage(data);
        });

        // users
        stompClient.subscribe('/topic/users', function (msg) {
            addUser(msg.body);
        });

        // private messages
        stompClient.subscribe('/user/queue/messages', function (msg) {
            const data = JSON.parse(msg.body);
            showPrivateMessage(data);
        });

        if (username) {
            sendJoin();
        }
    });
}

function sendJoin() {
    stompClient.send("/app/join", {}, username);
}

// GROUP MESSAGE
function sendMessage() {
    if (!username) {
        alert("Set your name first");
        return;
    }

    const msgInput = document.getElementById("msg");
    const msg = msgInput.value;

    if (!msg.trim()) return;

    stompClient.send("/app/send", {}, JSON.stringify({
        sender: username,
        content: msg
    }));

    msgInput.value = "";
}

// PRIVATE MESSAGE
function sendPrivateMessage() {
    if (!selectedUser) {
        alert("Select a user first");
        return;
    }

    const input = document.getElementById("privateMsg");
    const msg = input.value;

    if (!msg.trim()) return;

    stompClient.send("/app/private", {}, JSON.stringify({
        sender: username,
        receiver: selectedUser,
        content: msg
    }));

    showPrivateMessage({
        sender: username,
        content: msg
    });

    input.value = "";
}

// DISPLAY GROUP MESSAGE
function showGroupMessage(data) {
    const li = document.createElement("li");
    li.classList.add("message");

    if (data.sender === username) {
        li.classList.add("you");
        li.textContent = "You: " + data.content;
    } else {
        li.classList.add("other");
        li.textContent = data.sender + ": " + data.content;
    }

    document.getElementById("messages").appendChild(li);

    autoScroll("messages");
}

// DISPLAY PRIVATE MESSAGE
function showPrivateMessage(data) {
    const li = document.createElement("li");

    li.textContent =
        (data.sender === username ? "You" : data.sender)
        + ": " + data.content;

    document.getElementById("privateMessages").appendChild(li);

    autoScroll("privateMessages");
}

// USER LIST
function addUser(user) {
    if (users.has(user)) return;

    users.add(user);

    const li = document.createElement("li");
    li.textContent = user;

    li.onclick = function () {
        selectedUser = user;
        document.getElementById("privateTitle").textContent =
            "Chat with " + user;

        document.getElementById("privateMessages").innerHTML = "";
    };

    document.getElementById("users").appendChild(li);
}

// SCROLL
function autoScroll(id) {
    const box = document.getElementById(id);
    box.scrollTop = box.scrollHeight;
}