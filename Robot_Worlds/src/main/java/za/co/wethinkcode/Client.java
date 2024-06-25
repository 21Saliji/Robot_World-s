package za.co.wethinkcode;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverName = "localhost";
        int port = 12345;

        try {
            // Connect to server
            Socket clientSocket = new Socket(serverName, port);

            // Input stream to receive data from server
            InputStream inFromServer = clientSocket.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            // Receive message from server
            String message = in.readUTF();
            System.out.println("Received from server: " + message);

            // Close connection
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

