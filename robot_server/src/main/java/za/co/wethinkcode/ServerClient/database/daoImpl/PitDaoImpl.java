package za.co.wethinkcode.ServerClient.database.daoImpl;

import net.lemnik.eodsql.QueryTool;
import za.co.wethinkcode.ServerClient.database.RobotWorldsDB;
import za.co.wethinkcode.ServerClient.database.dai.PitDAI;
import za.co.wethinkcode.ServerClient.database.dao.PitDAO;

import java.util.List;

public class PitDaoImpl {
    private static final PitDAI pitQUERY =
            QueryTool.getQuery(RobotWorldsDB.getConnection(), PitDAI.class);

    public PitDAO findById(int pitId) {

        /**
         * TODO: 1 -> Check if it exists, if so continue else 2 -> throw not found error
         */
        return pitQUERY.getById(pitId);
    }

    public PitDAO findByPosition(String pitPosition) {

        /**
         * TODO: 1 -> Check if it exists, if so continue else 2 -> throw not found error
         */
        return pitQUERY.getByPosition(pitPosition);
    }

    public List<PitDAO> findAll() {
        return pitQUERY.getAll();
    }

    public PitDAO add(PitDAO pit) {
        /**
         * TODO: 1 -> Check if it already exists, if so call update(world) else 2 -> Continue with save
         */
        pitQUERY.save(pit);
        return findByPosition(pit.getPosition());
    }

    public void update(PitDAO pit) {

        /**
         * TODO: 1 -> Get the world object with name, 2 -> Create a new world object and assign the updates
         */
        pitQUERY.save(pit);
    }

    public void deleteById(int pitId) {
        /**
         * TODO: 1 -> Check if it exists, if so continue with delete, else 2 -> throw not found error
         */
       pitQUERY.delete(pitId);
    }

    public List<PitDAO> findAllByWorldId(int worldId) {
        return pitQUERY.getAllByWorldId(worldId);
    }
}
