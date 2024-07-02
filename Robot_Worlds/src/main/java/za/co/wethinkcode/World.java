package za.co.wethinkcode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import static za.co.wethinkcode.Client.objectMapper;

public class World {
    private int height;
    private int width;
    private static List<Robot> robots;

    public World(int height, int width) {
        this.height = height;
        this.width = width;
        this.robots = new ArrayList<>();
    }

    public void addRobot(Robot robot) {
        robots.add(robot);
    }

    public static List<Robot> getRobots() {
        return robots;
    }

    public static void removeAllRobots() {
        robots.clear();
    }

    public String launch(String robotName, JsonNode argumentsNode) {
        // Placeholder implementation
        // Randomly position the robot in an open space (not implemented fully)
        int x = 0; // Replace with logic to find an open space
        int y = 0; // Replace with logic to find an open space
        Robot newRobot = new Robot(robotName, "NORMAL", x, y);
        robots.add(newRobot);
        return createResponse("OK", "Launched " + robotName, createState(newRobot));
    }

    public String look(String robotName) {
        // Placeholder implementation
        // Implement logic to look around and gather information
        return createResponse("OK", "Looking around", createState(findRobotByName(robotName)));
    }

    public String quit() {
        // Disconnect all robots and end the world
        removeAllRobots();
        return createResponse("OK", "Disconnected all robots and ended the world.", null);
    }

    public String robots() {
        // List all robots in the world
        ArrayNode robotsArray = objectMapper.createArrayNode();
        for (Robot robot : robots) {
            ObjectNode robotNode = objectMapper.createObjectNode();
            robotNode.put("name", robot.getName());
            robotNode.put("state", robot.getState());
            robotsArray.add(robotNode);
        }
        return createResponse("OK", "List of robots", robotsArray);
    }

    public String dump() {
        // Implement logic to dump the state of the world (not fully implemented)
        return createResponse("OK", "World state dump", null);
    }

    private String createResponse(String result, String message, JsonNode stateNode) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("result", result);
        response.put("message", message);
        if (stateNode != null) {
            response.set("state", stateNode);
        }
        return response.toString();
    }

    private JsonNode createState(Robot robot) {
        // Example method to create state information for the robot (not fully implemented)
        ObjectNode stateNode = objectMapper.createObjectNode();
        stateNode.put("positionX", robot.getxPosition());
        stateNode.put("positionY", robot.getyPosition());
        stateNode.put("direction", "NORTH"); // Placeholder for direction
        stateNode.put("shields", 100); // Placeholder for shields
        stateNode.put("shots", 10); // Placeholder for shots
        stateNode.put("status", robot.getState());
        return stateNode;
    }

    private Robot findRobotByName(String robotName) {
        for (Robot robot : robots) {
            if (robot.getName().equals(robotName)) {
                return robot;
            }
        }
        return null;
    }
}
