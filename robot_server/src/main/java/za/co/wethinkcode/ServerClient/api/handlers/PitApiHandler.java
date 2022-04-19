package za.co.wethinkcode.ServerClient.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.ServerClient.database.dao.PitDAO;
import za.co.wethinkcode.ServerClient.database.daoImpl.PitDaoImpl;

public class PitApiHandler {
    private static final PitDaoImpl pitDaoImpl = new PitDaoImpl();
    /**
     * Returns all obstacle objects in the database as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getAll(Context context) {
        context.json(pitDaoImpl.findAll());
    }

    /**
     * Returns all pit objects with the specified world id as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getAllByWorldId(Context context) {
        int id = context.pathParamAsClass("id", int.class).get();

        context.json(pitDaoImpl.findAllByWorldId(id));
    }

    /**
     * Returns the specified pit as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getById(Context context) {
        int id = context.pathParamAsClass("id", int.class).get();
        PitDAO pit = pitDaoImpl.findById(id);
        context.json(pit);
    }

    /**
     * Create a new pit
     *
     * @param context The Javalin Context for the HTTP POST Request
     */
    public static void create(Context context) {
       PitDAO pit = context.bodyAsClass(PitDAO.class);
       PitDAO newPit = pitDaoImpl.add(pit);
        context.header("Location", "/world/pit/" + newPit.getId());
        context.status(HttpCode.CREATED);
        context.json(pit);
    }

    public static void delete(Context context) {
        PitDAO pit = context.bodyAsClass(PitDAO.class);
        pitDaoImpl.deleteById(pit.getId());
    }
}
