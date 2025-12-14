<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>在线聊天室-登录</title>
    <script type="module" src="login.js"></script>
</head>
<body>
<h2>在线聊天室</h2>

<form action="login" method="post" id="loginForm">
    <label for="username">请输入您的用户名：</label>
    <input type="text" id="username" name="username" required>
    <input type="submit" id="loginBtn" value="进入聊天室">
</form>

<div id="errorMessage">
    <c:if test="${error != null}">
        <p class="error">${error}</p>
    </c:if>
</div>

</body>
</html>