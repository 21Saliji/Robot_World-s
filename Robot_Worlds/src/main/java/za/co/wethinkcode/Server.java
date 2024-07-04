package za.co.wethinkcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        int width = 0, height = 0, visibility = 0, reloadTime = 0, repairTime = 0, mineTime = 0, maxShields = 0, maxShots = 0;

        // Read configuration from file
        try (BufferedReader configReader = new BufferedReader(new FileReader("config.txt"))) {
            String line;
            while ((line = configReader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    switch (parts[0]) {
                        case "width":
                            width = Integer.parseInt(parts[1]);
                            break;
                        case "height":
                            height = Integer.parseInt(parts[1]);
                            break;
                        case "visibility":
                            visibility = Integer.parseInt(parts[1]);
                            break;
                        case "reload":
                            reloadTime = Integer.parseInt(parts[1]);
                            break;
                        case "repair":
                            repairTime = Integer.parseInt(parts[1]);
                            break;
                        case "mine":
                            mineTime = Integer.parseInt(parts[1]);
                            break;
                        case "max-shields":
                            maxShields = Integer.parseInt(parts[1]);
                            break;
                        case "max-shots":
                            maxShots = Integer.parseInt(parts[1]);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create world instance with configuration
        World world = new World(width, height, visibility, reloadTime, repairTime, mineTime, maxShields, maxShots);

        // Start server
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and waiting for client on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

                // Start a new thread to handle the client
                ClientHandler clientHandler = new ClientHandler(clientSocket, world);
                Thread handlerThread = new Thread(clientHandler);
                handlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
