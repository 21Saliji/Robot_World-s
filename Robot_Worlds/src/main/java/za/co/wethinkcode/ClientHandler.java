package za.co.wethinkcode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private World world;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ClientHandler(Socket clientSocket, World world) {
        this.clientSocket = clientSocket;
        this.world = world;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request;
            while ((request = reader.readLine()) != null) {
                String response = handleRequest(request);
                writer.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String handleRequest(String request) {
        try {
            JsonNode rootNode = objectMapper.readTree(request);
            String robotName = rootNode.get("robot").asText();
            String command = rootNode.get("command").asText();
            JsonNode argumentsNode = rootNode.get("arguments");

            switch (command) {
                case "launch":
                    return handleLaunch(robotName, argumentsNode);
                case "look":
                    return handleLook(robotName);
                case "state":
                    return handleState(robotName);
                case "forward":
                    return handleMove(robotName, "forward", argumentsNode);
                case "back":
                    return handleMove(robotName, "back", argumentsNode);
                case "turn":
                    return handleTurn(robotName, argumentsNode);
                case "repair":
                    return handleRepair(robotName);
                case "reload":
                    return handleReload(robotName);
                case "mine":
                    return handleMine(robotName);
                case "fire":
                    return handleFire(robotName);
                case "world":
                    return handleWorld();
                default:
                    return createErrorResponse("Unsupported command");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return createErrorResponse("Could not parse arguments");
        }
    }

    private String handleLaunch(String robotName, JsonNode argumentsNode) {
        try {
            String kind = argumentsNode.get(0).asText();
            int maxShields = argumentsNode.get(1).asInt();
            int maxShots = argumentsNode.get(2).asInt();
            return world.launchRobot(robotName, kind, maxShields, maxShots);
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
            return createErrorResponse("Could not parse arguments");
        }
    }

    private String handleLook(String robotName) {
        return world.look(robotName);
    }

    private String handleState(String robotName) {
        return world.getRobotState(robotName);
    }

    private String handleMove(String robotName, String direction, JsonNode argumentsNode) {
        try {
            int steps = argumentsNode.get(0).asInt();
            return world.moveRobot(robotName, direction, steps);
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
            return createErrorResponse("Could not parse arguments");
        }
    }

    private String handleTurn(String robotName, JsonNode argumentsNode) {
        try {
            String direction = argumentsNode.get(0).asText();
            return world.turnRobot(robotName, direction);
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
            return createErrorResponse("Could not parse arguments");
        }
    }

    private String handleRepair(String robotName) {
        return world.repairRobot(robotName);
    }

    private String handleReload(String robotName) {
        return world.reloadRobot(robotName);
    }

    private String handleMine(String robotName) {
        return world.mineRobot(robotName);
    }

    private String handleFire(String robotName) {
        return world.fireRobot(robotName);
    }

    private String handleWorld() {
        return world.getWorldParameters();
    }

    private String createErrorResponse(String message) {
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("result", "ERROR");
        ObjectNode dataNode = objectMapper.createObjectNode();
        dataNode.put("message", message);
        errorNode.set("data", dataNode);
        return errorNode.toString();
    }
}
