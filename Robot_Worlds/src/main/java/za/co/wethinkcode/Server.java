package za.co.wethinkcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;
    private static final int MAX_DIMENSION = 200;

    public static void main(String[] args) {
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            int width = getInputDimension(consoleReader, "Enter the width of the world (limit 200): ");
            int height = getInputDimension(consoleReader, "Enter the height of the world (limit 200): ");
            System.out.println("Width: " + width + ", Height: " + height);

            World world = new World(height, width);

            ServerSocket serverSocket = new ServerSocket(PORT);
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

    private static int getInputDimension(BufferedReader reader, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int dimension = Integer.parseInt(reader.readLine());
                if (dimension > 0 && dimension <= MAX_DIMENSION) {
                    return dimension;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and " + MAX_DIMENSION);
                }
            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
