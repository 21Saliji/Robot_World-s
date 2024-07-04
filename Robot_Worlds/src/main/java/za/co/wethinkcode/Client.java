package za.co.wethinkcode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Prompt user for world dimensions and visibility
            int width = promptDimension("Enter the width of the world (limit 200): ");
            int height = promptDimension("Enter the height of the world (limit 200): ");
            int visibility = promptDimension("Enter the visibility of the world (limit 200): ");

            // Write configuration to file
            writeConfigToFile(width, height, visibility);

            // Send commands to server
            sendRequest(writer, createRequest("robot1", "launch", new Object[]{"type", 100, 10}));
            String launchResponse = reader.readLine();
            processResponse(launchResponse);

            // Continue with other commands...

            // Close connection
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int promptDimension(String prompt) {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        int dimension = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print(prompt);
                dimension = Integer.parseInt(consoleReader.readLine());
                if (dimension > 0 && dimension <= 200) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and 200.");
                }
            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return dimension;
    }

    private static void writeConfigToFile(int width, int height, int visibility) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"))) {
            writer.write("width=" + width + "\n");
            writer.write("height=" + height + "\n");
            writer.write("visibility=" + visibility + "\n");
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

    private static void processResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            String result = rootNode.get("result").asText();
            if ("OK".equals(result)) {
                // Successful response
                JsonNode stateNode = rootNode.get("state");
                if (stateNode != null) {
                    updateRobotState(stateNode);
                }
                // Process other data as needed
                JsonNode dataNode = rootNode.get("data");
                if (dataNode != null) {
                    // Process data
                }
            } else if ("ERROR".equals(result)) {
                // Error response
                JsonNode dataNode = rootNode.get("data");
                String errorMessage = null;
                if (dataNode != null) {
                    errorMessage = dataNode.get("message").asText();
                    System.err.println("Error: " + errorMessage);
                }
                // Handle specific error cases
                // Example: Server could not parse request arguments
                if ("Could not parse arguments".equals(errorMessage)) {
                    // Handle parse error
                }
                // Example: Unsupported command
                else if ("Unsupported command".equals(errorMessage)) {
                    // Handle unsupported command error
                }
                // Example: Generic internal service error
                else if ("An internal error has occurred".equals(errorMessage)) {
                    // Handle internal error
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateRobotState(JsonNode stateNode) {
        // Update local robot state based on stateNode
        int xPosition = stateNode.path("position").get(0).asInt();
        int yPosition = stateNode.path("position").get(1).asInt();
        String direction = stateNode.get("direction").asText();
        int shields = stateNode.get("shields").asInt();
        int shots = stateNode.get("shots").asInt();
        String status = stateNode.get("status").asText();

        // Example: Update your Robot object's state
        // robot.setPosition(xPosition, yPosition);
        // robot.setDirection(direction);
        // robot.setShields(shields);
        // robot.setShots(shots);
        // robot.setStatus(status);
    }
}
