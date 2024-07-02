package za.co.wethinkcode;

public class Robot {
    private String name;
    private String state;  // You can expand this to a more detailed state representation if needed
    private int xPosition;
    private int yPosition;

    public Robot(String name, String state, int xPosition, int yPosition) {
        this.name = name;
        this.state = state;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
}
