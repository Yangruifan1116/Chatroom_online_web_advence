const messageForm = document.querySelector("#messageForm");
const exitForm = document.querySelector("#exitForm");
const messageContent = document.querySelector("#messageContent");
const userList = document.querySelector("#userList");
const messageContainer = document.querySelector("#messageContainer");
const userCount = document.querySelector("#userCount");
const receiverSelect = document.querySelector("#receiver");

let currentMessageCount = 0;

function updateChatData() {
    fetch('message')
        .then(response => response.text())
        .then(data => {
            if (!data) return;

            const parts = data.split('###');
            const users = parts[0] ? parts[0].split(',') : [];
            const messages = parts[1] ? parts[1].split('||') : [];

            updateUserList(users);
            updateReceiverList(users);
            updateMessageDisplay(messages);
        })
        .catch(error => console.error('Error updating chat data:', error));
}

function updateUserList(users) {
    while (userList.firstChild) {
        userList.removeChild(userList.firstChild);
    }

    let validUserCount = 0;
    for (let i = 0; i < users.length; i++) {
        const user = users[i];
        if (user && user.trim() !== '') {
            const li = document.createElement('li');
            li.textContent = user;
            userList.appendChild(li);
            validUserCount++;
        }
    }
    userCount.textContent = validUserCount;
}

function updateReceiverList(users) {
    const currentValue = receiverSelect.value;

    while (receiverSelect.options.length > 1) {
        receiverSelect.remove(1);
    }

    for (let i = 0; i < users.length; i++) {
        const user = users[i];
        if (user && user.trim() !== '') {
            const option = document.createElement('option');
            option.value = user;
            option.textContent = user;
            receiverSelect.appendChild(option);
        }
    }

    if (currentValue !== '所有人') {
        let found = false;
        for (let i = 0; i < receiverSelect.options.length; i++) {
            if (receiverSelect.options[i].value === currentValue) {
                receiverSelect.selectedIndex = i;
                found = true;
                break;
            }
        }
        if (!found) {
            receiverSelect.selectedIndex = 0;
        }
    }
}

function updateMessageDisplay(messages) {
    if (messages.length === 0 || messages[0].trim() === '') {
        return;
    }

    if (messages.length < currentMessageCount || messages.length - currentMessageCount > 10) {
        while (messageContainer.firstChild) {
            messageContainer.removeChild(messageContainer.firstChild);
        }
        currentMessageCount = 0;
    }

    for (let i = currentMessageCount; i < messages.length; i++) {
        const msgStr = messages[i];
        if (!msgStr.trim()) {
            continue;
        }

        const msgParts = msgStr.split('|');
        if (msgParts.length < 5) continue;

        const sender = msgParts[0];
        let content = msgParts[1];
        const time = msgParts[2];
        const receiver = msgParts[3];
        const type = msgParts[4];

        const messageDiv = document.createElement('div');


        const userSpan = document.createElement('span');
        userSpan.textContent = sender + " ";

        const timeSpan = document.createElement('span');
        timeSpan.textContent = "(" + time + ") ";

        const contentSpan = document.createElement('span');
        contentSpan.textContent = content;

        messageDiv.appendChild(userSpan);
        messageDiv.appendChild(timeSpan);
        messageDiv.appendChild(contentSpan);
        messageContainer.appendChild(messageDiv);
    }

    currentMessageCount = messages.length;
    messageContainer.scrollTop = messageContainer.scrollHeight;
}

setInterval(updateChatData, 2000);
updateChatData();

messageForm.addEventListener("submit", (e) => {
    const content = messageContent.value.trim();

    if (!content) {
        e.preventDefault();
        alert("消息内容不能为空");
        return;
    }

    const receiver = receiverSelect.value;
    if (receiver !== '所有人') {
        let receiverExists = false;
        for (let i = 0; i < receiverSelect.options.length; i++) {
            if (receiverSelect.options[i].value === receiver) {
                receiverExists = true;
                break;
            }
        }
        if (!receiverExists) {
            e.preventDefault();
            alert("选择的用户已不在线，请重新选择");
            receiverSelect.selectedIndex = 0;
            return;
        }
    }
});

exitForm.addEventListener("submit", (e) => {
    if (!confirm("确定要退出聊天室吗？")) {
        e.preventDefault();
    }
});