package za.co.wethinkcode.ServerClient.world;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.server.MultiServers;

import java.util.*;

/**
 * RealWorld class that extends the AbstractWorld class
 * @see AbstractWorld
 */
public class RealWorld extends AbstractWorld {
    private JSONObject data;
    private final Random random = new Random();
    private HashMap<String, Integer> worldAttributes;
    private HashMap<String, Robot> playerConnections = new HashMap<>();
    private HashMap<String, WorldObject> worldObjects = new HashMap<>();
    private String name;
    private int size;

    /**
     * RealWorld constructor
     */
    public RealWorld() {
        this.size = MultiServers.getSize();
        this.worldAttributes = loadConfig();
        loadEdges();
        generateWorldObjects();
    }

    /**
     * RealWorld constructor that takes 5 parameters.
     * @param size the size of the world
     * @param name the name of the world
     * @param obstacles the world obstacles
     * @param pits the world pits
     * @param mines the world mines
     */
    public RealWorld(int size, String name, ObstacleObject[] obstacles, PitObject[] pits, MineObject[] mines,
                     HashMap<String, Integer> worldAttributes) {
        this.size = size;
        this.name = name;
        setObstacles(obstacles);
        setPits(pits);
        setMines(mines);
        this.worldAttributes = worldAttributes;
    }

    /**
     * Loads Edges of the World as part of the world objects
     */
    private void loadEdges() {
        worldObjects.put("TOP",    new EdgeObject(0, getTopLeftY()));
        worldObjects.put("LEFT",   new EdgeObject(getTopLeftX(), 0));
        worldObjects.put("RIGHT",  new EdgeObject(getBottomRightX(), 0));
        worldObjects.put("BOTTOM", new EdgeObject(0, getBottomRightY()));
    }

    @Override
    public void addNewConnection(String clientMachine, Robot robot) {
        setRobotPosition(robot);
        worldObjects.put(robot.toString(), robot);
        robot.setStatus(Status.TODO);
        playerConnections.put(robot.getName(), robot);
        addClient(clientMachine, robot.getName());
    }

    @Override
    public HashMap<String, Robot> getPlayers() {
        return this.playerConnections;
    }

    @Override
    public void clearPlayerConnections() {
        playerConnections.values()
                .forEach(robot -> worldObjects.remove(robot.toString()));

        this.playerConnections.clear();
    }

    /**
     * Sets the robot's position (x,y) in the world. If there are no robots in there the position will be
     * (0,0), else the robot will be assigned a random position in the world.
     * @param robot the robot to be assigned a position in the world
     */
    public void setRobotPosition(Robot robot) {
        int positionX = 0;
        int positionY = 0;

        if (playerConnections.size() >= 1) {
            boolean positionIsBlocked;
            int sizeFactor = MultiServers.getSize() % 2 == 0 ? MultiServers.getSize() : MultiServers.getSize() - 1;
            do {
                positionX = random.nextInt(sizeFactor + 1) + 1 - sizeFactor;
                positionY = random.nextInt(sizeFactor + 1) + 1 - sizeFactor;
                positionIsBlocked = false;

                for (WorldObject object : worldObjects.values()) {
                    if (!(object instanceof EdgeObject) &&
                        !object.blocksPosition(new Position(positionX, positionY)).equalsIgnoreCase("OK")) {
                        positionIsBlocked = true;
                        break;
                    }
                }
            } while (positionIsBlocked);
        }

        robot.setPosition(new Position(positionX, positionY));
    }

    @Override
    public ArrayList<Integer> convertPosToInt(Robot robot) {
        ArrayList<Integer> pos = new ArrayList<>();
        pos.add(robot.objectPosition().getX());
        pos.add(robot.objectPosition().getY());
        return pos;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject getData(Robot robot) {
        data = new JSONObject();

        ArrayList<Integer> pos = convertPosToInt(robot);

        data.put("position", pos);
        data.put("shields", robot.getShields());

        data.putAll(worldAttributes);

        return data;
    }

    @Override
    public String updatePosition(int nrSteps, Robot robot) {
        int newX = robot.objectPosition().getX();
        int newY = robot.objectPosition().getY();

        switch (robot.getCurrentDirection()) {
            case NORTH : newY = newY + nrSteps;
            case EAST : newX = newX + nrSteps;
            case SOUTH : newY = newY - nrSteps;
            case WEST : newX = newX - nrSteps;
        }
        Position newPosition = new Position(newX, newY);
        String message;
        for ( WorldObject object : worldObjects.values() ) {
            if ( object == robot ) {
                continue;
            }

            message = object.blocksPath(robot.objectPosition(), newPosition);
            if ( !(message.equals("Done")) ) {
                System.out.println(message);
                return message;
            }
        }
        robot.setPosition(newPosition);
        return translateMessage(Message.DONE);
    }

    @Override
    public HashMap<String, Integer> getWorldAttributes() {
        return this.worldAttributes;
    }

    @Override
    protected void generateObstacles() {
        Integer[] position = Arrays.stream(MultiServers.getObstacle().split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
        WorldObject obstacle = new ObstacleObject(position[0], position[1]);

        worldObjects.put(obstacle.toString(), obstacle);
        
    }

    /**
     * Adds the obstacles to this world objects list.
     * @param obstacles obstacle objects to be added to the world objects
     */
    private void setObstacles(ObstacleObject[] obstacles) {
        Arrays.stream(obstacles)
                .forEach( obstacle -> worldObjects.put(obstacle.toString(), obstacle));
    }

    @Override
    protected void generatePits() {
        int x;
        int y;
        int numObs = random.nextInt(21);
        for (int i = 0; i <= numObs; i++) {
            x = random.nextInt(181) - 90;
            y = random.nextInt(381) - 190;
            while (blockedPosition(new Position(x, y))) {
                x = random.nextInt(181) - 90;
                y = random.nextInt(381) - 190;
            }
            WorldObject pit = new PitObject(x, y);
            worldObjects.put(pit.objectPosition().toString(), pit);
        }
    }

    /**
     * Adds the pits to this world objects list.
     * @param pits pit objects to be added to the world objects
     */
    private void setPits(PitObject[] pits) {
        Arrays.stream(pits)
                .forEach( pit -> worldObjects.put(pit.toString(), pit));
    }

    /**
     * Checks if a robot is on the edge of the world and returns a boolean value.
     * @param position the position of the robot
     * @return true if the world is on the edge, else false if not
     */
    private boolean onEdge(Position position) {
        boolean onLeftEdge   = position.getX() == getTopLeftX();
        boolean onRightEdge  = position.getX() == getBottomRightX();
        boolean onTopEdge    = position.getY() == getTopLeftY();
        boolean onBottomEdge = position.getY() == getBottomRightY();
        return onLeftEdge || onRightEdge || onTopEdge || onBottomEdge;
    }

    public String isPathBlocked(Robot robot) {
        String message;
        Position robotPosition = robot.objectPosition();
        Position rangePosition = findTargetPosition(robot);
        for (WorldObject object: worldObjects.values()) {
            message = object.blocksPath(robotPosition, rangePosition);
            if ( message.equals("OK") ) {
                continue;
            }
            return message;
        }
        return "Done";
    }

    @Override
    protected void generateWorldObjects() {
        if (MultiServers.getSize() > 1) {
            if (!MultiServers.getObstacle().equalsIgnoreCase("none")) {
                generateObstacles();
            }
            else if (!MultiServers.getPits().equalsIgnoreCase("none")) {
                generatePits();
            }
        }
    }

    public Position findTargetPosition(Robot robot) {
        int range = shotRange(robot.getMaxShots());
        Position robotCurrent = robot.objectPosition();
        switch (robot.getCurrentDirection()) {
            case NORTH : return new Position(robotCurrent.getX(), robotCurrent.getY() + range);
            case SOUTH : return new Position(robotCurrent.getX(), robotCurrent.getY() - range);
            case EAST : return new Position(robotCurrent.getX() + range, robotCurrent.getY());
            default : return new Position(robotCurrent.getX() - range, robotCurrent.getY());
        }
    }

    public boolean blockedPosition(Position position) {
        for ( WorldObject object : worldObjects.values() ) {
            if ( !(object.blocksPosition(position).equals("OK")) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setMine(Robot robot) {
        Position position = robot.objectPosition();
        WorldObject mine = new MineObject(position.getX(), position.getY());
        worldObjects.put(mine.objectPosition().toString(), mine);
        updatePosition(1, robot);
    }

    /**
     * Adds the mines to this world objects list.
     * @param mines mine objects to be added to the world object
     */
    public void setMines(MineObject[] mines) {
        Arrays.stream(mines)
                .forEach(mine -> worldObjects.put(mine.toString(), mine));
    }

    @Override
    public HashMap<String, WorldObject> getWorldObjects() {
        return worldObjects;
    }

    @Override
    public List<ObstacleObject> getObstacles() {
        return Arrays.asList(this.worldObjects.values()
                .stream()
                .filter(object -> object instanceof ObstacleObject)
                .toArray(ObstacleObject[]::new));
    }

    @Override
    public List<MineObject> getMines() {
        return Arrays.asList(this.worldObjects.values()
                            .stream()
                            .filter(object -> object instanceof MineObject)
                            .toArray(MineObject[]::new));
    }

    @Override
    public List<PitObject> getPits() {
        return Arrays.asList(this.worldObjects.values()
                        .stream()
                        .filter(object -> object instanceof PitObject)
                        .toArray(PitObject[]::new));
    }

    /**
     * Translates a type enum message to a string message
     * @param message enum of type Message
     * @return a string message
     * @see IWorld.Message
     */
    public String translateMessage(IWorld.Message message) {
        switch (message) {
            case DONE : return "DONE";
            case FELL : return "FELL";
            case MINE : return "MINE";
            default : return "OBSTRUCTED";
        }
    }

    @Override
    public boolean enoughSpaceInWorld() {
        // The number of objects in the world minus the four edges
        int numWorldObjects = getWorldObjects().size() - 4;
        // If the world size is odd, round down to the nearest int meaning if size is 3 world size == 2
        int serverSize = this.size % 2 == 0 ? this.size : this.size - 1;
        int worldGridSize = this.size == 1 ? 1 : (int)Math.pow(serverSize + 1, serverSize);

        return worldGridSize > numWorldObjects;
    }

    /**
     * Checks if the specified robot, by name, is already launched in the world.
     * @param robotName a string containing the robot name
     * @return boolean value, true if the robot is launched and false if not
     */
    public boolean checksIfRobotIsLaunched(String robotName) {
        return getPlayers().toString().contains(robotName);
    }
}
