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
 * ForwardCommandTest class runs the robot forward command test cases
 * @see ForwardCommandTest
 */
class ForwardCommandTest {

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
     * Forward Command Test Scenario: At the edge of the world
     */
    @Test
    void robotAtEdgeOfWorld(){
        // Given that I am connected to a running Robot Worlds server
        Assertions.assertTrue(serverClient.isConnected());

        // And the world is of size 1x1 with no obstacles or pits
        // And a robot called "HAL" is already connected and launched
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\" " +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        // When I send a command for "HAL" to move forward by 5 steps
        request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"forward\", " +
                "  \"arguments\": [\"5\"] " +
                "}";
        response = serverClient.sendRequest(request);

        // Then I should get an "OK" response with the message "At the NORTH edge"
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        Assertions.assertNotNull(response.get("data"));
        Assertions.assertNotNull(response.get("data").get("message"));
        Assertions.assertTrue(response.get("data").get("message").asText().contains("At the NORTH edge"));

        // and the position information returned should be at co-ordinates [0,0]
        Assertions.assertNotNull(response.get("data"));
        Assertions.assertEquals(0, response.get("data").get("position").get(0).asInt());
        Assertions.assertEquals(0, response.get("data").get("position").get(1).asInt());
    }

}