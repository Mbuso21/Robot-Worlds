package za.co.wethinkcode.ServerClient.api.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.plugin.openapi.annotations.*;
import org.json.JSONObject;
import za.co.wethinkcode.ServerClient.api.RobotWorldsServer;
import za.co.wethinkcode.ServerClient.database.dao.RobotDAO;
import za.co.wethinkcode.ServerClient.database.daoImpl.RobotDaoImpl;

import java.util.NoSuchElementException;

/**
 * RobotApiHandler class
 */
public class RobotApiHandler {
    private static final RobotDaoImpl robotDaoImpl = new RobotDaoImpl();

    /**
     * Returns all robot objects in the database as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    @OpenApi(
            summary = "Get all robots",
            operationId = "getRobots",
            path = "/admin/robots",
            method = HttpMethod.GET,
            tags = {"Robot"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = RobotDAO[].class)})
            }
    )
    public static void getAll(Context context) {
        try {
            context.json(robotDaoImpl.findAll());
        } catch (RuntimeException e) {
            context.status(HttpCode.NOT_FOUND);
            context.json("Could not find robots or robots do not exist.");
        }
    }

    /**
     * Returns a robot object in the database as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    @OpenApi(
            summary = "Get robot by Name",
            operationId = "getRobot",
            path = "/admin/robot/:robotName",
            method = HttpMethod.GET,
            tags = {"robot"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = RobotDAO.class)}),
                    @OpenApiResponse(status = "400"),
                    @OpenApiResponse(status = "404")
            }
    )
    public static void getOne(Context context) {
        String robotName = context.pathParamAsClass("name", String.class).get();

        try {
            RobotDAO robot = robotDaoImpl.findByName(robotName);
            context.status(HttpCode.OK);
            context.json(robot);
        } catch (NullPointerException | NoSuchElementException e) {
            context.status(HttpCode.NOT_FOUND);
            context.json("Robot with name: '" + robotName + "' not found.");
        } catch (RuntimeException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.json(e.getMessage());
        }
    }

    /**
     * Delete robot
     * @param context The Javalin Context for the HTTP POST Request
     */
    @OpenApi(
            summary = "Delete robot by Name",
            operationId = "deleteRobotByName",
            path = "/admin/robots/:robotName",
            method = HttpMethod.DELETE,
            pathParams = {@OpenApiParam(name = "robotName", type = Integer.class, description = "The robot Name")},
            tags = {"Robot"},
            responses = {
                    @OpenApiResponse(status = "204"),
                    @OpenApiResponse(status = "400"),
                    @OpenApiResponse(status = "404")
            }
    )
    public static void delete(Context context) {
        String robotName = context.pathParamAsClass("name", String.class).get();

        try {
            robotDaoImpl.deleteByName(robotName);
            context.status(HttpCode.NO_CONTENT);
            context.json("Robot with name: '" + robotName + "' deleted successfully.");
        } catch (NoSuchElementException e) {
            context.status(HttpCode.NOT_FOUND);
            context.json("Robot with name: '" + robotName + "' not found.");
        }
    }

    /**
     * Create a new robot
     * @param context The Javalin Context for the HTTP POST Request
     */
    @OpenApi(
            summary = "Create robot",
            operationId = "createRobot",
            path = "/admin/robots",
            method = HttpMethod.POST,
            tags = {"robot"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = RobotDAO.class)}),
            responses = {
                    @OpenApiResponse(status = "201"),
                    @OpenApiResponse(status = "400")
            }
    )
    public static void create(Context context) {
        RobotDAO robot = context.bodyAsClass(RobotDAO.class);
        try {
            RobotDAO newRobot = robotDaoImpl.add(robot);
            context.header("Location", "/admin/robot/" + newRobot.getName());
            context.status(HttpCode.CREATED);
            context.json(newRobot);
        } catch (IllegalArgumentException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.json(e.getMessage());
        } catch (RuntimeException e) {
            context.status(HttpCode.BAD_REQUEST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "Failed to create robot.");
            jsonObject.put("cause", e.getMessage());
            context.json(jsonObject);
        }
    }

    /**
     * Execute robot command
     * @param context The Javalin Context for the HTTP POST Request
     */
    @OpenApi(
            summary = "Create robot command",
            operationId = "createRobotCommand",
            path = "/robot/",
            method = HttpMethod.POST,
            tags = {"robot"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = JSONObject.class)}),
            responses = {
                    @OpenApiResponse(status = "201"),
                    @OpenApiResponse(status = "400")
            }
    )
    public static void command(Context context) {
        if (!RobotWorldsServer.serverClient.isConnected()) {
            context.status(HttpCode.BAD_REQUEST);
            context.json("There is no client server connection established.");
        } else {
            JSONObject request = new JSONObject(context.body());
            JsonNode response = RobotWorldsServer.serverClient.sendRequest(request.toString(4));

            HttpCode status = response.get("result")
                    .asText()
                    .equalsIgnoreCase("OK") ? HttpCode.OK : HttpCode.BAD_REQUEST;

            context.status(status);
            context.json(new JSONObject(response.asText()));
        }
    }
}