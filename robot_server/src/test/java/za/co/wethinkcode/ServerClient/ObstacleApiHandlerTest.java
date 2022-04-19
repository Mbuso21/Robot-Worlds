package za.co.wethinkcode.ServerClient;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

import org.junit.jupiter.api.*;

import za.co.wethinkcode.ServerClient.api.RobotWorldsServer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObstacleApiHandlerTest {
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
    @DisplayName("POST /admin/obstacles")
    void createObstacles() throws UnirestException {
        String jsonString = "{\n" +
                "    \"obstacles\": [\n" +
                "\n" +
                "        {   \n" +
                "            \"worldId\": 1,\n" +
                "            \"position\": \"[3,8]\",\n" +
                "            \"size\": \"2\"\n" +
                "        },\n" +
                "        { \n" +
                "            \"worldId\": 1,\n" +
                "            \"position\": \"[-2,-4]\",\n" +
                "            \"size\": \"3\"\n" +
                "        },\n" +
                "        {   \n" +
                "            \"worldId\": 1,\n" +
                "            \"position\": \"[-12,4]\",\n" +
                "            \"size\": \"4\"\n" +
                "        }\n" +
                "    ]\n" +
                "        \n" +
                "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/admin/obstacles")
                .header("Content-Type", "application/json")
                .body(jsonString)
                .asJson();

        assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("GET /admin/obstacles")
    public void getObstacles() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/admin/obstacles").asJson();
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
    }

    @Test
    @DisplayName("Delete /admin/obstacles")
    void deleteObstacles() throws UnirestException {
        String jsonString = "{\n" +
                "    \"obstacles\": [\n" +
                "\n" +
                "        {   \n" +
                "            \"id\": 1,\n" +
                "            \"worldId\": 1,\n" +
                "            \"position\": \"[3,8]\",\n" +
                "            \"size\": \"2\"\n" +
                "        },\n" +
                "        { \n" +
                "            \"id\": 2,\n" +
                "            \"worldId\": 1,\n" +
                "            \"position\": \"[-2,-4]\",\n" +
                "            \"size\": \"3\"\n" +
                "        }\n" +
                "    ]\n" +
                "        \n" +
                "}";

        HttpResponse<JsonNode> response = Unirest.delete("http://localhost:8080/admin/obstacles")
                .header("Content-Type", "application/json")
                .body(jsonString)
                .asJson();

        assertEquals(204, response.getStatus());

        jsonString = "{\n" +
                "    \"obstacles\": [\n" +
                "\n" +
                "        {   \n" +
                "            \"id\": 3,\n" +
                "            \"worldId\": 1,\n" +
                "            \"position\": \"[3,8]\",\n" +
                "            \"size\": \"2\"\n" +
                "        }\n" +
                "    ]\n" +
                "        \n" +
                "}";

        response = Unirest.delete("http://localhost:8080/admin/obstacles")
                .header("Content-Type", "application/json")
                .body(jsonString)
                .asJson();

        assertEquals(response.getStatus(), 404);
    }
}
