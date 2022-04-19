package za.co.wethinkcode.ServerClient.world;

import za.co.wethinkcode.ServerClient.robots.Robot;

/**
 * EdgeObject class that extends {@link WorldObject} class
 */
public class EdgeObject extends WorldObject {
    private final int CONSTANT_AXIS;

    /**
     * EdgeObject constructor with two parameters
     * @param positionX the x coordinate
     * @param positionY the y coordinate
     */
    public EdgeObject(int positionX, int positionY) {
        super(positionX, positionY);
        if ( positionX == 0 ) { CONSTANT_AXIS = positionY; }
        else {CONSTANT_AXIS = positionX;}
    }

    /**
     * Checks which edge of the world is a world object on.
     * @return a string message of the edge the object is on (NORTH, SOUTH, EAST or WEST)
     */
    private String whichEdge() {
        switch (this.objectPosition().getY()) {
            case 0:
                if (this.CONSTANT_AXIS > 0 ) {
                    return "EAST";
                }
                return "WEST";
            default:
                if ( this.CONSTANT_AXIS > 0 ) {
                    return "NORTH";
                }
                return "SOUTH";
        }
    }

    @Override
    public boolean inRange(Robot robot) {
//        return objectPosition().inRange(robot);
        Direction relativeDirection = relativeObjectDirection(robot.objectPosition(), this.OBJ_POSITION);
        switch ( whichEdge() ) {
            case "TOP":
                setDistance(CONSTANT_AXIS - robot.objectPosition().getY());
                return robot.objectPosition().getY() + robot.visibleRange() > this.CONSTANT_AXIS;
            case "BOTTOM":
                setDistance(robot.objectPosition().getY() - CONSTANT_AXIS);
                return robot.objectPosition().getY() - robot.visibleRange() < this.CONSTANT_AXIS;
            case "LEFT":
                setDistance(robot.objectPosition().getX() - CONSTANT_AXIS);
                return robot.objectPosition().getX() - robot.visibleRange() < this.CONSTANT_AXIS;
            default:
                setDistance(CONSTANT_AXIS - robot.objectPosition().getX());
                return robot.objectPosition().getX() + robot.visibleRange() > this.CONSTANT_AXIS;
        }
    }

    @Override
    public String blocksPosition(Position position) {
        if ( position.getY() == CONSTANT_AXIS || position.getX() == CONSTANT_AXIS ) {
            return "At the " + whichEdge() + " edge";
        }
        return "OK";
    }
}
