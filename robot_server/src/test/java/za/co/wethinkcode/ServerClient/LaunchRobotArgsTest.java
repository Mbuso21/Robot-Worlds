package za.co.wethinkcode.ServerClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import za.co.wethinkcode.ServerClient.client.RobotWorldClient;
import za.co.wethinkcode.ServerClient.client.RobotWorldJsonClient;
import za.co.wethinkcode.ServerClient.server.MultiServers;

import java.io.IOException;

/**
 * LaunchRobotArgsTest class is a LaunchRobotTest with tests that have special args
 * that are needed for them pass (i.e. 2x2 world and obstacles)
 * @see LaunchRobotTest
 */
public class LaunchRobotArgsTest {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private final String serverJarPath = System.getProperty("user.dir") + "/target/uss-newton.jar";
    private Process process;

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
     * Each test is a scenario , it tells a user story which we test
     */

    /**
     * Launch Test Scenario: Can launch another robot in a 2x2 world
     */
    @Test
    void canLaunchAnotherRobot() {
        // Given that I am connected to a running Robot Worlds server
        runServerWithArgs("-s 2");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        Assertions.assertTrue(serverClient.isConnected());

        // Given a world of size 2x2
        // and robot "HAL" has already been launched into the world
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        // When I launch robot "R2D2" into the world
        String request2 = "{" +
                "  \"robot\": \"R2D2\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response2 = serverClient.sendRequest(request2);

        // Then the launch should be successful
        Assertions.assertNotNull(response2.get("result"));
        Assertions.assertEquals("OK", response2.get("result").asText());

        // and a randomly allocated position of R2D2 should be returned.
        // but R2D2's position shouldn't be the same as HAL's
        Assertions.assertTrue(
                response2.get("data").get("position").get(0) != response.get("data").get("position").get(0) ||
                        response2.get("data").get("position").get(1) != response.get("data").get("position").get(0)
        );
    }

    /**
     * Launch Test Scenario: World without obstacles is full in a 2x2 world
     */
    @Test
    void worldWithoutObstaclesIsFull() {
        // Given a world of size 2x2
        // ,and I have successfully launched 9 robots into the world
        runServerWithArgs("-s 2");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        Assertions.assertTrue(serverClient.isConnected());

        //Robot 1
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        //Robot 2
        String request2 = "{" +
                "  \"robot\": \"R2-D2\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response2 = serverClient.sendRequest(request2);

        Assertions.assertNotNull(response2.get("result"));
        Assertions.assertEquals("OK", response2.get("result").asText());

        //Robot 3
        String request3 = "{" +
                "  \"robot\": \"BB-8\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response3 = serverClient.sendRequest(request3);

        Assertions.assertNotNull(response3.get("result"));
        Assertions.assertEquals("OK", response3.get("result").asText());

        //Robot 4
        String request4 = "{" +
                "  \"robot\": \"IG-11\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response4 = serverClient.sendRequest(request4);

        Assertions.assertNotNull(response4.get("result"));
        Assertions.assertEquals("OK", response4.get("result").asText());

        //Robot 5
        String request5 = "{" +
                "  \"robot\": \"R5-D4\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response5 = serverClient.sendRequest(request5);

        Assertions.assertNotNull(response5.get("result"));
        Assertions.assertEquals("OK", response5.get("result").asText());

        //Robot 6
        String request6 = "{" +
                "  \"robot\": \"AP-5\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response6 = serverClient.sendRequest(request6);

        Assertions.assertNotNull(response6.get("result"));
        Assertions.assertEquals("OK", response6.get("result").asText());

        //Robot 7
        String request7 = "{" +
                "  \"robot\": \"C1-10P\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response7 = serverClient.sendRequest(request7);

        Assertions.assertNotNull(response7.get("result"));
        Assertions.assertEquals("OK", response7.get("result").asText());

        //Robot 8
        String request8 = "{" +
                "  \"robot\": \"Mouse\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response8 = serverClient.sendRequest(request8);

        Assertions.assertNotNull(response8.get("result"));
        Assertions.assertEquals("OK", response8.get("result").asText());

        //Robot 9
        String request9 = "{" +
                "  \"robot\": \"bones\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response9 = serverClient.sendRequest(request9);

        Assertions.assertNotNull(response9.get("result"));
        Assertions.assertEquals("OK", response9.get("result").asText());

        // When I launch one more robot
        String request10 = "{" +
                "  \"robot\": \"K-2SO\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response10 = serverClient.sendRequest(request10);

        // Then I should get an error response back with the message "No more space in this world"
        Assertions.assertNotNull(response10.get("result"));
        Assertions.assertEquals("ERROR", response10.get("result").asText());

        Assertions.assertNotNull(response10.get("data"));
        Assertions.assertNotNull(response10.get("data").get("message"));
        Assertions.assertTrue(response10.get("data").get("message").asText().contains("No more space in this world"));
    }

    /**
     * Launch Test Scenario: Launch robots into a world with an obstacle
     */
    @Test
    void launchRobotsIntoWorldWithObstacle() {
        // Given a world of size 2x2
        // and the world has an obstacle at coordinate [1,1]
        // When I launch 8 robots into the world
        runServerWithArgs("-s 2 -o 1,1");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        Assertions.assertTrue(serverClient.isConnected());

        //Robot 1
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        //Robot 2
        String request2 = "{" +
                "  \"robot\": \"R2-D2\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response2 = serverClient.sendRequest(request2);

        Assertions.assertNotNull(response2.get("result"));
        Assertions.assertEquals("OK", response2.get("result").asText());

        //Robot 3
        String request3 = "{" +
                "  \"robot\": \"BB-8\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response3 = serverClient.sendRequest(request3);

        Assertions.assertNotNull(response3.get("result"));
        Assertions.assertEquals("OK", response3.get("result").asText());

        //Robot 4
        String request4 = "{" +
                "  \"robot\": \"IG-11\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response4 = serverClient.sendRequest(request4);

        Assertions.assertNotNull(response4.get("result"));
        Assertions.assertEquals("OK", response4.get("result").asText());

        //Robot 5
        String request5 = "{" +
                "  \"robot\": \"R5-D4\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response5 = serverClient.sendRequest(request5);

        Assertions.assertNotNull(response5.get("result"));
        Assertions.assertEquals("OK", response5.get("result").asText());

        //Robot 6
        String request6 = "{" +
                "  \"robot\": \"AP-5\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response6 = serverClient.sendRequest(request6);

        Assertions.assertNotNull(response6.get("result"));
        Assertions.assertEquals("OK", response6.get("result").asText());

        //Robot 7
        String request7 = "{" +
                "  \"robot\": \"C1-10P\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response7 = serverClient.sendRequest(request7);

        Assertions.assertNotNull(response7.get("result"));
        Assertions.assertEquals("OK", response7.get("result").asText());

        //Robot 8
        String request8 = "{" +
                "  \"robot\": \"Mouse\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response8 = serverClient.sendRequest(request8);

        Assertions.assertNotNull(response8.get("result"));
        Assertions.assertEquals("OK", response8.get("result").asText());

        // Then each robot cannot be in position [1,1].
        int[] object = {1,1};
        int[] robot1 = {response.get("data").get("position").get(0).asInt(), response.get("data").get("position").get(1).asInt()};
        int[] robot2 = {response2.get("data").get("position").get(0).asInt(), response2.get("data").get("position").get(1).asInt()};
        int[] robot3 = {response3.get("data").get("position").get(0).asInt(), response3.get("data").get("position").get(1).asInt()};
        int[] robot4 = {response4.get("data").get("position").get(0).asInt(), response4.get("data").get("position").get(1).asInt()};
        int[] robot5 = {response5.get("data").get("position").get(0).asInt(), response5.get("data").get("position").get(1).asInt()};
        int[] robot6 = {response6.get("data").get("position").get(0).asInt(), response6.get("data").get("position").get(1).asInt()};
        int[] robot7= {response7.get("data").get("position").get(0).asInt(), response7.get("data").get("position").get(1).asInt()};
        int[] robot8= {response8.get("data").get("position").get(0).asInt(), response8.get("data").get("position").get(1).asInt()};
        Assertions.assertNotSame(object, robot1);
        Assertions.assertNotSame(object, robot2);
        Assertions.assertNotSame(object, robot3);
        Assertions.assertNotSame(object, robot4);
        Assertions.assertNotSame(object, robot5);
        Assertions.assertNotSame(object, robot6);
        Assertions.assertNotSame(object, robot7);
        Assertions.assertNotSame(object, robot8);
    }

    /**
     * Launch Test Scenario: World with an obstacle is full
     */
    @Test
    void worldWithObstacleFull() {
        // Given a world of size 2x2
        // and the world has an obstacle at coordinate [1,1]
        // and I have successfully launched 8 robots into the world
        runServerWithArgs("-s 2 -o 1,1");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        Assertions.assertTrue(serverClient.isConnected());

        //Robot 1
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        Assertions.assertNotNull(response.get("result"));
        Assertions.assertEquals("OK", response.get("result").asText());

        //Robot 2
        String request2 = "{" +
                "  \"robot\": \"R2-D2\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response2 = serverClient.sendRequest(request2);

        Assertions.assertNotNull(response2.get("result"));
        Assertions.assertEquals("OK", response2.get("result").asText());

        //Robot 3
        String request3 = "{" +
                "  \"robot\": \"BB-8\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response3 = serverClient.sendRequest(request3);

        Assertions.assertNotNull(response3.get("result"));
        Assertions.assertEquals("OK", response3.get("result").asText());

        //Robot 4
        String request4 = "{" +
                "  \"robot\": \"IG-11\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response4 = serverClient.sendRequest(request4);

        Assertions.assertNotNull(response4.get("result"));
        Assertions.assertEquals("OK", response4.get("result").asText());

        //Robot 5
        String request5 = "{" +
                "  \"robot\": \"R5-D4\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response5 = serverClient.sendRequest(request5);

        Assertions.assertNotNull(response5.get("result"));
        Assertions.assertEquals("OK", response5.get("result").asText());

        //Robot 6
        String request6 = "{" +
                "  \"robot\": \"AP-5\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response6 = serverClient.sendRequest(request6);

        Assertions.assertNotNull(response6.get("result"));
        Assertions.assertEquals("OK", response6.get("result").asText());

        //Robot 7
        String request7 = "{" +
                "  \"robot\": \"C1-10P\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response7 = serverClient.sendRequest(request7);

        Assertions.assertNotNull(response7.get("result"));
        Assertions.assertEquals("OK", response7.get("result").asText());

        //Robot 8
        String request8 = "{" +
                "  \"robot\": \"Mouse\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response8 = serverClient.sendRequest(request8);

        Assertions.assertNotNull(response8.get("result"));
        Assertions.assertEquals("OK", response8.get("result").asText());

        // When I launch one more robot
        String request9 = "{" +
                "  \"robot\": \"IG-12\"," +
                "  \"command\": \"launch\" " +
                "}";

        JsonNode response9 = serverClient.sendRequest(request9);

        // Then I should get an error response back with the message "No more space in this world"
        Assertions.assertNotNull(response9.get("result"));
        Assertions.assertEquals("ERROR", response9.get("result").asText());
        Assertions.assertNotNull(response9.get("data"));
        Assertions.assertNotNull(response9.get("data").get("message"));
        Assertions.assertTrue(response9.get("data").get("message").asText().contains("No more space in this world"));
    }
}
