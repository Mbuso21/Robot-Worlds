package za.co.wethinkcode.ServerClient.database.dai;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;
import za.co.wethinkcode.ServerClient.database.dao.RobotDAO;

import java.util.List;

public interface RobotDAI extends BaseQuery {

    /**
     * Gets all the robots in the database
     * @return a list of robots data objects
     */
    @Select(
            "SELECT * "
                    + "FROM robot "
    )
    List<RobotDAO> getAll();

    /**
     * Gets a robot data object from the database by name
     * @param robotName the name of the robot to fetch from the database
     * @return a robot data object
     */
    @Select(
            "SELECT * "
                    + "FROM robot "
                    + "WHERE name = ?{1} "
                    + "COLLATE NOCASE"
    )
    RobotDAO getByName(String robotName);

    /**
     * Gets all the robots with the specified world id in the database
     * @param worldId world id
     * @return a list of robot data objects
     */
    @Select(
            "SELECT * "
                    + "FROM robot "
                    + "WHERE world_id = ?{1}"
    )
    List<RobotDAO> getAllByWorldId(int worldId );

    /**
     * Deletes the robot with the specified name
     * @param robotName The name of the robot to delete
     */
    @Update(
            "DELETE " +
                    "FROM robot " +
                    "WHERE name = ?{1} " +
                    "COLLATE NOCASE "
    )
    void delete(String robotName);


    /**
     * Adds a robot data object to the database
     * @param robot
     */
    @Update(
            sql="INSERT INTO robot (name, position, world_id, direction) "
                    + "VALUES (?{1.name}, ?{1.position}, ?{1.worldId}, ?{1.direction}) "
    )
    void save(RobotDAO robot);

    /**
     * Updates the robot
     * @param robot the robot to be updated
     */
    @Update(
            sql="UPDATE robot SET "
                    + "name = ?{1.name}, world_id = ?{1.worldId}, position = ?{1.position}, direction = ?{1.direction} "
                    + "WHERE id = ?{1.id} "
    )
    void update(RobotDAO robot);
}
