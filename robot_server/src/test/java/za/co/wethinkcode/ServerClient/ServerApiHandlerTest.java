package za.co.wethinkcode.ServerClient;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.ServerClient.api.RobotWorldsServer;
import za.co.wethinkcode.ServerClient.database.dao.ServerDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerApiHandlerTest {
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
    @DisplayName("POST /admin/servers")
    void createServer() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/admin/servers")
                .header("Content-Type", "application/json")
                .body(ServerDAO.create("WW2", 5000, "localhost"))
                .asJson();
        assertEquals(201, response.getStatus());
        assertEquals("/admin/server/WW2", response.getHeaders().getFirst("Location"));

        response = Unirest.post("http://localhost:8080/admin/servers")
                .header("Content-Type", "application/json")
                .body(ServerDAO.create("Dummy", 6000, "127.0.1.3"))
                .asJson();
        assertEquals(201, response.getStatus());
        assertEquals("/admin/server/Dummy", response.getHeaders().getFirst("Location"));
    }

    @Test
    @DisplayName("GET /admin/servers")
    public void getAllServers() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/admin/servers").asJson();
        assertEquals(201, response.getStatus());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));

        JSONArray jsonArray = response.getBody().getArray();

        assertEquals(2, jsonArray.length());

        JSONObject server1 = (JSONObject) jsonArray.get(0);

        assertEquals( "WW2", server1.getString("name"));
        assertEquals( 5000, server1.getInt("port"));
        assertEquals( "localhost", server1.getString("ip"));

        JSONObject server2 = (JSONObject) jsonArray.get(1);

        assertEquals( "Dummy", server2.getString("name"));
        assertEquals( 6000, server2.getInt("port"));
        assertEquals( "127.0.1.3", server2.getString("ip"));
    }

    @Test
    @DisplayName("Delete /admin/server/{id}")
    void deleteServer() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.delete("http://localhost:8080/admin/server/2")
                .header("Content-Type", "application/json")
                .asJson();

        assertEquals(204, response.getStatus());

        response = Unirest.delete("http://localhost:8080/admin/server/2")
                .header("Content-Type", "application/json")
                .asJson();
        assertEquals(response.getBody().toString(), "Server with id: '2' does not exist.");
    }

    @Test
    @DisplayName("GET /admin/server/{id}")
    public void selectServer() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/admin/server/1").asJson();
        assertEquals(201, response.getStatus());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));

        assertEquals( 200, response.getStatus());
        assertEquals( "The connection was successfully established.", response.getBody().toString());
    }
}
