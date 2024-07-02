package za.co.wethinkcode;

public class LakeObstacle implements Obstacle {
    private int bottomLeftX ;
    private int bottomLeftY ;
    static int lakeSize = 5;

    public LakeObstacle(int bottomLeftX, int bottomLeftY) {
        this.bottomLeftX = bottomLeftX;
        this.bottomLeftY = bottomLeftY;
    }

    @Override
    public int getBottomLeftX() {
        return bottomLeftX;
    }

    @Override
    public int getBottomLeftY() {
        return bottomLeftY;
    }

    @Override
    public int getSize() {
        // For lakes, we'll assume a fixed size of 1 unit
        return lakeSize;
    }

    @Override
    public boolean blocksPosition(Position position) {
        // Lakes are point obstacles, so check if the position matches the lake's coordinates
        return position.getX() >= getBottomLeftX() && position.getX() < getBottomLeftX() + lakeSize
                && position.getY() >= getBottomLeftY() && position.getY() < getBottomLeftY() + lakeSize;
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        // Since lakes are point obstacles, they don't block paths between positions
        if(a.getX() == b.getX() && b.getY() > a.getY() && bottomLeftX <= a.getX() && a.getX() <= bottomLeftX
                + 4 && a.getY() <= bottomLeftY && bottomLeftY <= b.getY()){
            return true;
        } else if (a.getX() == b.getX() && b.getY() < a.getY() && bottomLeftX <= a.getX() && a.getX() <= bottomLeftX
                + 4 && b.getY() <= bottomLeftY && bottomLeftY <= a.getY()) {
            return true;
        } else if (a.getY() == b.getY() && b.getX() > a.getX() && bottomLeftY <= a.getY() && a.getY() <= bottomLeftY +4
                && a.getX() <= bottomLeftX && bottomLeftX <= b.getX()) {
            return true;
        }else return a.getY() == b.getY() && b.getX()< a.getY() && bottomLeftY <= a.getY() && a.getY() <= bottomLeftY +4 &&
                b.getX() <= bottomLeftX && bottomLeftX <= a.getX();
    }
}

