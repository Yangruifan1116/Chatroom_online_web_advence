package org.example.chatroom_online_web_advence;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ExitServlet", value = "/exit")
public class ExitServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("currentUser");

        ServletContext servletContext = getServletContext();

        Map<String, String> onlineUsers = (Map<String, String>) servletContext.getAttribute("onlineUsers");
        onlineUsers.remove(username);

        List<Map<String, String>> chatMessages = (List<Map<String, String>>) servletContext.getAttribute("chatMessages");
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("sender", "系统消息");
        systemMessage.put("content", username + " 已退出聊天室");
        systemMessage.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()));
        systemMessage.put("receiver", "所有人");
        systemMessage.put("type", "system");
        chatMessages.add(systemMessage);


        session.setAttribute("normalExit", true);
        session.invalidate();

        response.sendRedirect("index.jsp");
    }
}