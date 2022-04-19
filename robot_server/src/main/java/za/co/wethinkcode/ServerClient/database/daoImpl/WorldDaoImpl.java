package za.co.wethinkcode.ServerClient.database.daoImpl;

import net.lemnik.eodsql.QueryTool;
import za.co.wethinkcode.ServerClient.database.RobotWorldsDB;
import za.co.wethinkcode.ServerClient.database.dai.WorldDAI;
import za.co.wethinkcode.ServerClient.database.dao.WorldDAO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class WorldDaoImpl {
    private static final WorldDAI worldQUERY = QueryTool.getQuery(RobotWorldsDB.getConnection(), WorldDAI.class);

    /**
     * Get world by name
     * @param name World name
     * @return World
     */
    public WorldDAO findByName(String name) {
        return Optional.of(worldQUERY.getByName(name)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Get all worlds
     * @return List of Worlds
     */
    public List<WorldDAO> findAll() {
        return worldQUERY.getAll();
    }

    /**
     * Save world
     * @param world World object to save
     * @return Saved world object
     */
    public WorldDAO add(WorldDAO world) {
        try {
            findByName(world.getName());
        } catch (NoSuchElementException e) {
            worldQUERY.save(world);
            return findByName(world.getName());
        }
        return update(world);
    }

    /**
     * Update world
     * @param world World object to update
     * @return Update world object
     */
    public WorldDAO update(WorldDAO world) {
        try {
            worldQUERY.save(world);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return findByName(world.getName());
    }
}
