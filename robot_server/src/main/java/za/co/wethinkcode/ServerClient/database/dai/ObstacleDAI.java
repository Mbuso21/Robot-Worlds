package za.co.wethinkcode.ServerClient.database.dai;

import java.util.List;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;
import za.co.wethinkcode.ServerClient.database.dao.ObstacleDAO;

public interface ObstacleDAI extends BaseQuery {

    /**
     * Gets all the obstacles in the database
     * @return a list of obstacle data objects
     */
    @Select(
            "SELECT * "
                    + "FROM obstacle "
    )
    List<ObstacleDAO> getAll();

    /**
     * Gets a obstacle data object from the database by id
     * @param obstacleId the id of the obstacle to fetch from the database
     * @return a obstacle data object
     */
    @Select(
            "SELECT * "
                    + "FROM obstacle "
                    + "WHERE id = ?{1}"
    )
    ObstacleDAO getById( int obstacleId );

    /**
     * Gets a obstacle data object from the database by its position
     * @param obstaclePosition the position of the obstacle to fetch from the database
     * @return a obstacle data object
     */
    @Select(
            "SELECT * "
                    + "FROM obstacle "
                    + "WHERE position = ?{1}"
    )
    ObstacleDAO getByPosition( String obstaclePosition );

    /**
     * Gets all the obstacles with the specified world id in the database
     * @param worldId world id
     * @return a list of obstacle data objects
     */
    @Select(
            "SELECT * "
                    + "FROM obstacle "
                    + "WHERE world_id = ?{1}"
    )
    List<ObstacleDAO> getAllByWorldId(int worldId );

    /**
     * Adds a obstacle data object to the database
     * @param obstacle the obstacle object to be saved
     */
    @Update(
            sql="INSERT INTO obstacle (size, position, world_id) "
                    + "VALUES (?{1.size}, ?{1.position}, ?{1.worldId}) "
    )
    void save( ObstacleDAO obstacle );

    /**
     * Updates the obstacle
     * @param obstacle the obstacle to be updated
     */
    @Update(
            sql="UPDATE obstacle SET "
                    + "size = ?{1.size}, position = ?{1.position} "
                    + "WHERE id = ?{1.id} "
    )
    void update( ObstacleDAO obstacle );

    /**
     * Deletes the obstacle with the specified id
     * @param obstacleId the id of the obstacle to be deleted
     */
    @Update(
            "DELETE " +
                    "FROM obstacle " +
                    "WHERE id = ?{1} "
    )
    void delete( int obstacleId );

    /**
     * Deletes the obstacles with the specified world id
     * @param worldId the id of the world
     */
    @Update(
            "DELETE " +
                    "FROM obstacle " +
                    "WHERE world_id = ?{1} "
    )
    void deleteAllByWorldId( int worldId );
}

