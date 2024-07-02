package za.co.wethinkcode;

import java.util.Random;

public class Mountain {
    private int x;
    private int y;

    public Mountain(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // You can add more methods and properties as needed
    // Method to generate random obstacles within the range of the width and height
    public void generateObstacles() {
        Random random = new Random();
        int numObstacles = Math.max(5, random.nextInt(x * y / (y * x)));
        System.out.println("You have Mountains in your world...");
        for (int i = 0; i < numObstacles; i++) {
            int obstacleX = random.nextInt(x);
            int obstacleY = random.nextInt(y);
            // Here, you can create an obstacle object or print the obstacle coordinates
            System.out.println("Mountains generated at: (" + obstacleX + ", " + obstacleY + ")");
        }
    }
}

