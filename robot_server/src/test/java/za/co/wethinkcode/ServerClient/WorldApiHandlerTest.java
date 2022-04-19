package za.co.wethinkcode.ServerClient;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.ServerClient.api.RobotWorldsServer;
import za.co.wethinkcode.ServerClient.database.dao.WorldDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorldApiHandlerTest {
    private static RobotWorldsServer server;

    @BeforeAll
    public static void startServer() {
        server = new RobotWorldsServer();
        server.start(8080);
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @Test
    @DisplayName("POST /admin/save")
    void saveWorld() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/admin/save")
                .header("Content-Type", "application/json")
                .body(WorldDAO.create("HAL", 2))
                .asJson();
        assertEquals(201, response.getStatus());
        assertEquals("/admin/save/HAL", response.getHeaders().getFirst("Location"));
    }

    @Test
    @DisplayName("GET /admin/load/{name}")
    public void loadWorld() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/admin/load{name}").asJson();
        assertEquals(201, response.getStatus());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));

        JSONObject jsonObject = response.getBody().getObject();
        assertEquals("HAL", jsonObject.getString("name"));
        assertEquals(2, jsonObject.getInt("author"));
    }
}
