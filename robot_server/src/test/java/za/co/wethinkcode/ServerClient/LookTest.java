package za.co.wethinkcode.ServerClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.ServerClient.client.RobotWorldClient;
import za.co.wethinkcode.ServerClient.client.RobotWorldJsonClient;

import java.io.IOException;

/**
 * LookTest class runs the robot look test cases
 */
public class LookTest {

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
     * Each test is a scenario , it tells a user story which we test
     */

    /**
     * Look Command Test Scenario: The world is empty.
     */
    @Test
    void EmptyWorld(){
        // Given a world of size 1x1
        // and the world has no objects (obstacles, mines or pits)
        Assertions.assertTrue(serverClient.isConnected());

        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                 "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        // And the position should be (x:0, y:0)
        Assertions.assertNotNull(response.get("data"));
        Assertions.assertNotNull(response.get("data").get("position"));
        Assertions.assertEquals(0, response.get("data").get("position").get(0).asInt());
        Assertions.assertEquals(0, response.get("data").get("position").get(1).asInt());

        // And I should also get the state of the robot
        Assertions.assertNotNull(response.get("state"));

        String request1 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";

        JsonNode response1 = serverClient.sendRequest(request1);

        // Then I should get a valid response from the server
        Assertions.assertNotNull(response1.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        // Then the visible objects in the world should be equal to 0 if the world is empty
        Assertions.assertNotNull(response1.get("data"));
        Assertions.assertEquals(0, response1.get("data").get("objects").size());
    }
}
