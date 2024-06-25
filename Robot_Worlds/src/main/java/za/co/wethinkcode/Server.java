package za.co.wethinkcode;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(port);

            // Wait for client connection
            System.out.println("Server waiting for client on port " + port);
            Socket clientSocket = serverSocket.accept();

            // Client connected
            System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

            // Output stream to send data to client
            OutputStream outToClient = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToClient);

            // Send a message to client
            String message = "Thank you for connecting";
            out.writeUTF(message);

            // Close connection
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

