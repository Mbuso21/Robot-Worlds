package za.co.wethinkcode.ServerClient.world;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;

import java.util.HashMap;

import static java.lang.Math.abs;

/**
 * WorldObject class
 */
public abstract class WorldObject {
    protected Position OBJ_POSITION;
    private int distance;
    private final int VISIBILITY_STEPS;
    private final int MAX_RELOAD_SEC;
    private final int MAX_REPAIR_SEC;
    private final int MINE_SEC;
    private HashMap<Position, WorldObject> visibleObjects;

    /**
     * An enum set of directions - {@link Direction#NORTH}, {@link Direction#SOUTH}, {@link Direction#WEST} and
     * {@link Direction#EAST}.
     */
    public enum Direction {
        NORTH, SOUTH, WEST, EAST
    }

    /**
     * An enum set of statuses - {@link Status#NORMAL}, {@link Status#SETMINE}, {@link Status#DEAD},
     * {@link Status#RELOAD} and {@link Status#REPAIR}.
     */
    enum Status {
        NORMAL, SETMINE, DEAD, RELOAD, REPAIR
    }

    /**
     * An enum set of messages - {@link Message#DONE}, {@link Message#OBSTRUCTED},
     * {@link Message#FELL}, {@link Message#MINE} and {@link Message#EDGE}.
     */
    enum Message {
        DONE, OBSTRUCTED, FELL, MINE, EDGE,
    }

    /**
     * WorldObject class constructor that takes 2 parameters of type int as positional arguments
     * ,x-coordinate and y-coordinate.
     * @param positionX the x-coordinate
     * @param positionY the y-coordinate
     */
    public WorldObject(int positionX, int positionY) {
        this.OBJ_POSITION     = new Position(positionX, positionY);
        this.VISIBILITY_STEPS = 0;
        this.MAX_RELOAD_SEC   = 0;
        this.MAX_REPAIR_SEC   = 0;
        this.MINE_SEC         = 0;
    }

    /**
     * WorldObject class constructor that takes an int array as a parameter as world attributes.
     * @param worldAttributes the world attributes HashMAp
     */
    public WorldObject(HashMap<String, Integer> worldAttributes) {
        this.OBJ_POSITION     = null;
        this.VISIBILITY_STEPS = worldAttributes.get("visibility");
        this.MAX_RELOAD_SEC   = worldAttributes.get("reload");
        this.MAX_REPAIR_SEC   = worldAttributes.get("repair");
        this.MINE_SEC         = worldAttributes.get("mine");
    }

    /**
     * Checks the type of {@link WorldObject} the object is and returns the type of object.
     * @return the description of WorldObject as a string
     */
    public String objectType() {
        if (this instanceof Robot) {
            return "ROBOT";
        }
        if (this instanceof MineObject) {
            return "MINE";
        }
        if (this instanceof PitObject) {
            return "PIT";
        }
        if (this instanceof EdgeObject) {
            return "EDGE";
        }
        return "OBSTACLE";
    }

    /**
     * @return  the position of the object
     */
    public Position objectPosition() {
        return this.OBJ_POSITION;
    }

    /**
     * @return the visibility range of the identified object, in the number of steps
     */
    protected int visibleRange() {
        return this.VISIBILITY_STEPS;
    }

    /**
     * Takes a {@link Robot} as a parameter and checks if the object is in range.
     * @param robot the robot in the world
     * @return true if the object is in range else, false
     */
    public abstract boolean inRange(Robot robot);

    /**
     * Takes a {@link Robot} as a parameter and returns a {@link JSONObject} containing the robots data.
     * @param robot the robot in the world
     * @return robot data in json format
     */
    public JSONObject objectData(Robot robot) {
        JSONObject data = new JSONObject();
        Direction relativeDirection = relativeObjectDirection(robot.objectPosition(), this.OBJ_POSITION);
        data.put("type", this.objectType());
        data.put("distance", getDistance());
        data.put("direction", relativeDirection.toString());

        return data;
    }

    /**
     * @return the {@link Message} of the object
     */
    public Message objectMessage() {
        if ( this instanceof MineObject ) {
            return Message.MINE;
        }
        if ( this instanceof ObstacleObject ) {
            return Message.OBSTRUCTED;
        }
        if ( this instanceof PitObject ) {
            return Message.FELL;
        }
        if ( this instanceof EdgeObject ) {
            return Message.EDGE;
        }
        return Message.DONE;
    }

    /**
     * Takes 2 parameters of type {@link Position} and returns the absolute value
     * of the positional difference between the y-values of the two positions.
     * @param robotPosition the position of the robot
     * @param position the position of the object
     * @return positional y difference
     */
    public static int positionalDifferenceY(Position robotPosition, Position position) {
        return abs(robotPosition.getY() - position.getY());
    }

    /**
     * Takes 2 parameters of type {@link Position} and returns the absolute value
     * of the positional difference between x-values of the two positions.
     * @param robotPosition the position of the robot
     * @param position the position of the object
     * @return positional y difference
     */
    public static int positionalDifferenceX(Position robotPosition, Position position) {
        return abs(robotPosition.getX() - position.getX());
    }

    /**
     * Takes 2 parameters of type {@link Position} and returns the
     * {@link WorldObject#relativeHorizontalDirection(Position, Position)} of the object if the
     * {@link WorldObject#positionalDifferenceY(Position, Position)} is greater than the
     * {@link WorldObject#positionalDifferenceX(Position, Position)}, else it returns the
     * {@link WorldObject#relativeVerticalDirection(Position, Position)} of the object.
     * @param robotPosition the position of the robot
     * @param position the position of the object
     * @return the direction of the object
     */
    public static Direction relativeObjectDirection(Position robotPosition, Position position) {
        if ( positionalDifferenceX(robotPosition, position) < positionalDifferenceY(robotPosition, position) ) {
            return relativeVerticalDirection(robotPosition, position);
        } else {
            return relativeHorizontalDirection(robotPosition, position);
        }
    }

    /**
     * Takes 2 parameters of type {@link Position} and returns the relative horizontal
     * direction. It returns {@link Direction#EAST} if the object's positional x-value
     * is greater than the reference's positional x-value, else it returns {@link Direction#WEST} .
     * @param reference the position of the reference
     * @param object the position of the object
     * @return the direction of the object
     */
    public static Direction relativeHorizontalDirection(Position reference, Position object) {
        if ( reference.getX() < object.getX() ) {
            return Direction.EAST;
        }
        return Direction.WEST;
    }

    /**
     * Takes 2 parameters of type {@link Position} and returns the relative vertical
     * direction. It returns {@link Direction#NORTH} if the object's positional y-value
     * is greater than the reference's positional y-value, else it returns {@link Direction#SOUTH} .
     * @param reference the position of the reference
     * @param object the position of the object
     * @return the direction of the object
     */
    public static Direction relativeVerticalDirection(Position reference, Position object) {
        if ( reference.getY() < object.getY() ) {
            return Direction.NORTH;
        }
        return Direction.SOUTH;
    }

    /**
     * Takes 2 parameters of type {@link Position} and returns the distance of the robot from the object.
     * @param robot the robot object in the world
     * @param relativeDirection the relative direction of the object
     * @return
     */
    protected int distanceFromObject(Robot robot, Direction relativeDirection) {
        switch (relativeDirection) {
            case NORTH:
            case SOUTH:
                return positionalDifferenceY(robot.objectPosition(), this.OBJ_POSITION);
            default:
                return positionalDifferenceX(robot.objectPosition(), this.OBJ_POSITION);
        }
    }

    /**
     * Takes a {@link Position} as a parameter and updates the object's position.
     * @param newPosition
     */
    public void updateObjectPosition(Position newPosition) {
        this.OBJ_POSITION = newPosition;
    }

    /**
     * Takes a {@link Position} as a parameter and checks if the object blocks this position.
     * @param position the position to check
     * @return description of the result
     */
    public abstract String blocksPosition(Position position);

    /**
     * Takes 2 parameters of type {@link Position} and checks if this
     * object blocks the path between the two position
     * @param a position at the one endpoint
     * @param b position at the other endpoint
     * @return short description whether it blocks the path or not
     */
    public String blocksPath(Position a, Position b) {
        int min;
        int max;
        String message;

//      Movement on the y-axis, check vertical movement/positions
        if ( a.getX() == b.getX() ) {
            max = Integer.max(a.getY(), b.getY());
            min = Integer.min(a.getY(), b.getY());
            for (int i = min; i <= max; i++) {
                message = blocksPosition(new Position(a.getX(), i));
                if (message.equals("OK")) {
                    continue;
                }
                return message;
            }
        }
//      Movement on the x-axis, check horizontal movement/positions
        else if (a.getY() == b.getY()) {
            max = Integer.max(a.getX(), b.getX());
            min = Integer.min(a.getX(), b.getX());
            for (int i = min; i <= max; i++) {
                message = blocksPosition(new Position(a.getX(), i));
                if (message.equals("OK")) {
                    continue;
                }
                return message;
            }
        }
        return "Done";
    }

    /**
     * @return The object's distance, in number of steps.
     */
    public int getDistance() {
        return this.distance;
    }

    /**
     * Sets the object's distance.
     * @param distance length in number of steps
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }
}
