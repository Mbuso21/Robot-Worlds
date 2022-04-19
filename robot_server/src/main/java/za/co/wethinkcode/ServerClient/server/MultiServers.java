package za.co.wethinkcode.ServerClient.server;

import org.json.JSONObject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import za.co.wethinkcode.ServerClient.database.dao.ObstacleDAO;
import za.co.wethinkcode.ServerClient.database.dao.WorldDAO;
import za.co.wethinkcode.ServerClient.database.daoImpl.WorldDaoImpl;
import za.co.wethinkcode.ServerClient.database.daoImpl.ObstacleDaoImpl;
import za.co.wethinkcode.ServerClient.world.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

/**
 * Server class the client must connect to which implements the Callable interface similar to Runnable.
 * @see Callable
 */
@Command(
        name = "robots",
        mixinStandardHelpOptions = true,
        version = {"robots 1.0"},
        description = {"Uss-netwon MultiServer"}
)
public class MultiServers implements Callable<Integer> {
    private static AbstractWorld world;

    /**
     * MultiServers class constructor
     */
    public MultiServers() {
        System.out.println("RUNNING MULTI...");
    }

    @Option(
            names = {"-s", "--size"},
            description = {"Size of the world as one side of a square grid. The default size is one."}
    )
    private static int size = 1;

    @Option(
            names = {"-p", "--port"},
            description = {"Port to listen for client connections. The default port number is 5000."}
    )
    private int PORT = 5000;

    @Option(
            names = {"-o", "--obstacle"},
            description = {"Position of fixed obstacle as [x,y] coordinate in form 'x,y', or 'none' or 'random'."
                    + "The default value is 'none'"}
    )
    private static String obstacle = "none";

    @Option(
            names = {"-pt", "--pit"},
            description = {"Position of fixed obstacle as [x,y] coordinate in form 'x,y', or 'none' or 'random'."
                    + "The default value is 'none'"}
    )
    private static String pits = "none"; // Defaults to none

    @Option(
            names = {"-m", "--mine"},
            description = {"Position of fixed obstacle as [x,y] coordinate in form 'x,y', or 'none' or 'random'."
                    + "The default value is 'none'"}
    )
    private static String mines = "none"; // Defaults to none

    @Option(
            names = {"-v", "--visibility"},
            description = {"Visibility for robot in number of steps. The default value is 10."}
    )
    private static int visibility = 10;

    /**
     * Gets the size of the world.
     * @return the int value representing the size of the world
     */
    public static int getSize() {
        return size;
    }

    /**
     * Gets the obstacle.
     * @return a string containing the position of the obstacle
     */
    public static String getObstacle() {
        return obstacle;
    }

    /**
     * Gets the pit.
     * @return a string containing the position of the pit
     */
    public static String getPits() {
        return pits;
    }

    /**
     * Gets the mine.
     * @return a string containing the position of the fixed obstacle
     */
    public static String getMines() {
        return mines;
    }

    /**
     * Returns an int object that controls the range of visibility of all robots in the world in each direction
     * (i.e. front, back or either sides).
     * The visibility is configured in the server’s configuration file as a value in number of steps,
     * which the robot cannot see further than - ahead of it (i.e. in the direction it is facing).
     * If an object is larger than the visible range, then the robot cannot see the extent or ends of the obstacle.
     * @return the int value of the configured number of steps
     */
    public static int getVisibility() {
        return visibility;
    }

    @Override
    public Integer call() {
        world = new RealWorld();
        printWorld();

        System.out.println("Robot World server running and waiting for client connections on port " + PORT);
        try {
            Socket socket = new Socket();
            // Opens socket for the PORT
            ServerSocket serverSocket = new ServerSocket(PORT);

            do {
                // Program will wait until there is a connection from a client
                socket = serverSocket.accept();
                System.out.println(" > Client connection: " + socket);
                // invokes the Server Class
                Runnable r = new Server(socket, world);
                Thread task = new Thread(r);
                task.start();

            } while (true);
        } catch (IOException e) {
            System.err.println("There was a problem starting the server: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Prints out the world size, visibility and obstacles in the world.
     */
    public void printWorld() {
        System.out.println("* Creating World.. [" +
                "size: " + getSize() + " x " + getSize() +
                ", obstacles: " + getObstacle() +
                ", pits: " + getPits() +
                ", mines: " + getMines() +
                ", visibility: " + getVisibility() +
                "]");
    }

    public static void main(String[] args) {
        (new CommandLine(new MultiServers())).execute(args);
    }

    /**
     * Saves the constructed RobotWorld to a database and returns a message
     * on the result of the save attempt, whether it is a success or failure.
     * @param worldName The name of the world being saved in the database.
     * @return a string message of the save result
     */
    public static String saveWorld(String worldName) {
        System.out.println("Saving world...");
        if (worldName.isEmpty()) {
            return "Name of world cannot be empty or null";
        }
        else {
            ObstacleDaoImpl obstacleDaoImpl = new ObstacleDaoImpl();
            ObstacleDAO obstacleDao = new ObstacleDAO();

            WorldDaoImpl worldDaoImpl = new WorldDaoImpl();
            WorldDAO worldDAO = new WorldDAO();

            worldDAO.setName(worldName);
            worldDAO.setSize(world.getSize());
            worldDAO.setAttributes(new JSONObject(world.getWorldAttributes()));
            int worldId = worldDaoImpl.add(worldDAO).getId();

            world.getObstacles().forEach(obstacle ->
                            obstacleDaoImpl.add(ObstacleDAO.create(obstacle.getSize(), worldId,
                                    obstacle.objectPosition().toString())));
            return "SUCCESS";
        }
    }

    /**
     * Restores the specified RobotWorld (by name) from the database and returns a
     * message on the result of the restore attempt, whether it is a success or failure.
     * @param worldName The name of the world being restored from the database.
     * @return a string message of the save result
     */
    public static String restoreWorld(String worldName) {
        WorldDaoImpl worldDaoImpl = new WorldDaoImpl();
        ObstacleDaoImpl obstacleDaoImpl = new ObstacleDaoImpl();
        System.out.println("Restoring world...");
        AbstractWorld worldToRestore;
        if (worldName.isEmpty()) {
            return "Name of world cannot be empty or null";
        }
        else {
            try {
                WorldDAO _world = worldDaoImpl.findByName(worldName);
                ObstacleObject[] obstacles = obstacleDaoImpl.findAllByWorldId(_world.getId())
                        .stream()
                        .map(obstacle -> {
                            Position position = Position.createFromString(obstacle.getPosition());
                            return new ObstacleObject(position.getX(), position.getY());
                        })
                        .toArray(ObstacleObject[]::new);

                world = new RealWorld(_world.getSize(), _world.getName(), obstacles, new PitObject[]{}, new MineObject[]{},
                        _world.getAttributes());
            } catch (NoSuchElementException ex) {
                return "World with name '" + worldName + "' does not exists!";
            }
        }

        System.out.println("Restored world '" + worldName + "' successfully.");
        System.out.println("* Restored World.. [" +
                "size: " + world.getSize() + " x " + world.getSize() +
                ", obstacles: " + world.getObstacles().size() +
                ", pits: " + world.getPits().size() +
                ", mines: " + world.getMines().size() +
                ", visibility: " + getVisibility() +
                "]");

        return "SUCCESS";
    }
}
