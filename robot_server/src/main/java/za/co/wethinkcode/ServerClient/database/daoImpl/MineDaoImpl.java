package za.co.wethinkcode.ServerClient.database.daoImpl;

import net.lemnik.eodsql.QueryTool;
import za.co.wethinkcode.ServerClient.database.RobotWorldsDB;
import za.co.wethinkcode.ServerClient.database.dai.MineDAI;
import za.co.wethinkcode.ServerClient.database.dao.MineDAO;

import java.util.List;

public class MineDaoImpl {
    private static final MineDAI mineQUERY =
            QueryTool.getQuery(RobotWorldsDB.getConnection(), MineDAI.class);

    public MineDAO findById(int mineId) {

        /**
         * TODO: 1 -> Check if it exists, if so continue else 2 -> throw not found error
         */
        return mineQUERY.getById(mineId);
    }

    public MineDAO findByPosition(String minePosition) {

        /**
         * TODO: 1 -> Check if it exists, if so continue else 2 -> throw not found error
         */
        return mineQUERY.getByPosition(minePosition);
    }

    public List<MineDAO> findAll() {
        return mineQUERY.getAll();
    }

    public MineDAO add(MineDAO mine) {
        /**
         * TODO: 1 -> Check if it already exists, if so call update(world) else 2 -> Continue with save
         */
        mineQUERY.save(mine);
        return findByPosition(mine.getPosition());
    }

    public void update(MineDAO mine) {

        /**
         * TODO: 1 -> Get the world object with name, 2 -> Create a new world object and assign the updates
         */
        mineQUERY.save(mine);
    }

    public void deleteById(int mineId) {
        /**
         * TODO: 1 -> Check if it exists, if so continue with delete, else 2 -> throw not found error
         */
        mineQUERY.delete(mineId);
    }

    public List<MineDAO> findAllByWorldId(int worldId) {
        return mineQUERY.getAllByWorldId(worldId);
    }
}
