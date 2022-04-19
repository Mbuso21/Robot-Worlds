package za.co.wethinkcode.ServerClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import za.co.wethinkcode.ServerClient.client.RobotWorldClient;
import za.co.wethinkcode.ServerClient.client.RobotWorldJsonClient;

import java.io.IOException;

/**
 * StateTest class runs the robot state test cases.
 */
public class StateTest {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private static final String serverJarPath = System.getProperty("user.dir") + "/target/uss-newton.jar";
    private static Process process;

    /**
     * Before all tests (i.e. methods indicated by the @Test annotation) we will run the server.
     */
    @BeforeAll
    static void runServer() {
        try {
            // Runs server on separate thread using Process
            process = Runtime.getRuntime().exec("java -jar " + serverJarPath);
            // Waits for the server to run completely
            Thread.sleep(5000);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 	Before each test (i.e. methods indicated by the @Test annotation) we will connect to the server.
     */
    @BeforeEach
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    /**
     * After each test we will reset the world state and disconnect from the server,
     * so each test can start with a clean world state.
     */
    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }

    /**
     * After all tests (i.e. methods indicated by the @Test annotation) we will close the server.
     */
    @AfterAll
    static void closeServer(){
        do {
            process.destroyForcibly();
        } while(process.isAlive());
    }

    /**
     * State Test Scenario: The robot exists in the world.
     */
    @Test
    void robotExists(){
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        Assertions.assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\", " +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        // When I send a valid state request to the server
        String request2 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"state\"," +
                "  \"arguments\": [] " +
                "}";
        JsonNode response2 = serverClient.sendRequest(request2);

        // Then I should get a valid response from the server
        Assertions.assertNotNull(response2.get("result"));
        Assertions.assertEquals("OK", response2.get("result").asText());

        // The status should be TODO and not null
        Assertions.assertNotNull(response.get("data"));
        Assertions.assertEquals("TODO", response.get("state").get("status").asText());
    }

    /**
     * State Test Scenario: The robot is not in the world.
     */
    @Test
    void robotDoesNotExist() {
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)

        Assertions.assertTrue(serverClient.isConnected());

        // When I sent a valid state command without launching
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                " \"command\": \"state\" " +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // I should get an error
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("ERROR", response.get("result").asText());

        // The message should be that the "Robot does not exist"
        Assertions.assertNotNull(response.get("data"));
        Assertions.assertNotNull(response.get("data").get("message"));
        Assertions.assertTrue(response.get("data").get("message").asText().contains("Robot does not exist"));
    }
}
