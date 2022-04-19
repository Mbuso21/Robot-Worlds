package za.co.wethinkcode.ServerClient.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.ServerClient.database.dao.MineDAO;
import za.co.wethinkcode.ServerClient.database.daoImpl.MineDaoImpl;

public class MineApiHandler {
    private static final MineDaoImpl mineDaoImpl = new MineDaoImpl();

    /**
     * Returns all mine objects in the database as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getAll(Context context) {
        context.json(mineDaoImpl.findAll());
    }

    /**
     * Returns all mine objects with the specified world id as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getAllByWorldId(Context context) {
        int id = context.pathParamAsClass("id", int.class).get();

        context.json(mineDaoImpl.findAllByWorldId(id));
    }

    /**
     * Returns the specified mine as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getById(Context context) {
        int id = context.pathParamAsClass("id", int.class).get();
        MineDAO mine = mineDaoImpl.findById(id);
        context.json(mine);
    }

    /**
     * Create a new mine
     *
     * @param context The Javalin Context for the HTTP POST Request
     */
    public static void create(Context context) {
        MineDAO mine = context.bodyAsClass(MineDAO.class);
        MineDAO newMine = mineDaoImpl.add(mine);
        context.header("Location", "/world/mine/" + newMine.getId());
        context.status(HttpCode.CREATED);
        context.json(mine);
    }

    public static void delete(Context context) {
        MineDAO mine = context.bodyAsClass(MineDAO.class);
        mineDaoImpl.deleteById(mine.getId());
    }
}

