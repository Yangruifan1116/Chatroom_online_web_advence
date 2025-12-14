package org.example.chatroom_online_web_advence;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");

        ServletContext servletContext = getServletContext();

        Map<String, String> onlineUsers = (Map<String, String>) servletContext.getAttribute("onlineUsers");
        if (onlineUsers == null) {
            onlineUsers = new HashMap<>();
            servletContext.setAttribute("onlineUsers", onlineUsers);
        }

        List<Map<String, String>> chatMessages = (List<Map<String, String>>) servletContext.getAttribute("chatMessages");

        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
            servletContext.setAttribute("chatMessages", chatMessages);
        }

        if (onlineUsers.containsKey(username)) {
            request.setAttribute("error", "用户名已被占用，请更换其他用户名");
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);

        } else {
            HttpSession session = request.getSession();
            String sessionId = session.getId();

            session.setAttribute("currentUser", username);
            session.setAttribute("normalExit", false);
            onlineUsers.put(username, sessionId);

            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("sender", "系统消息");
            systemMessage.put("content", username + " 加入了聊天室");
            systemMessage.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            systemMessage.put("receiver", "所有人");
            systemMessage.put("type", "system");
            chatMessages.add(systemMessage);

            response.sendRedirect("chatroom.jsp");
        }
    }
}