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
 * LaunchRobotTest class runs robot launch test cases
 */
public class LaunchRobotTest {

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
     * Launch Test Scenario: Valid launch command should succeed
     */
    @Test
    public void validLaunchShouldSucceed(){
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        Assertions.assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        // And the position should be (x:0, y:0)
        Assertions.assertNotNull(response.get("data"));
        Assertions.assertNotNull(response.get("data").get("position").asText());
        Assertions.assertEquals(0, response.get("data").get("position").get(0).asInt());
        Assertions.assertEquals(0, response.get("data").get("position").get(1).asInt());

        // And I should also get the state of the robot
        Assertions.assertNotNull(response.get("state"));
    }

    /**
     * Launch Test Scenario: Incorrect launch command should fail
     */
    @Test
    void invalidLaunchShouldFail(){
        // Given that I am connected to a running Robot Worlds server
        Assertions.assertTrue(serverClient.isConnected());

        // When I send an invalid launch request with the command "luanch" instead of "launch"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"luanch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error response
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("ERROR", response.get("result").asText());

        // And the message "Unsupported command"
        Assertions.assertNotNull(response.get("data"));
        Assertions.assertNotNull(response.get("data").get("message"));
        Assertions.assertTrue(response.get("data").get("message").asText().contains("Unsupported command"));
    }

    /**
     * Launch Test Scenario: No more space in the world for another robot
     */
    @Test
    void noMoreSpaceForAnotherRobotInTheWorld(){
        // Given that I am connected to a running Robot Worlds server
        Assertions.assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        // And I send a second launch request
        request = "{" +
                "  \"robot\": \"KILLUA\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        response = serverClient.sendRequest(request);

        // I should get an error that there are too many robots
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("ERROR", response.get("result").asText());

        // And the message "No more space in this world"
        Assertions.assertNotNull(response.get("data"));
        Assertions.assertNotNull(response.get("data").get("message"));
        Assertions.assertTrue(response.get("data").get("message").asText().contains("No more space in this world"));
    }

    /**
     * Launch Test Scenario: Robot with the same name is already in the world
     */
   @Test
    void sameNameRobotError(){
        // Given that I am connected to a running Robot Worlds server
        Assertions.assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        // When I send a valid launch request to the server for another robot of the same name
        request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("ERROR", response.get("result").asText());

        // And the message ""Too many of you in this world""
        Assertions.assertNotNull(response.get("data").get("message"));
        Assertions.assertTrue(response.get("data").get("message").asText().contains("Too many of you in this world"));
    }
}