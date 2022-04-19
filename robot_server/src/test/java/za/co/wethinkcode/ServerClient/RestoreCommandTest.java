package za.co.wethinkcode.ServerClient;

import com.fasterxml.jackson.databind.JsonNode;
import za.co.wethinkcode.ServerClient.client.RobotWorldClient;
import za.co.wethinkcode.ServerClient.client.RobotWorldJsonClient;

/**
 * RestoreCommandTest class runs the restore test cases
 */
public class RestoreCommandTest {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    /**
     * Tests whether a player is able to restore a world, that was saved in a database.
     */
    void restoreExistingWorld() {
        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(launchRequest);
    }

    void restoreNonExistingWorld() {

    }
}
