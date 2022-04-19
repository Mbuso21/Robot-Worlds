package za.co.wethinkcode.ServerClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.ServerClient.client.RobotWorldClient;
import za.co.wethinkcode.ServerClient.client.RobotWorldJsonClient;
import za.co.wethinkcode.ServerClient.server.MultiServers;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * LookRobotArgsTest class is a LookTest with tests that have special args
 * that are needed for them pass (i.e. 2x2 world and obstacles)
 * @see LookTest
 */
public class LookRobotArgsTest {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private static final String serverJarPath = System.getProperty("user.dir") + "/target/uss-newton.jar";
    private static Process process;

    /**
     * After each test we will reset the world state and disconnect from the server,
     * so each test can start with a clean world state.
     */
    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
        closeServer();
    }

    /**
     * Takes a String array as a parameter and runs the MultiServer class.
     * @param command the string command containing arguments used to configure the server
     * @see MultiServers
     */
    void runServerWithArgs(String command) {
        try {
            // Runs server on separate thread using Process
            process = Runtime.getRuntime().exec("java -jar " + serverJarPath + " " + command);
            // Waits for the server to run completely
            Thread.sleep(5000);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method closes the server.
     */
    void closeServer(){
        do {
            process.destroyForcibly();
        } while(process.isAlive());
    }

    /**
     * Look Command Test Scenario: See an obstacle.
     */
    @Test
    void SeeAnObstacle(){
        // Given a world of size 2x2
        // and the world has an obstacle at coordinate [0,1]
        // and I have successfully launched a robot into the world
        // When I ask the robot to look
        // Then I should get a response back with an object of type OBSTACLE at a distance of 1 step
        runServerWithArgs("-s 2 -o 0,1");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        Assertions.assertTrue(serverClient.isConnected());

        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        String request1 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";

        JsonNode response1 = serverClient.sendRequest(request1);

        // Validating the response from the server
        Assertions.assertNotNull(response1.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        // Then the number of visible objects in the world should be equal to 1 because the world has only one other
        // world object apart from the looking robot itself which is the obstacle positioned at [0,1]

        Assertions.assertNotNull(response1.get("data"));
        Assertions.assertEquals(1, response1.get("data").get("objects").size());
        Assertions.assertEquals("OBSTACLE", response1.get("data").get("objects").findValue("type").asText());

        // The distance the robot at [0,0] and the obstacle at [0,1] should be 1 given their relative positions
        Assertions.assertEquals(1, response1.get("data").get("objects").findValue("distance").asInt());

        // The direction of the obstacle should be NORTH since its position y-value
        // is greater than the robot's position y-value
        Assertions.assertEquals("NORTH", response1.get("data").get("objects").findValue("direction").asText());
    }

    /**
     * Look Command Test Scenario: See robot.
     */
    @Test
    void seeRobotsAndObstacles(){
        // Given a world of size 2x2
        // and the world has an obstacle at coordinate [0,1]
        // and I have successfully launched 8 robots into the world
        runServerWithArgs("-s 2 -o 0,1");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        String sightLaunchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";

        JsonNode response1 = serverClient.sendRequest(sightLaunchRequest);

        String avariceLaunchRequest = "{" +
                "  \"robot\": \"AVARICE\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";

        JsonNode response2 = serverClient.sendRequest(avariceLaunchRequest);

        String envyLaunchRequest = "{" +
                "  \"robot\": \"ENVY\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";

        JsonNode response3= serverClient.sendRequest(envyLaunchRequest);

        String gluttonyLaunchRequest = "{" +
                "  \"robot\": \"GLUTTONY\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";

        JsonNode response4 = serverClient.sendRequest(gluttonyLaunchRequest);

        String lustLaunchRequest = "{" +
                "  \"robot\": \"LUST\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";

        JsonNode response5 = serverClient.sendRequest(lustLaunchRequest);

        String slothLaunchRequest = "{" +
                "  \"robot\": \"SLOTH\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";

        JsonNode response6 = serverClient.sendRequest(slothLaunchRequest);

        String vanityLaunchRequest = "{" +
                "  \"robot\": \"VANITY\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";

        JsonNode response7 = serverClient.sendRequest(vanityLaunchRequest);

        String wrathLaunchRequest = "{" +
                "  \"robot\": \"WRATH\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"] " +
                "}";

        JsonNode response8 = serverClient.sendRequest(wrathLaunchRequest);

        String sightLookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";

        JsonNode response9 = serverClient.sendRequest(sightLookRequest);

        // Validating the responses from the server
        Assertions.assertNotNull(response1.get("result"));
        Assertions.assertEquals("OK", response1.get("result").asText());
        Assertions.assertNotNull(response2.get("result"));
        Assertions.assertEquals("OK", response2.get("result").asText());
        Assertions.assertNotNull(response3.get("data"));
        Assertions.assertEquals("OK", response3.get("result").asText());
        Assertions.assertNotNull(response4.get("data"));
        Assertions.assertEquals("OK", response4.get("result").asText());
        Assertions.assertNotNull(response5.get("data"));
        Assertions.assertEquals("OK", response5.get("result").asText());
        Assertions.assertNotNull(response6.get("data"));
        Assertions.assertEquals("OK", response6.get("result").asText());
        Assertions.assertNotNull(response7.get("data"));
        Assertions.assertEquals("OK", response7.get("result").asText());
        Assertions.assertNotNull(response8.get("data"));
        Assertions.assertEquals("OK", response8.get("result").asText());

        // The number of visible objects returned should be 4 since the robot is at the center of world
        // with size 2, given that the world is full and the robot can only see in 4 directions
        Assertions.assertEquals(4, response9.get("data").get("objects").size());

        // There should be three robots and one obstacle, each being one step away from the observing robot
        Assertions.assertNotNull(response9.get("data"));

        // The robot should be able to see all 4 visible objects in all 4 different direction (NORTH, SOUTH, WEST, EAST)
        Assertions.assertTrue( response9.get("data").get("objects").findValues("direction")
                .stream()
                .map(JsonNode::asText)
                .collect(Collectors.toList())
                .containsAll(List.of(new String[]{"NORTH", "SOUTH", "WEST", "EAST"}))
        );

        // All visible objects should be 1 step away from the robot
        response9.get("data").get("objects").findValues("distance").forEach( value ->
                Assertions.assertEquals(1, value.asInt()));

        // The robot should see 4 visible objects - which are 3 ROBOTS and 1 OBSTACLE
        List<String> objectTypes = response9.get("data").get("objects").findValues("type")
                .stream()
                .map(JsonNode::asText)
                .collect(Collectors.toList());

        // The robot should see 3 ROBOTS
        Assertions.assertEquals(3, Collections.frequency(objectTypes, "ROBOT"));

        // The robot should see 1 OBSTACLE
        Assertions.assertEquals(1, Collections.frequency(objectTypes, "OBSTACLE"));
    }
}
