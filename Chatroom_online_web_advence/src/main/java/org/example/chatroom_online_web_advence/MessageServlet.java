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

@WebServlet(name = "MessageServlet", value = "/message")
public class MessageServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("currentUser");

        String content = request.getParameter("messageContent");
        String receiver = request.getParameter("receiver");

        if (content == null || content.trim().isEmpty()) {
            response.sendRedirect("chatroom.jsp");
            return;
        }

        content = content.trim();
        if (receiver == null) {
            receiver = "所有人";
        }

        ServletContext servletContext = getServletContext();
        List<Map<String, String>> chatMessages = (List<Map<String, String>>)
                servletContext.getAttribute("chatMessages");

        Map<String, String> message = new HashMap<>();
        message.put("sender", username);
        message.put("content", content);
        message.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        message.put("receiver", receiver);

        if ("所有人".equals(receiver)) {
            message.put("type", "public");
        } else {
            message.put("type", "private");
        }

        chatMessages.add(message);

        if (chatMessages.size() > 100) {
            chatMessages.remove(0);
        }

        response.sendRedirect("chatroom.jsp");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String currentUser = (String) session.getAttribute("currentUser");

        ServletContext servletContext = getServletContext();
        List<Map<String, String>> chatMessages = (List<Map<String, String>>) servletContext.getAttribute("chatMessages");
        Map<String, String> onlineUsers = (Map<String, String>) servletContext.getAttribute("onlineUsers");

        StringBuilder userStr = new StringBuilder();
        for (String user : onlineUsers.keySet()) {
            userStr.append(user).append(",");
        }
        if (userStr.length() > 0) {
            userStr.setLength(userStr.length() - 1);
        }

        StringBuilder msgStr = new StringBuilder();
        for (Map<String, String> msg : chatMessages) {
            String type = msg.get("type");
            String sender = msg.get("sender");
            String receiver = msg.get("receiver");
            String content = msg.get("content");

            if ("system".equals(type) || "系统消息".equals(sender)) {
                content = "[系统消息] " + content;
            }
            else if ("private".equals(type)) {
                if (!sender.equals(currentUser) && !receiver.equals(currentUser)) {
                    continue;
                }
                content = "[私信消息] " + content;
            }
            else if ("public".equals(type)) {
                content = "[群聊消息] " + content;
            }

            String singleMsg = sender + "|" +
                    content + "|" +
                    msg.get("time") + "|" +
                    receiver + "|" +
                    type;
            msgStr.append(singleMsg).append("||");
        }
        if (msgStr.length() > 0) {
            msgStr.setLength(msgStr.length() - 2);
        }

        String result = userStr + "###" + msgStr;
        response.getWriter().write(result);
    }
}