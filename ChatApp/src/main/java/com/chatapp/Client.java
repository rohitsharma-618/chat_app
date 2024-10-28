package com.chatapp;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Perform authentication
            System.out.println(input.readLine()); // "Enter username:"
            String username = consoleInput.readLine();
            output.println(username);

            System.out.println(input.readLine()); // "Enter password:"
            String password = consoleInput.readLine();
            output.println(password);

            // Check authentication result
            String authResponse = input.readLine();
            System.out.println(authResponse);

            if (!authResponse.equalsIgnoreCase("Authentication successful!")) {
                System.out.println("Authentication failed. Exiting...");
                return;
            }

            // Start chat session
            String message;
            while (true) {
                System.out.print("You: ");
                message = consoleInput.readLine();
                output.println(message);

                if (message.equalsIgnoreCase("bye")) {
                    System.out.println("Chat ended.");
                    break;
                }

                String receivedMessage = input.readLine();
                if (receivedMessage == null || receivedMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Server ended the chat.");
                    break;
                }
                System.out.println("Server: " + receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
