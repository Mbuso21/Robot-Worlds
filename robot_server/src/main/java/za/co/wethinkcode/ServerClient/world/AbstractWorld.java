package za.co.wethinkcode.ServerClient.world;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.server.MultiServers;

import java.io.*;
import java.util.*;

/**
 * AbstractWorld class that implements the IWorld interface
 * @implements IWorld
 */
public abstract class AbstractWorld implements IWorld {
    private final HashMap<String, String> clientNameList = new HashMap<>();
    private int VISIBILITY;
    private int RELOAD;
    private int REPAIR;
    private int MINE;
    private int TOP_LEFT_X;
    private int TOP_LEFT_Y;
    private int BOTTOM_RIGHT_X;
    private int BOTTOM_RIGHT_Y;

    /**
     * Updates the direction of the robot in the world.
     * @param turnRight the robot must turn right if true, else turn left if false
     * @param robot the robot that must get its direction updated
     */
    public void updateDirection(boolean turnRight, Robot robot) {
        if (turnRight) {
            switch (robot.getCurrentDirection()) {
                case NORTH : robot.setCurrentDirection(WorldObject.Direction.EAST);
                case EAST : robot.setCurrentDirection(WorldObject.Direction.SOUTH);
                case SOUTH : robot.setCurrentDirection(WorldObject.Direction.WEST);
                case WEST : robot.setCurrentDirection(WorldObject.Direction.NORTH);
            }
        } else {
            turnLeft(robot);
        }
    }

    /**
     * Turns the robot towards the left direction
     * @param robot the robot that must make the left turn
     */
    public void turnLeft(Robot robot) {
        switch (robot.getCurrentDirection()) {
            case NORTH : robot.setCurrentDirection(WorldObject.Direction.WEST);
            case WEST : robot.setCurrentDirection(WorldObject.Direction.SOUTH);
            case SOUTH : robot.setCurrentDirection(WorldObject.Direction.EAST);
            case EAST : robot.setCurrentDirection(WorldObject.Direction.NORTH);
        }
    }

    /**
     * Adds a new robot connection to the world.
     * @param clientMachine the machine the client is using to access the world
     * @param robot the robot to be connected to the world
     */
    public abstract void addNewConnection(String clientMachine, Robot robot);

    /**
     * @return A hashmap of connected players, where the key is the string
     * and value is the robot connected.
     */
    public abstract HashMap<String, Robot> getPlayers();

    /**
     * Creates and returns a JSONObject containing the robot's data.
     * @param robot the robot object with data
     * @return JSONObject containing the robot's data
     */
    public abstract JSONObject getData(Robot robot);

    /**
     * Updates the robot's position in the world.
     * @param nrSteps the number of steps the robot has to take
     * @param robot the robot target that has to have its position updated
     * @return a string message containing the result of this operation
     */
    public abstract String updatePosition(int nrSteps, Robot robot);

    /**
     * Gets the shot range a robot has.
     * @param maximumShots the max shots the robot has in number of steps
     * @return shot range in number of steps
     */
    public int shotRange(int maximumShots) {
        switch (maximumShots) {
            case 5 : return 1;
            case 4 : return 2;
            case 3 : return 3;
            case 2 : return 4;
            case 1 : return 5;
            default : return 0;
        }
    }

    /**
     * @return The {@link AbstractWorld#TOP_LEFT_Y} value in the world grid.
     */
    public int getTopLeftY() {
        return this.TOP_LEFT_Y;
    }

    /**
     * @return The {@link AbstractWorld#BOTTOM_RIGHT_Y} value in the world grid.
     */
    public int getBottomRightY() {
        return this.BOTTOM_RIGHT_Y;
    }

    /**
     * @return The {@link AbstractWorld#BOTTOM_RIGHT_X}  value in the world grid.
     */
    public int getBottomRightX() {
        return this.BOTTOM_RIGHT_X;
    }

    /**
     * @return The {@link AbstractWorld#TOP_LEFT_X} value in the world grid.
     */
    public int getTopLeftX() {
        return this.TOP_LEFT_X;
    }

    /**
     * Creates a configuration file with the worlds attributes.
     */
    public static void createConfig() {
        try {
            String filePath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
            File configFile = new File(filePath).getAbsoluteFile();
            Properties properties = new Properties();

            properties.setProperty("visibility", Integer.toString(MultiServers.getVisibility()));
            properties.setProperty("reload", "3");
            properties.setProperty("repair", "3");
            properties.setProperty("mine", "2");
            properties.setProperty("topLeftX", Integer.toString(-MultiServers.getSize()));
            properties.setProperty("topLeftY", Integer.toString(MultiServers.getSize()));
            properties.setProperty("bottomRightX", Integer.toString(MultiServers.getSize()));
            properties.setProperty("bottomRightY", Integer.toString(-MultiServers.getSize()));

            FileWriter writer = new FileWriter(configFile);
            properties.store(writer, "");
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads the configuration file, assigns the positional world attributes ({@link AbstractWorld#TOP_LEFT_Y},
     * {@link AbstractWorld#BOTTOM_RIGHT_Y}, {@link AbstractWorld#BOTTOM_RIGHT_X} and {@link AbstractWorld#TOP_LEFT_X}).
     * @return Non-positional world attributes ({@link AbstractWorld#VISIBILITY}, {@link AbstractWorld#RELOAD},
     * {@link AbstractWorld#REPAIR} and {@link AbstractWorld#MINE}).
     */
    public HashMap<String, Integer> loadConfig() {
        createConfig();

        try {
            String filePath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
            File configFile = new File(filePath).getAbsoluteFile();
            FileReader reader = new FileReader(configFile);
            Properties properties = new Properties();

            properties.load(reader);

            this.VISIBILITY = Integer.parseInt(properties.getProperty("visibility"));
            this.RELOAD = Integer.parseInt(properties.getProperty("reload"));
            this.REPAIR = Integer.parseInt(properties.getProperty("repair"));
            this.MINE = Integer.parseInt(properties.getProperty("mine"));
            this.TOP_LEFT_X = Integer.parseInt(properties.getProperty("topLeftX"));
            this.TOP_LEFT_Y = Integer.parseInt(properties.getProperty("topLeftY"));
            this.BOTTOM_RIGHT_X = Integer.parseInt(properties.getProperty("bottomRightX"));
            this.BOTTOM_RIGHT_Y = Integer.parseInt(properties.getProperty("bottomRightY"));

            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        HashMap<String, Integer> worldAttributes = new HashMap<>();

        worldAttributes.put("mine", this.MINE);
        worldAttributes.put("reload", this.RELOAD);
        worldAttributes.put("repair", this.REPAIR);
        worldAttributes.put("visibility", this.VISIBILITY);
        worldAttributes.put("topLeftX", this.TOP_LEFT_X);
        worldAttributes.put("topLeftY", this.TOP_LEFT_Y);
        worldAttributes.put("bottomLeftX", this.BOTTOM_RIGHT_X);
        worldAttributes.put("bottomLeftY", this.BOTTOM_RIGHT_Y);

        return worldAttributes;
    }

    /**
     * Sets a mine object in the world.
     * @param robot the robot that is setting the mine in the world
     */
    public abstract void setMine(Robot robot);

    /**
     * Gets a hashmap of world objects, where the key is the string
     * and value is the objects in the world.
     * @return a hashmap containing world objects
     */
    public abstract HashMap<String, WorldObject> getWorldObjects();

    /**
     * Adds a client to the world clientNameList
     * @param clientMachine machine the client is using to connect to the world
     * @param robotName the name of the robot connected to the world
     */
    protected void addClient(String clientMachine, String robotName) {
        this.clientNameList.put(clientMachine, robotName);
    }

    /**
     * @return An int array containing world attributes.
     */
    public abstract HashMap<String, Integer> getWorldAttributes();

    /**
     * Generate obstacles to populate the world.
     */
    protected abstract void generateObstacles();

    /**
     * Generate pits to populate the world.
     */
    protected abstract void generatePits();

    /**
     * Checks if the robot path is blocked and returns a message.
     * @param robot the robot to check
     * @return a description whether the robot position is blocked or not
     */
    public abstract String isPathBlocked(Robot robot);

    /**
     * Checks if the is enough space in the world to launch another object and
     * returns a boolean value. Returns true, if there is enough space else returns false.
     * @return boolean value true or false whether there is enough space in the world
     */
    public abstract boolean enoughSpaceInWorld();

    /**
     * Removes all connected players from the world.
     */
    public abstract void clearPlayerConnections();

    /**
     * Checks if the robot is launched and returns a boolean value.
     * @param robotName which is the string
     * @return true if the robot is launched else, false
     */
    public abstract boolean checksIfRobotIsLaunched(String robotName);

    /**
     * Converts the position from type String to type ArrayList<Integer>
     * @param robot robot that has the position to be converted
     * @return ArrayList<Integer> containing x and y coordinates
     */
    public abstract ArrayList<Integer> convertPosToInt(Robot robot);

    /**
     * @return The int size of the world.
     */
    public abstract int getSize();

    /**
     * @return The name of the world.
     */
    public abstract String getName();

    /**
     * Sets the name of the world.
     * @param name the string name of the world to set
     */
    public abstract void setName(String name);

    /**
     * Sets the size of the world.
     * @param size the int size of the world to set
     */
    public abstract void setSize(int size);

    /**
     * @return A list of obstacle objects.
     */
    public abstract List<ObstacleObject> getObstacles();

    /**
     * @return A list of mine objects.
     */
    public abstract List<MineObject> getMines();

    /**
     * @return A list of pit objects.
     */
    public abstract List<PitObject> getPits();

    /**
     * Generates the world objects - mines, obstacles and pits.
     */
    protected abstract void generateWorldObjects();
}