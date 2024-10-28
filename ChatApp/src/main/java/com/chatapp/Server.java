package com.chatapp;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server {
    // A simple user database with usernames and passwords
    private static final HashMap<String, String> userDatabase = new HashMap<>();

    public static void main(String[] args) {
        // Initialize user credentials
        userDatabase.put("Rohit", "ROHIT_618");
        userDatabase.put("Smarty", "SMARTY_618");

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is running...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            // Authenticate and get the client's username
            String clientName = authenticateUser(input, output);
            if (clientName == null) {
                System.out.println("Authentication failed. Closing connection.");
                clientSocket.close();
                return;
            }
            System.out.println(clientName + " authenticated successfully! Chat session started.");

            // Start chat loop
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true) {
                String receivedMessage = input.readLine();
                if (receivedMessage == null || receivedMessage.equalsIgnoreCase("bye")) {
                    System.out.println(clientName + " ended the chat.");
                    break;
                }
                System.out.println(clientName + ": " + receivedMessage);

                System.out.print("You: ");
                message = consoleInput.readLine();
                output.println(message);

                if (message.equalsIgnoreCase("bye")) {
                    System.out.println("Ending chat...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Authentication method - returns the username if authenticated, otherwise null
    private static String authenticateUser(BufferedReader input, PrintWriter output) throws IOException {
        output.println("Enter username:");
        String username = input.readLine();

        output.println("Enter password:");
        String password = input.readLine();

        // Validate credentials
        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            output.println("Authentication successful!");
            return username;  // Return the authenticated username
        } else {
            output.println("Authentication failed!");
            return null;  // Return null if authentication fails
        }
    }
}
