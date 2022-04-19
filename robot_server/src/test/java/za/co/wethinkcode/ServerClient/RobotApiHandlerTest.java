package za.co.wethinkcode.ServerClient;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.ServerClient.api.RobotWorldsServer;
import za.co.wethinkcode.ServerClient.database.dao.RobotDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobotApiHandlerTest {
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
    @DisplayName("POST /admin/robots")
    void createRobot() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/admin/robots")
                .header("Content-Type", "application/json")
                .body(RobotDAO.create("RR2", 1, "[0,0]", "NORTH"))
                .asJson();

        assertEquals(201, response.getStatus());
        assertEquals("/admin/robot/RR2", response.getHeaders().getFirst("Location"));

        response = Unirest.post("http://localhost:8080/admin/robots")
                .header("Content-Type", "application/json")
                .body(RobotDAO.create("CC7", 1, "[3,-7]", "EAST"))
                .asJson();

        assertEquals(201, response.getStatus());
        assertEquals("/admin/robot/CC7", response.getHeaders().getFirst("Location"));
    }

    @Test
    @DisplayName("GET /admin/robots")
    public void getAllRobots() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/admin/robots").asJson();
        assertEquals(201, response.getStatus());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));

        JSONArray jsonArray = response.getBody().getArray();

        assertEquals(2, jsonArray.length());

        JSONObject robot1 = (JSONObject) jsonArray.get(0);

        assertEquals( "RR2", robot1.getString("name"));
        assertEquals( "[0,0]", robot1.getString("position"));
        assertEquals( "NORTH", robot1.getString("direction"));

        JSONObject robot2 = (JSONObject) jsonArray.get(1);

        assertEquals( "CC7", robot2.getString("name"));
        assertEquals( "[3,-7]", robot2.getString("position"));
        assertEquals( "EAST", robot2.getString("direction"));
    }

    @Test
    @DisplayName("Delete /admin/robot/{name}")
    void deleteRobot() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.delete("http://localhost:8080/admin/robot/CC7")
                .header("Content-Type", "application/json")
                .asJson();

        assertEquals(204, response.getStatus());

        response = Unirest.delete("http://localhost:8080/admin/robot/CC7")
                .header("Content-Type", "application/json")
                .asJson();
        assertEquals(response.getBody().toString(), "Robot with name: 'CC7' not found.");
    }

    @Test
    @DisplayName("POST /robot/{command}")
    public void command() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/robot")
                .body(new JSONObject("{ "
                        +"\"robot\": \"RR2\", "
                        +"\"command\": \"launch\", "
                        +"\"arguments\": [\"shooter\",\"5\",\"5\"]"
                        +"}"))
                .asJson();

        assertEquals( 200, response.getStatus());
    }
}
