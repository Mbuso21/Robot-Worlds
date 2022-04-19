package za.co.wethinkcode.ServerClient.world;

import za.co.wethinkcode.ServerClient.robots.Robot;

/**
 * ObstacleObject class that extends {@link WorldObject} class
 */
public class ObstacleObject extends WorldObject {
    private final int SIZE = 5;
    private final Position BOTTOM_LEFT;
    private final Position TOP_LEFT;
    private final Position BOTTOM_RIGHT;
    private final Position TOP_RIGHT;

    /**
     * ObstacleObject constructor with two parameters
     * @param positionX the x coordinate
     * @param positionY the y coordinate
     */
    public ObstacleObject(int positionX, int positionY) {
        super(positionX, positionY);
        BOTTOM_LEFT  = objectPosition();
        TOP_LEFT     = new Position(positionX, positionY + SIZE);
        BOTTOM_RIGHT = new Position(positionX + SIZE, positionY);
        TOP_RIGHT    = new Position(positionX + SIZE, positionY + SIZE);
    }

    @Override
    public String blocksPosition(Position position) {
        if ( this.OBJ_POSITION.equals(position) ) {
            return "Obstructed";
        }
        return "OK";
    }

    @Override
    public boolean inRange(Robot robot) {
        boolean bottomLeftCornerInRange  = BOTTOM_LEFT.inRange(robot);
        boolean topLeftCornerInRange     = TOP_LEFT.inRange(robot);
        boolean bottomRightCornerInRange = BOTTOM_RIGHT.inRange(robot);
        boolean topRightCornerInRange    = TOP_RIGHT.inRange(robot);
        setDistance(distanceToClosestCorner(robot));
        return  bottomLeftCornerInRange || topLeftCornerInRange || bottomRightCornerInRange || topRightCornerInRange;
    }

    /**
     * @return The size of the obstacle.
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * Takes a  robot as a parameter and returns
     * the distance to the closest corner of the obstacle.
     * @param robot the robot in the world
     * @return the distance to the closest corner in number of steps
     */
    private int distanceToClosestCorner(Robot robot) {
        int bottomLeftDistance  = distanceToCorner(robot, BOTTOM_LEFT);
        int topLeftDistance     = distanceToCorner(robot, TOP_LEFT);
        int bottomRightDistance = distanceToCorner(robot, BOTTOM_RIGHT);
        int topRightDistance    = distanceToCorner(robot, TOP_RIGHT);

        int smallestDistanceA = Integer.min(bottomLeftDistance, bottomRightDistance);
        int smallestDistanceB = Integer.min(topLeftDistance, topRightDistance);
        return Integer.min(smallestDistanceA, smallestDistanceB);
    }

    /**
     * Takes 2 parameters of robot and position then returns the distance the
     * between the robot position and given position.
     * @param robot the robot in the world
     * @param cornerPosition a obstacle corner position
     * @return the distance to the closest corner in number of steps
     */
    private int distanceToCorner(Robot robot, Position cornerPosition) {
        Direction relativeDirection = relativeObjectDirection(robot.objectPosition(), cornerPosition);
        return distanceFromObject(robot, relativeDirection);

    }
}
