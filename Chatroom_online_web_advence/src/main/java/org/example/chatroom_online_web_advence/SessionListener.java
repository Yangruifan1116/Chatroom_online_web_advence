package org.example.chatroom_online_web_advence;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletContext;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String username = (String) session.getAttribute("currentUser");

        if (username == null) {
            return;
        }

        ServletContext servletContext = session.getServletContext();
        Map<String, String> onlineUsers = (Map<String, String>)
                servletContext.getAttribute("onlineUsers");

        Boolean normalExit = (Boolean) session.getAttribute("normalExit");

        if (normalExit == null || !normalExit) {
            if (onlineUsers != null) {
                onlineUsers.remove(username);
            }

            addSystemMessage(servletContext, username + " 已离开聊天室");
        }
    }

    private void addSystemMessage(ServletContext servletContext, String content) {
        List<Map<String, String>> chatMessages = (List<Map<String, String>>) servletContext.getAttribute("chatMessages");

        if (chatMessages != null) {
            Map<String, String> message = new HashMap<>();
            message.put("sender", "系统消息");
            message.put("content", content);
            message.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            message.put("receiver", "所有人");
            message.put("type", "system");

            chatMessages.add(message);

        }
    }

}