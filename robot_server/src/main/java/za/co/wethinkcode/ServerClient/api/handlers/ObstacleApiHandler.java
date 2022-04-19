package za.co.wethinkcode.ServerClient.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.plugin.openapi.annotations.*;
import org.json.JSONArray;
import org.json.JSONObject;
import za.co.wethinkcode.ServerClient.database.dao.ObstacleDAO;
import za.co.wethinkcode.ServerClient.database.dao.RobotDAO;
import za.co.wethinkcode.ServerClient.database.daoImpl.ObstacleDaoImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * ObstacleApiHandler class
 */
public class ObstacleApiHandler {
    private static final ObstacleDaoImpl obstacleDaoImpl = new ObstacleDaoImpl();

    /**
     * Returns all obstacle objects in the database as JSON in the response
     * @param context The Javalin Context for the HTTP GET Request
     */
    @OpenApi(
            summary = "Get all obstacles",
            operationId = "getObstacles",
            path = "/admin/obstacles",
            method = HttpMethod.GET,
            tags = {"Obstacle"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = ObstacleDAO[].class)})
            }
    )
    public static void getAll(Context context) {
        try {
            context.json(obstacleDaoImpl.findAll());
        } catch (RuntimeException e) {
            context.status(HttpCode.NOT_FOUND);
            context.json("Could not find obstacles or obstacles do not exist.");
        }
    }

    /**
     * Create obstacles
     * @param context The Javalin Context for the HTTP POST Request
     */
    @OpenApi(
            summary = "Create obstacles by obstacles request bosy",
            operationId = "createObstaclesByRequestBody",
            path = "/admin/obstacles",
            method = HttpMethod.POST,
            tags = {"Obstacle"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = ObstacleDAO[].class)}),
            responses = {
                    @OpenApiResponse(status = "201"),
                    @OpenApiResponse(status = "400")
            }
    )
    public static void create(Context context) {
        JSONObject jsonObject = new JSONObject(context.body());
        JSONArray jsonArray = jsonObject.getJSONArray("obstacles");
        List<ObstacleDAO> obstacles = new ArrayList<>();

        try {
            for (Object object: jsonArray) {
                obstacles.add(ObstacleDAO.createFromJsonObject((JSONObject)object));
            }

            for (ObstacleDAO obstacle: obstacles) {
                obstacleDaoImpl.add(obstacle);
            }

            context.header("Location", "/admin/obstacles");
            context.status(HttpCode.CREATED);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("obstacles", new JSONArray(obstacleDaoImpl.findAll()));
            jsonResponse.put("message", "Added obstacles successfully.");
            context.json(jsonResponse);
        } catch (RuntimeException e) {
            e.printStackTrace();
            context.status(HttpCode.BAD_REQUEST);
            JSONObject jsonError = new JSONObject();
            jsonError.put("message", "Failed to create obstacle.");
            jsonError.put("cause", e.getMessage());
            context.json(jsonError);
        }
    }

    /**
     * Delete obstacles
     * @param context The Javalin Context for the HTTP POST Request
     */
    @OpenApi(
            summary = "Delete obstacles by obstacles request body",
            operationId = "deleteObstaclesByBody",
            path = "/admin/obstacles",
            method = HttpMethod.DELETE,
            requestBody = @OpenApiRequestBody( content = {@OpenApiContent(from = RobotDAO[].class)}),
            tags = {"Obstacle"},
            responses = {
                    @OpenApiResponse(status = "204"),
                    @OpenApiResponse(status = "400"),
                    @OpenApiResponse(status = "404")
            }
    )
    public static void delete(Context context) {
        JSONObject jsonObject = new JSONObject(context.body());
        JSONArray jsonArray = jsonObject.getJSONArray("obstacles");
        System.out.println(jsonArray.toString(4));
        try {
            for (Object object: jsonArray) {
                obstacleDaoImpl.deleteById(((JSONObject) object).getInt("id"));
            }

            context.status(HttpCode.NO_CONTENT);
            context.json("Deleted obstacles successfully.");
        } catch (RuntimeException e) {
            context.status(HttpCode.BAD_REQUEST);
            JSONObject jsonError = new JSONObject();
            jsonError.put("message", "Failed to delete obstacles.");
            jsonError.put("cause", e.getMessage());
            context.json(jsonError);
        }
    }
}
