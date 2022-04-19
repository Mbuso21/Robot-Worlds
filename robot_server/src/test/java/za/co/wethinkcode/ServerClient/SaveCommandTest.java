package za.co.wethinkcode.ServerClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.ServerClient.client.RobotWorldClient;
import za.co.wethinkcode.ServerClient.client.RobotWorldJsonClient;

/**
 * SaveCommandTest class runs the save test cases
 */
public class SaveCommandTest {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();


    /**
     * After each test we will reset the world state and disconnect from the server,
     * so each test can start with a clean world state.
     */
    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }

    /**
     * Save Command Test Scenario: Save new world.
     */
    @Test
    void saveNewWorld() {
        // Given a world of size 1x1
        // and the world has no obstacles, pits or mines
        // and the world has never been saved before
        // When I give the save command
        // Then I should get a response back with ...

        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(launchRequest);

        String saveRequest = "{" +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"RoroWorld\"]" +
                "}";

        JsonNode response1 = serverClient.sendRequest(saveRequest);

        // Validating the response from the server
        Assertions.assertNotNull(response1.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        /*
            CODE: HERE....
         */
    }

    /**
     * Save Command Test Scenario: Saving an already existing new world should update.
     */
    @Test
    void saveExistingWorld() {
        // Given a world of size 1x1
        // and the world has no obstacles, pits or mines
        // and the world has never been saved before
        // When I give the save command
        // Then I should get a response back with ...

        String saveRequest = "{" +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"RoroWorld\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(saveRequest);

        // Validating the response from the server
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());


        Assertions.assertEquals(1, response.get("data").get("size").asInt());
        Assertions.assertEquals(0, response.get("data").get("mines").asInt());
        Assertions.assertEquals(0, response.get("data").get("pits").asInt());
        Assertions.assertEquals(0, response.get("data").get("obstacles").asInt());
    }

    /**
     * Save Command Test Scenario: Saving a world with no name should fail.
     */
    @Test
    void saveWorldWithNoShouldFail() {
        // Given a world of size 1x1
        // and the world has no obstacles, pits or mines
        // and the world has never been saved before
        // When I give the save command
        // Then I should get a response back with ...

        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(launchRequest);

        String saveRequest = "{" +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"RoroWorld\"]" +
                "}";

        JsonNode response1 = serverClient.sendRequest(saveRequest);

        // Validating the response from the server
        Assertions.assertNotNull(response1.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        /*
            CODE: HERE....
         */
    }
}
