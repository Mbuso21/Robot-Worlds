package za.co.wethinkcode.ServerClient.database.daoImpl;

import net.lemnik.eodsql.QueryTool;
import za.co.wethinkcode.ServerClient.database.RobotWorldsDB;
import za.co.wethinkcode.ServerClient.database.dai.ObstacleDAI;
import za.co.wethinkcode.ServerClient.database.dao.ObstacleDAO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * ObstacleDaoImpl class
 */
public class ObstacleDaoImpl {
    private static final ObstacleDAI obstacleQUERY =
            QueryTool.getQuery(RobotWorldsDB.getConnection(), ObstacleDAI.class);

    /**
     * Get obstacle by id
     * @param obstacleId Obstacle id
     * @return Obstacle
     */
    public ObstacleDAO findById(int obstacleId) {
        return Optional.of(obstacleQUERY.getById(obstacleId)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Get obstacle by position
     * @param obstaclePosition Obstacle position
     * @return Obstacle
     */
    public ObstacleDAO findByPosition(String obstaclePosition) {
        return Optional.of(obstacleQUERY.getByPosition(obstaclePosition)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Get all obstacles
     * @return List of Obstacles
     */
    public List<ObstacleDAO> findAll() {
        return Optional.of(
                obstacleQUERY.getAll()).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Save obstacle
     * @param obstacle Obstacle object to save
     */
    public void add(ObstacleDAO obstacle) {
        if (exists(obstacle)) {
            throw new IllegalArgumentException("Robot with POSITION: '" + obstacle.getPosition() + "' already exists.");
        }

        try {
            findById(obstacle.getId());
            throw new IllegalArgumentException("Obstacle with id: '" + obstacle.getId() + "' already exists.");
        } catch (NullPointerException | NoSuchElementException e) {
            obstacleQUERY.save(obstacle);
        }
    }

    /**
     * Delete obstacle by id
     * @param id Obstacle object id
     */
    public void deleteById(int id) {
        if(exists(findById(id))) {
            obstacleQUERY.delete(id);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Check if the obstacle exists
     * @param obstacle Obstacle to check
     * @return True if robot exists, else false.
     */
    public boolean exists(ObstacleDAO obstacle) {
        try {
            return findAll().stream()
                    .map(ObstacleDAO::getPosition)
                    .anyMatch(position -> position.equalsIgnoreCase(obstacle.getPosition()));
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Delete obstacles with the specified world id
     * @param id world id
     */
    public void deleteAllByWorldId(int id) {
        deleteAllByWorldId(id);
    }

    /**
     * Get all obstacles by world Id
     * @param worldId Specific world Id
     * @return List of Obstacles
     */
    public List<ObstacleDAO> findAllByWorldId(int worldId) {
        return obstacleQUERY.getAllByWorldId(worldId);
    }
}
