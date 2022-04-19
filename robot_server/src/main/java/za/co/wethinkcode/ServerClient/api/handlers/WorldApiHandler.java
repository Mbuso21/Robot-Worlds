package za.co.wethinkcode.ServerClient.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.ServerClient.database.dao.WorldDAO;
import za.co.wethinkcode.ServerClient.database.daoImpl.*;
import za.co.wethinkcode.ServerClient.server.MultiServers;

import java.util.NoSuchElementException;

/**
 * WorldApiHandler class
 */
public class WorldApiHandler {
    private static final WorldDaoImpl worldDaoImpl = new WorldDaoImpl();

    /**
     * Returns all objects in the current world as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getAll(Context context) {
        context.json(worldDaoImpl.findAll());
    }

    /**
     * Create a new world
     * @param context The Javalin Context for the HTTP POST Request
     */
    public static void create(Context context) {
        String name = context.bodyAsClass(String.class);

        try {
            MultiServers.saveWorld(name);
            WorldDAO newWorld = worldDaoImpl.findByName(name);
            context.header("Location", "/admin/load/" + newWorld.getName());
            context.status(HttpCode.CREATED);
            context.json(newWorld);
        } catch (RuntimeException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.json(e.getMessage());
        }
    }

    /**
     * Returns the specified world as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void load(Context context) {
        String name = context.pathParamAsClass("name", String.class).get();

        try {
            MultiServers.restoreWorld(name);
            context.status(HttpCode.OK);
            context.json(worldDaoImpl.findByName(name));
        } catch (NullPointerException | NoSuchElementException e) {
            context.status(HttpCode.NOT_FOUND);
            context.json("The world with name: " + name + " was not found.");
        } catch (RuntimeException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.json(e.getMessage());
        }
    }
}
