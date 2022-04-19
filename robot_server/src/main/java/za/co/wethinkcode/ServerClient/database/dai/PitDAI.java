package za.co.wethinkcode.ServerClient.database.dai;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;
import za.co.wethinkcode.ServerClient.database.dao.PitDAO;

import java.util.List;

public interface PitDAI extends BaseQuery {

    /**
     * Gets all the pits in the database
     * @return a list of pit data objects
     */
    @Select(
            "SELECT * "
                    + "FROM pit "
    )
    List<PitDAO> getAll();

    /**
     * Gets a pit data object from the database by id
     * @param pitId the id of the pit to fetch from the database
     * @return a pit data object
     */
    @Select(
            "SELECT * "
                    + "FROM pit "
                    + "WHERE id = ?{1}"
    )
    PitDAO getById(int pitId );

    /**
     * Gets a pit data object from the database by its position
     * @param pitPosition the position of the pit to fetch from the database
     * @return a pit data object
     */
    @Select(
            "SELECT * "
                    + "FROM pit "
                    + "WHERE position = ?{1}"
    )
    PitDAO getByPosition(String pitPosition );

    /**
     * Gets all the pits with the specified world id in the database
     * @param worldId world id
     * @return a list of pit data objects
     */
    @Select(
            "SELECT * "
                    + "FROM pit "
                    + "WHERE world_id = ?{id}"
    )
    List<PitDAO> getAllByWorldId(int worldId );

    /**
     * Adds a pit data object to the database
     * @param pit the pit object to be saved
     */
    @Update(
            sql="INSERT INTO pit (size, name, position) "
                    + "VALUES (?{1.size}, ?{1.name}, ?{1.position}) "
    )
    void save( PitDAO pit );

    /**
     * Updates the pit
     * @param pit the pit to be updated
     */
    @Update(
            sql="UPDATE pit SET "
                    + "name = ?{1.name}, size = ?{1.size}, position = ?{1.position} "
                    + "WHERE id = ?{1.id} "
    )
    void update( PitDAO pit );

    /**
     * Deletes the pit with the specified id
     * @param pitId the id of the pit to be deleted
     */
    @Update(
            "DELETE " +
                    "FROM pit " +
                    "WHERE id = ?{1} "
    )
    void delete( int pitId );
}

