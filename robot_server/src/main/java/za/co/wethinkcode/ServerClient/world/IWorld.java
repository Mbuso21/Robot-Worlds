package za.co.wethinkcode.ServerClient.world;

import za.co.wethinkcode.ServerClient.robots.Robot;

/**
 * IWorld interface
 */
public interface IWorld {

    /**
     * An enum set of directions - {@link IWorld.Direction#NORTH}, {@link IWorld.Direction#SOUTH},
     * {@link IWorld.Direction#WEST} and{@link IWorld.Direction#EAST}.
     */
    enum Direction {
            NORTH, SOUTH, WEST, EAST
    }

    /**
     * An enum set of statuses - {@link IWorld.Status#TODO}, {@link IWorld.Status#SETMINE}, {@link IWorld.Status#DEAD},
     * {@link IWorld.Status#RELOAD} and {@link IWorld.Status#REPAIR}.
     */
    enum Status {
        TODO, SETMINE, DEAD, RELOAD, REPAIR
    }

    /**
     * An enum set of messages - {@link IWorld.Message#DONE}, {@link IWorld.Message#OBSTRUCTED},
     * {@link IWorld.Message#FELL} and {@link WorldObject.Message#MINE}.
     */
    enum Message {
        DONE, OBSTRUCTED, FELL, MINE
    }

    /**
     * Updates the direction of the robot in the world.
     * @param turnRight the robot must turn right if true, else turn left if false
     * @param robot the robot that must get its direction updated
     */
    void updateDirection(boolean turnRight, Robot robot);
}
