package za.co.wethinkcode.ServerClient.database.dai;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;
import za.co.wethinkcode.ServerClient.database.dao.WorldDAO;

import java.util.List;

public interface WorldDAI extends BaseQuery {

    /**
     * Gets all the worlds in the database
     * @return a list of world data objects
     */
    @Select(
            "SELECT * "
                    + "FROM world "
    )
    List<WorldDAO> getAll();

    /**
     * Gets a world data object from the database by id
     * @param worldId the id of the world to fetch from the database
     * @return a world data object
     */
    @Select(
            "SELECT * "
                    + "FROM world "
                    + "WHERE id = ?{1} "
    )
    WorldDAO getById( int worldId );

    /**
     * Gets a world data object from the database by name
     * @param worldName the name of the world to fetch from the database
     * @return a world data object
     */
    @Select(
            "SELECT * "
                    + "FROM world "
                    + "WHERE name = ?{1} "
    )
    WorldDAO getByName( String worldName );

    /**
     * Adds a world data object to the database
     * @param world the world object to be saved
     */
    @Update(
        sql="INSERT INTO world (name, size) "
                + "VALUES (?{1.name}, ?{1.size}) "
    )
    void save( WorldDAO world );

    /**
     * Updates the world
     * @param world the world to be updated
     */
    @Update(
        sql="UPDATE world SET "
                + "name = ?{1.name}, size = ?{1.size} "
                + "WHERE id = ?{1.id} "
    )
    void update( WorldDAO world );

    /**
     * Deletes the world with the specified name
     * @param worldName the name of the world to be deleted
     */
    @Update(
            "DELETE " +
                    "FROM world " +
                    "WHERE name = ?{1} "
    )
    void delete( String worldName );
}
