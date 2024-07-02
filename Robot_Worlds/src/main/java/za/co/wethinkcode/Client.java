package za.co.wethinkcode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Send commands to server
            sendRequest(writer, createRequest("robot1", "launch", new Object[]{}));
            String launchResponse = reader.readLine();
            System.out.println("Launch Response: " + launchResponse);

            sendRequest(writer, createRequest("robot1", "look", new Object[]{}));
            String lookResponse = reader.readLine();
            System.out.println("Look Response: " + lookResponse);

            sendRequest(writer, createRequest("robot1", "robots", new Object[]{}));
            String robotsResponse = reader.readLine();
            System.out.println("Robots Response: " + robotsResponse);

            sendRequest(writer, createRequest("robot1", "dump", new Object[]{}));
            String dumpResponse = reader.readLine();
            System.out.println("Dump Response: " + dumpResponse);

            sendRequest(writer, createRequest("robot1", "quit", new Object[]{}));
            String quitResponse = reader.readLine();
            System.out.println("Quit Response: " + quitResponse);

            // Close connection
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendRequest(PrintWriter writer, String request) {
        writer.println(request);
    }

    private static String createRequest(String robotName, String command, Object[] arguments) throws IOException {
        ObjectNode requestNode = objectMapper.createObjectNode();
        requestNode.put("robot", robotName);
        requestNode.put("command", command);
        ArrayNode argumentsNode = objectMapper.createArrayNode();
        for (Object argument : arguments) {
            argumentsNode.add(argument.toString());
        }
        requestNode.set("arguments", argumentsNode);
        return requestNode.toString();
    }
}
