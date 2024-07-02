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
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private World world;
    private ObjectMapper objectMapper;

    public ClientHandler(Socket clientSocket, World world) {
        this.clientSocket = clientSocket;
        this.world = world;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String request;
            while ((request = reader.readLine()) != null) {
                try {
                    // Parse JSON request
                    JsonNode jsonRequest = objectMapper.readTree(request);

                    // Validate and process command
                    JsonNode robotNode = jsonRequest.get("robot");
                    JsonNode commandNode = jsonRequest.get("command");
                    JsonNode argumentsNode = jsonRequest.get("arguments");

                    if (robotNode == null || commandNode == null) {
                        sendErrorResponse(writer, "Missing robot or command");
                        continue;
                    }

                    String robotName = robotNode.asText();
                    String command = commandNode.asText().toLowerCase();

                    switch (command) {
                        case "launch":
                            String launchResult = world.launch(robotName, argumentsNode);
                            writer.println(launchResult);
                            break;
                        case "look":
                            String lookResult = world.look(robotName);
                            writer.println(lookResult);
                            break;
                        case "quit":
                            String quitResult = world.quit();
                            writer.println(quitResult);
                            break;
                        case "robots":
                            String robotsResult = world.robots();
                            writer.println(robotsResult);
                            break;
                        case "dump":
                            String dumpResult = world.dump();
                            writer.println(dumpResult);
                            break;
                        default:
                            sendErrorResponse(writer, "Unsupported command");
                    }

                } catch (IOException e) {
                    sendErrorResponse(writer, "Could not parse arguments");
                }
            }

            // Close resources
            reader.close();
            writer.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendErrorResponse(PrintWriter writer, String message) {
        ObjectNode errorResponse = objectMapper.createObjectNode();
        errorResponse.put("result", "ERROR");
        errorResponse.put("message", message);
        writer.println(errorResponse.toString());
    }
}
