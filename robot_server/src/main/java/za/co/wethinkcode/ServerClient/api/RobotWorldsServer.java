package za.co.wethinkcode.ServerClient.api;

import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.swagger.v3.oas.models.info.Info;
import za.co.wethinkcode.ServerClient.api.handlers.*;
import za.co.wethinkcode.ServerClient.client.RobotWorldJsonClient;
import za.co.wethinkcode.ServerClient.database.RobotWorldsDB;

/**
 * RobotWorldsServer class
 */
public class RobotWorldsServer {
    private final Javalin server;
    private static RobotWorldsDB robotWorldsDb;
    public static RobotWorldJsonClient serverClient;

    public RobotWorldsServer(String dbToUse) {
        serverClient = new RobotWorldJsonClient();
        robotWorldsDb = new RobotWorldsDB(new String[]{"-f", dbToUse});
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.enableCorsForAllOrigins();
            config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
        });
        run();
    }

    public RobotWorldsServer() {
        serverClient = new RobotWorldJsonClient();
        robotWorldsDb = new RobotWorldsDB(new String[]{"-f", "RobotWorldsDB.sqlite"});
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.enableCorsForAllOrigins();
            config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
        });

        run();
    }

    private void run(){
        this.server.get("/admin/servers", ServerApiHandler::getAll);
        this.server.post("/admin/servers", ServerApiHandler::create);
        this.server.delete("/admin/server/{id}", ServerApiHandler::delete);
        this.server.get("/admin/server/{id}", ServerApiHandler::select);

        this.server.get("/admin/robots", RobotApiHandler::getAll);
        this.server.get("/admin/robot/{name}", RobotApiHandler::getOne);
        this.server.post("/admin/robots", RobotApiHandler::create);
        this.server.delete("/admin/robot/{name}", RobotApiHandler::delete);

        this.server.post("/admin/obstacles", ObstacleApiHandler::create);
        this.server.get("/admin/obstacles", ObstacleApiHandler::getAll);
        this.server.delete("/admin/obstacles", ObstacleApiHandler::delete);

        this.server.post("/admin/save/{name}", WorldApiHandler::create);
        this.server.get("/admin/load/{name}", WorldApiHandler::load);

        this.server.post("/robot/{command}", RobotApiHandler::command);
    }

    public static void main(String[] args) {
        RobotWorldsServer server = new RobotWorldsServer();
        server.start(8080);
    }

    public void start(int port) {
        this.server.start(port);
    }

    public void stop() {
        this.server.stop();
    }

    private OpenApiOptions getOpenApiOptions() {
        Info applicationInfo = new Info()
                .version("1.0")
                .description("RobotWorlds Server");
        return new OpenApiOptions(applicationInfo).path("/swagger-docs");
    }
}