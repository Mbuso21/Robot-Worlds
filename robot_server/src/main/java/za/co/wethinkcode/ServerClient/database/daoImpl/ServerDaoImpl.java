package za.co.wethinkcode.ServerClient.database.daoImpl;

import net.lemnik.eodsql.QueryTool;

import za.co.wethinkcode.ServerClient.database.RobotWorldsDB;
import za.co.wethinkcode.ServerClient.database.dai.ServerDAI;
import za.co.wethinkcode.ServerClient.database.dao.ServerDAO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * ServerDaoImpl class
 */
public class ServerDaoImpl {
    public static final ServerDAI serverQUERY = QueryTool.getQuery(RobotWorldsDB.getConnection(), ServerDAI.class);

    /**
     * Get server by name
     * @param name Server name
     * @return Server
     */
    public ServerDAO findByName(String name) {
        return Optional.of(serverQUERY.getByName(name)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Get server by id
     * @param id Server id
     * @return Server
     */
    public ServerDAO findById(int id) {
        return Optional.of(serverQUERY.getById(id)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Get server by ip address
     * @param ipAddress Server ip address
     * @return Server
     */
    public ServerDAO findByIp(String ipAddress) {
        return Optional.of(serverQUERY.getByIp(ipAddress)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Get all servers
     * @return List of Servers
     */
    public List<ServerDAO> findAll() {
        return Optional.of(serverQUERY.getAllServers()).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Save server
     * @param server Server object to save
     * @return Saved server object
     */
    public ServerDAO add(ServerDAO server) {
        if (!exists(server)) {
            serverQUERY.save(server);
            return findByName(server.getName());
        } else {
            throw new IllegalArgumentException("Server instance already exists.");
        }
    }

    /**
     * Check if the server exists. It exists if any of its properties except the port
     * matches any other servers properties.
     * @param server Server to check
     * @return True if server exists, else false.
     */
    private boolean exists(ServerDAO server) {
        try {
            findById(server.getId());
            return true;
        } catch (NullPointerException | NoSuchElementException e) {
            try {
                findByName(server.getName());
                return true;
            } catch (NullPointerException | NoSuchElementException ex) {
                try {
                    findByIp(server.getIp());
                    return true;
                } catch (NullPointerException | NoSuchElementException exc) {
                    return false;
                }
            }
        }
    }

    /**
     * Delete server by id
     * @param id Server object id
     */
    public void deleteById(int id) {
        try {
            findById(id);
            serverQUERY.delete(id);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new NoSuchElementException();
        }
    }
}
