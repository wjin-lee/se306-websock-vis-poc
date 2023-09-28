package org.example;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@WebSocket
public class MessageWebSocket extends WebSocketServlet {
    private Session session;
    private int counter = 0;

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(MessageWebSocket.class);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        System.out.println("WebSocket connected.");

        // Schedule a task to send a message every x seconds
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (session != null && session.isOpen()) {
                    try {
                        session.getRemote().sendString(String.format("Connection Heartbeat %d", counter));
                        counter++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 100); // Send a message every second
    }

    @OnWebSocketMessage
    public void onMessage(String message) throws IOException {
        System.out.println("Received message: " + message);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        this.session = null;
        System.out.println("WebSocket closed: " + reason);
    }
}