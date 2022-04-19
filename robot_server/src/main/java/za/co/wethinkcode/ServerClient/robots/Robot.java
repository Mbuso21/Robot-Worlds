package za.co.wethinkcode.ServerClient.robots;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;
import za.co.wethinkcode.ServerClient.world.IWorld;
import za.co.wethinkcode.ServerClient.world.Position;
import za.co.wethinkcode.ServerClient.world.WorldObject;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

/**
 * Robot class that extends the {@link WorldObject} class.
 */
public class Robot extends WorldObject {
    private final HashMap<String, String> attributes = new HashMap<>();
    private HashMap<Direction, Entry<WorldObject, Integer>> visibleObjects = new HashMap<>();
    private final String name;
    private JSONObject state;
    private AbstractWorld world;
    private Position position;
    private Direction currentDirection = Direction.NORTH;
    private IWorld.Status status = IWorld.Status.TODO;
    private int MAX_SHOTS;
    private int MAX_SHIELDS;
    private String make;
    private int shots = 0;
    private int shields = 0;

    /**
     * Robot class constructor that take 2 parameters
     * @param clientName string with the client name
     * @param worldAttributes HashMap containing worldAttributes
     */
    public Robot(String clientName, HashMap<String, Integer> worldAttributes) {
        super(worldAttributes);
        this.name = clientName;
    }

    /**
     * Robot class constructor that takes 4 parameters, then returns a robot
     * configured with the params values as the robot's attributes.
     * @param make the robot type
     * @param name the robot name
     * @param shots the robot's number of shots
     * @param shields the robot's number of shields
     */
    public Robot(String make, String name, int shots, int shields) {
        super(0,0);
        this.make = make;
        this.name = name;
        this.shots = shots;
        this.shields = shields;
    }

    /**
     * Takes 2 parameters, then returns a robot configured
     * with the params values as the robot's attributes.
     * @param make the robot type
     * @param name the robot name
     * @return robot object
     */
    public static Robot make(String make, String name) {
        switch (make.toLowerCase()) {
//            case "sniper":
//                return new SniperRobot(name);
//            case "tank":
//                return new DemolitionRobot.TankRobot(name);
//            case "demolition":
//                return new DemolitionRobot(name);
            default : return new SniperRobot(name);
        }
    }

    /**
     * @return The name of the robot.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Takes a {@link JSONArray} as a parameter containing the robot's attributes and sets them.
     * @param attributes the robot attributes to set
     */
    public void setAttributes(JSONArray attributes) {
        String[] parameters = {"kind", "shields", "shots"};
        for (int i = 0; i < attributes.size(); i++) {
            this.attributes.put(parameters[i], (String) attributes.get(i));
        }
        MAX_SHOTS = Integer.parseInt(this.attributes.get("shots"));
        MAX_SHIELDS = Integer.parseInt(this.attributes.get("shields"));
    }

    @Override
    public boolean inRange(Robot robot) {
        Position topRangeLeft     = new Position(robot.objectPosition().getX() - robot.visibleRange(), robot.objectPosition().getY() + robot.visibleRange());
        Position bottomRangeRight = new Position(robot.objectPosition().getX() + robot.visibleRange(), robot.objectPosition().getY() - robot.visibleRange());
        return this.objectPosition().isIn(topRangeLeft, bottomRangeRight);
    }

    /**
     * Takes a {@link Position} as a parameter and sets the robot's position.
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
        this.updateObjectPosition(this.position);
    }

    /**
     * @return The robot's current direction.
     */
    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    /**
     * Takes a {@link za.co.wethinkcode.ServerClient.world.WorldObject.Direction}
     * as a parameter and sets the robot's direction.
     * @param direction the direction to set
     */
    public void setCurrentDirection(Direction direction) {
        this.currentDirection = direction;
    }

    /**
     * Takes a {@link IWorld.Status} as a parameter and sets the robot's status.
     * @param status the status to set
     */
    public void setStatus(IWorld.Status status) {
        this.status = status;
    }

    /**
     * @return The state of the robot as a json object.
     */
    public JSONObject getState() {
        state = new JSONObject();
        state.put("direction", this.currentDirection.toString());
        state.put("shields", attributes.get("shields"));
        state.put("shots", attributes.get("shots"));
        state.put("status", this.status.toString());
        return state;
    }

    /**
     * @return The number of the shields the robot has.
     */
    public String getShields() {
        return attributes.get("shields");
    }

    /**
     * Sets the robot's number of shields.
     * @param shields the number of shields
     */
    public void setShields(String shields) {
        attributes.put("shields", shields);
    }

    /**
     * @return The number of max shots the robot can have.
     */
    public int getMaxShots() {
        return MAX_SHOTS;
    }

    /**
     * @return The number of max shields the robot can have.
     */
    public int getMaxShields() {
        return MAX_SHIELDS;
    }

    /**
     * @return The number of shots the robot has.
     */
    public String getShots() {
        return attributes.get("shots");
    }

    /**
     * Sets the robot's number of shots.
     * @param shots the number of shots
     */
    public void setShots(String shots) {
        attributes.put("shots", shots);
    }

    @Override
    public String blocksPosition(Position position) {
        if ( this.position.equals(position) ) {
            return "Hit";
        }
        return "OK";
    }

//    public String translateStatus(IWorld.Status status) {
//        switch (status) {
//            case NORMAL:
//                return "NORMAL";
//            case DEAD:
//                return "DEAD";
//            case RELOAD:
//                return "RELOAD";
//            case REPAIR:
//                return "REPAIR";
//            default:
//                return "SETMINE";
//        }
//    }

    /**
     * Takes a {@link HashMap} of world objects as a parameter and returns a list of objects that
     * are in the robot's range - a step away from the robot.
     * @param worldObjects a HashMap of all world object (i.e. obstacles, mines and pits)
     * @return a list containing objects in range - objects closest to the robot in all 4 directions
     * (north, south, east and west)
     */
    public List<WorldObject> getObjectsInRange(HashMap<String, WorldObject> worldObjects) {
        for (WorldObject object: worldObjects.values()) {
            if ( object != this && !object.objectType().equals("EDGE") && object.inRange(this)) {
                Entry<Direction, Integer> objectDirectionAndDistancePair =
                        getDirectionAndDistanceToObject(object.objectPosition());

                if (objectDirectionAndDistancePair != null) {
                    if (shouldAddObjectToVisibleObjects(objectDirectionAndDistancePair)) {
                        object.setDistance(objectDirectionAndDistancePair.getValue());
                        visibleObjects.put(objectDirectionAndDistancePair.getKey(),
                                new SimpleEntry<>(object, objectDirectionAndDistancePair.getValue()));
                    }
                }
            }
        }
        return visibleObjects.values().stream().map(Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Takes a map entry of a world object as a parameter and returns a boolean whether it should
     * add the world object to the robot's visible objects list.
     * @param objectDirectionAndDistancePair the key-value pair constructed from a world object.
     * @return true, if the entry's key does not exist or the key exists and its value
     * is larger than the existing value, else false.
     */
    private Boolean shouldAddObjectToVisibleObjects(Entry<Direction, Integer> objectDirectionAndDistancePair) {
        if ( visibleObjects.containsKey(objectDirectionAndDistancePair.getKey()) ) {
            Entry<WorldObject, Integer> existingPair = visibleObjects.get(objectDirectionAndDistancePair.getKey());
            return existingPair.getValue() > objectDirectionAndDistancePair.getValue();
        }
        return true;
    }

    /**
     * Takes a world object's position as a parameter and returns a map entry containing the robot's direction towards
     * the object and distance between them.
     * @param objectPosition the world object's position
     * @return map entry of a direction and integer key-value pair
     */
    private Entry<Direction, Integer> getDirectionAndDistanceToObject(Position objectPosition) {
        /* Check if objects are on the vertical plane (either the NORTH or the SOUTH Direction), else
           they are on the horizontal plane (either the WEST or the EAST Direction).
         */
        int distance;
        if (this.position.getX() == objectPosition.getX()) {
            distance = objectPosition.getY() - this.position.getY();

            return distance > 0 ? new SimpleEntry<>(Direction.NORTH, distance) :
                    new SimpleEntry<>(Direction.SOUTH, abs(distance));
        }
        else if (this.position.getY() == objectPosition.getY()){
            distance = objectPosition.getX() - this.position.getX();

            return distance > 0 ? new SimpleEntry<>(Direction.EAST, distance) :
                    new SimpleEntry<>(Direction.WEST, abs(distance));
        }

        return null;
    }

    /**
     * @return The robot attributes.
     */
    public String getAttributes() {
        return this.attributes.toString();
    }
}