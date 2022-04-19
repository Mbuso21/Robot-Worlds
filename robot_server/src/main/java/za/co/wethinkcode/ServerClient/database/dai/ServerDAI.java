package za.co.wethinkcode.ServerClient.database.dai;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;
import za.co.wethinkcode.ServerClient.database.dao.ServerDAO;

import java.util.List;

public interface ServerDAI extends BaseQuery {
    /**
     * Gets all the servers in the database
     * @return a list of server data objects
     */
    @Select(
            "SELECT * "
                    + "FROM server "
    )
    List<ServerDAO> getAllServers();

    /**
     * Gets a server data object from the database by id
     * @param serverId the id of the server to fetch from the database
     * @return a server data object
     */
    @Select(
            "SELECT * "
                    + "FROM server "
                    + "WHERE id = ?{1} "
    )
    ServerDAO getById(int serverId);

    /**
     * Gets a server data object from the database by name
     * @param serverName the name of the server to fetch from the database
     * @return a server data object
     */
    @Select(
            "SELECT * "
                    + "FROM server "
                    + "WHERE name = ?{1} "
    )
    ServerDAO getByName(String serverName);

    /**
     * Gets a server data object from the database by ip address
     * @param serverIp the ip address of the server to fetch from the database
     * @return a server data object
     */
    @Select(
            "SELECT * "
                    + "FROM server "
                    + "WHERE ip = ?{1} "
    )
    ServerDAO getByIp(String serverIp);


    /**
     * Updates the world
     * @param server the world to be updated
     */
    @Update(
            sql="UPDATE server SET "
                    + "port = ?{1.port}, ip = ?{1.ip}, name = ?{1.name} "
                    + "WHERE id = ?{1.id} "
    )
    void update(ServerDAO server);

    /**
     * Deletes the server with the specified id
     * @param serverId The id of the robot to delete
     */
    @Update(
            "DELETE " +
                    "FROM server " +
                    "WHERE id = ?{1} "
    )
    void delete(int serverId);

    /**
     * Adds a server data object to the database
     * @param server
     */
    @Update(
            sql="INSERT INTO server (ip, name, port) "
                    + "VALUES (?{1.ip}, ?{1.name}, ?{1.port}) "
    )
    void save(ServerDAO server);
}
