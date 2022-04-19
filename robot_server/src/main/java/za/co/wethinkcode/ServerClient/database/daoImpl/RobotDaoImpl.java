package za.co.wethinkcode.ServerClient.database.daoImpl;

import net.lemnik.eodsql.QueryTool;
import za.co.wethinkcode.ServerClient.database.RobotWorldsDB;
import za.co.wethinkcode.ServerClient.database.dai.RobotDAI;
import za.co.wethinkcode.ServerClient.database.dao.RobotDAO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class RobotDaoImpl {
    public static final RobotDAI robotQUERY = QueryTool.getQuery(RobotWorldsDB.getConnection(), RobotDAI.class);

    /**
     * Get robot by name
     * @param name Robot name
     * @return Robot
     */
    public RobotDAO findByName(String name) {
        return Optional.of(robotQUERY.getByName(name)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Get all robots
     * @return List of Robots
     */
    public List<RobotDAO> findAll() {
        return Optional.of(robotQUERY.getAll()).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Save robot
     * @param robot Robot object to save
     * @return Saved robot object
     */
    public RobotDAO add(RobotDAO robot) {
        if (exists(robot)) {
            throw new IllegalArgumentException("Robot with POSITION: '" + robot.getPosition() + "' already exists.");
        }

        try {
            findByName(robot.getName());
        } catch (NullPointerException | NoSuchElementException e) {
            robotQUERY.save(robot);
            return findByName(robot.getName());
        }

        throw new IllegalArgumentException("Robot with name: '" + robot.getName() + "' already exists.");
    }

    /**
     * Check if the robot exists
     * @param robot Robot to check
     * @return True if robot exists, else false.
     */
    public boolean exists(RobotDAO robot) {
        try {
            return findAll().stream()
                    .map(RobotDAO::getPosition)
                    .anyMatch(position -> position.equalsIgnoreCase(robot.getPosition()));
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Delete robot by name
     * @param name Robot object name
     */
    public void deleteByName(String name) {
        try {
            findByName(name);
            robotQUERY.delete(name);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Get all robots by world id
     * @param worldId Specific world id
     * @return List of Robots
     */
    public List<RobotDAO> findAllByWorldId(int worldId) {
        return robotQUERY.getAllByWorldId(worldId);
    }
}
