package za.co.wethinkcode.ServerClient.database.dai;

import java.util.List;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;
import za.co.wethinkcode.ServerClient.database.dao.MineDAO;

public interface MineDAI extends BaseQuery {

    /**
     * Gets all the mines in the database
     * @return a list of mine data objects
     */
    @Select(
            "SELECT * "
                    + "FROM mine "
    )
    List<MineDAO> getAll();

    /**
     * Gets a mine data object from the database by id
     * @param mineId the id of the mine to fetch from the database
     * @return a mine data object
     */
    @Select(
            "SELECT * "
                    + "FROM mine "
                    + "WHERE id = ?{1} "
    )
    MineDAO getById( int mineId );

    /**
     * Gets a mine data object from the database by its position
     * @param minePosition the position of the mine to fetch from the database
     * @return a mine data object
     */
    @Select(
            "SELECT * "
                    + "FROM mine "
                    + "WHERE position = ?{1}"
    )
    MineDAO getByPosition(String minePosition );

    /**
     * Gets all the mines with the specified world id in the database
     * @param worldId world id
     * @return a list of mine data objects
     */
    @Select(
            "SELECT * "
                    + "FROM mine "
                    + "WHERE world_id = ?{1}"
    )
    List<MineDAO> getAllByWorldId(int worldId );

    /**
     * Adds a mine data object to the database
     * @param mine the mine object to be saved
     */
    @Update(
            sql="INSERT INTO mine (size, name, position) "
                    + "VALUES (?{1.size}, ?{1.name}, ?{1.position}) "
    )
    void save( MineDAO mine );

    /**
     * Updates the mine
     * @param mine the mine to be updated
     */
    @Update(
            sql="UPDATE mine SET "
                    + "name = ?{1.name}, size = ?{1.size}, position = ?{1.position} "
                    + "WHERE id = ?{1.id} "
    )
    void update( MineDAO mine );

    /**
     * Deletes the mine with the specified id
     * @param mineId the id of the mine to be deleted
     */
    @Update(
            "DELETE " +
                    "FROM mine " +
                    "WHERE id = ?{1} "
    )
    void delete( int mineId );
}

