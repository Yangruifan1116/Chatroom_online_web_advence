
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>在线聊天室</title>
    <script type="module" src="chatroom.js"></script>
</head>
<body>
<h2>在线聊天室 - 欢迎加入！</h2>

<form action="exit" method="post" id="exitForm">
    <input type="submit" id="exitBtn" value="点击退出聊天室">
</form>

<table>
    <tr>
        <td>
            <strong>在线用户（<span id="userCount">0</span>）</strong>
            <ul id="userList"></ul>
        </td>
        <td>
            <div id="messageContainer"></div>
        </td>
    </tr>
</table>

<form action="message" method="post" id="messageForm">
    <p>
        <label for="receiver">发送给：</label>
        <select id="receiver" name="receiver">
            <option value="所有人">所有人</option>
        </select>
    </p>
    <p>
        <label for="messageContent">请输入消息内容：</label>
        <textarea id="messageContent" name="messageContent" rows="5"
                  placeholder="在此输入消息内容..."></textarea>
    </p>
    <p>
        <input type="submit" id="sendBtn" value="点击发送消息">
    </p>
</form>

</body>
</html>