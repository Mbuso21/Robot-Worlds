package za.co.wethinkcode.ServerClient.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.plugin.openapi.annotations.*;
import za.co.wethinkcode.ServerClient.api.RobotWorldsServer;
import za.co.wethinkcode.ServerClient.database.dao.ServerDAO;
import za.co.wethinkcode.ServerClient.database.daoImpl.ServerDaoImpl;

import java.util.NoSuchElementException;

/**
 * ServerApiHandler class
 */
public class ServerApiHandler {
    private static final ServerDaoImpl serverDaoImpl = new ServerDaoImpl();

    /**
     * Returns all server objects in the database as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    @OpenApi(
            summary = "Get all servers",
            operationId = "getServers",
            path = "/admin/servers",
            method = HttpMethod.GET,
            tags = {"Server"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = ServerDAO[].class)})
            }
    )
    public static void getAll(Context context) {
        try {
            context.json(serverDaoImpl.findAll());
        } catch (RuntimeException e) {
            context.json("Could not find servers or servers do not exist.");
            context.status(HttpCode.NOT_FOUND);
        }
    }

    /**
     * Delete server
     * @param context The Javalin Context for the HTTP POST Request
     */
    @OpenApi(
            summary = "Delete server by ID",
            operationId = "deleteServerById",
            path = "/admin/server/:serverId",
            method = HttpMethod.DELETE,
            pathParams = {@OpenApiParam(name = "serverId", type = Integer.class, description = "The server ID")},
            tags = {"Server"},
            responses = {
                    @OpenApiResponse(status = "204"),
                    @OpenApiResponse(status = "400"),
                    @OpenApiResponse(status = "404")
            }
    )
    public static void delete(Context context) {
        int serverId = context.pathParamAsClass("id", int.class).get();

        try {
            serverDaoImpl.deleteById(serverId);
            context.status(HttpCode.NO_CONTENT);
            context.json("Server with id: '" + serverId + "' deleted successfully.");
        } catch (NoSuchElementException e) {
            context.status(HttpCode.NOT_FOUND);
            context.json("Server with id: '" + serverId + "' does not exist.");
        }
    }

    /**
     * Create a new server
     * @param context The Javalin Context for the HTTP POST Request
     */
    @OpenApi(
            summary = "Create server",
            operationId = "createServer",
            path = "/admin/servers",
            method = HttpMethod.POST,
            tags = {"Server"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = ServerDAO.class)}),
            responses = {
                    @OpenApiResponse(status = "201"),
                    @OpenApiResponse(status = "400")
            }
    )
    public static void create(Context context) {
        ServerDAO server = context.bodyAsClass(ServerDAO.class);
        try {
            ServerDAO newServer = serverDaoImpl.add(server);
            context.header("Location", "server/" + newServer.getName());
            context.status(HttpCode.CREATED);
            context.json(newServer);
        } catch (RuntimeException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.json(e.getMessage());
        }
    }

    /**
     * Select server
     * @param context The Javalin Context for the HTTP POST Request
     */
    @OpenApi(
            summary = "Select server by ID",
            operationId = "selectServerById",
            path = "/admin/server/:serverId",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "serverId", type = Integer.class, description = "The server ID")},
            tags = {"Server"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = ServerDAO.class)}),
                    @OpenApiResponse(status = "400"),
                    @OpenApiResponse(status = "404")
            }
    )
    public static void select(Context context) {
        int serverId = context.pathParamAsClass("id", int.class).get();

        try {
            ServerDAO server = serverDaoImpl.findById(serverId);
            RobotWorldsServer.serverClient.connect(server.getIp(), server.getPort());
            if (!RobotWorldsServer.serverClient.isConnected()) {
                context.status(HttpCode.BAD_REQUEST);
                context.json("The connection could not be established with the selected server.");
            } else {
                context.status(HttpCode.OK);
                context.json("The connection was successfully established.");
            }
        } catch (NoSuchElementException e) {
            context.status(HttpCode.NOT_FOUND);
            context.json("The connection could not be established with the selected server.");
        } catch (RuntimeException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.json("The connection could not be established with the selected server.");
        }
    }
}
