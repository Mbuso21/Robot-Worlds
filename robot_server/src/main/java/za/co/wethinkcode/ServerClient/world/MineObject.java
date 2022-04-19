package za.co.wethinkcode.ServerClient.world;

import za.co.wethinkcode.ServerClient.robots.Robot;

/**
 * Minebject class that extends {@link WorldObject} class.
 */
public class MineObject extends WorldObject {
    private final int SIZE = 1;

    /**
     * MineObject constructor with two parameters
     * @param positionX the x coordinate
     * @param positionY the y coordinate
     */
    public MineObject(int positionX, int positionY) {
        super(positionX, positionY);
    }

    @Override
    public String blocksPosition(Position position) {
        if ( this.OBJ_POSITION.equals(position) ) {
            return "Mine";
        }
        return "OK";
    }

    @Override
    public boolean inRange(Robot robot) {
        Direction relativeDirection = relativeObjectDirection(robot.objectPosition(), this.OBJ_POSITION);
        setDistance(distanceFromObject(robot, relativeDirection));
        return this.OBJ_POSITION.inRange(robot);
    }

    /**
     * @return The size of the mine.
     */
    public int getSize() {
        return SIZE;
    }
}
