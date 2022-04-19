package za.co.wethinkcode.ServerClient.world;

import za.co.wethinkcode.ServerClient.robots.Robot;

/**
 * Position class
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Position constructor that takes 2 parameters
     * @param x position's x coordinate
     * @param y position's y coordinate
     */
    public Position(int x, int y) {
        this.x= x;
        this.y = y;
    }

    /**
     * @return The positions x coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return The positions y coordinate.
     */
    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (this.x != position.getX()) return false;

        return this.y == position.getY();
    }

    /**
     * Takes a {@link Robot} as a parameter and checks if the object is in range.
     * @param robot the robot in the world
     * @return true if the object is in range else, false
     */
    public boolean inRange(Robot robot) {
        Position reference = robot.objectPosition();
        Position rangeTopLeft    = new Position(reference.getX() - robot.visibleRange(), reference.getY() + robot.visibleRange());
        Position rangeBottomRight = new Position(reference.getX() + robot.visibleRange(), reference.getY() - robot.visibleRange());
        return this.isIn(rangeTopLeft, rangeBottomRight);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    /**
     * Takes a position string in the format '[x,y]' where x and y values are numerical and creates
     * a type Position position
     * @param positionString position string in the format '[x,y]'
     */
    public static Position createFromString(String positionString) {
        positionString = positionString.replaceAll("[^0-9,]", "");
        String[] position = positionString.split(",");

        return new Position(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
    }

    /**
     * Takes 2 parameters and returns whether this position is within the world grid
     * @param topLeft the top left corner of the world
     * @param bottomRight the bottom right corner of the world
     * @return true if this position is within the edges else, false
     */
    public boolean isIn(Position topLeft, Position bottomRight) {
        boolean withinTop = this.y <= topLeft.getY();
        boolean withinBottom = this.y >= bottomRight.getY();
        boolean withinLeft = this.x >= topLeft.getX();
        boolean withinRight = this.x <= bottomRight.getX();
        return withinTop && withinBottom && withinLeft && withinRight;
    }
}
