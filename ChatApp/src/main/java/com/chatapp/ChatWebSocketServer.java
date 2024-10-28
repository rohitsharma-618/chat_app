package com.chatapp;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class ChatWebSocketServer extends WebSocketServer {
    private static final HashMap<String, String> userDatabase = new HashMap<>();
    private HashMap<WebSocket, String> clients = new HashMap<>();

    public ChatWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection: " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        conn.send("Enter username:");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (clients.get(conn) == null) {
            authenticateUser(conn, message);
        } else {
            broadcast(message);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    private void authenticateUser(WebSocket conn, String message) {
        String[] credentials = message.split("\n");
        if (credentials.length == 2) {
            String username = credentials[0];
            String password = credentials[1];
            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                clients.put(conn, username);
                conn.send("Authentication successful!");
            } else {
                conn.send("Authentication failed!");
            }
        }
    }

    public static void main(String[] args) {
        userDatabase.put("Rohit", "ROHIT_618");
        userDatabase.put("Smarty", "SMARTY_618");

        ChatWebSocketServer server = new ChatWebSocketServer(5000);
        server.start();
        System.out.println("Chat Server started on port: " + server.getPort());
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onStart'");
    }
}
