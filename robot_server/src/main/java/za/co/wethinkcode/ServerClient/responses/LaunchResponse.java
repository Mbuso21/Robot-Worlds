package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;

import java.io.PrintStream;

public class LaunchResponse extends Response{
    private final String clientMachine;
    private final String robotName;

    public LaunchResponse(String robotName, JSONArray arguments, String clientMachine) {
        super("launch" , robotName, arguments);
        this.clientMachine = clientMachine;
        this.robotName = robotName;
    }


    @Override
    public boolean process(AbstractWorld world, PrintStream out) {
        // Checks if the same name robot has already been launched
        if(world.getPlayers().toString().contains(robotName)) {
            System.out.println("Too many of you in this world");
            Response error = new ErrorResponse("Too many of you in this world");
            error.process(world, out);
            return false;
        }
        else if(!world.enoughSpaceInWorld()) {
            System.out.println("No more space in the world");
            Response error = new ErrorResponse("No more space in this world");
            error.process(world, out);
            return false;
        }
        else {
            Robot robot = new Robot(getClientName(), world.getWorldAttributes() );
            world.addNewConnection(clientMachine, robot);
            JSONObject clientResponse = composeResponse(world);
            System.out.println("LaunchResponse: " + clientResponse.toJSONString());
            out.println(clientResponse.toJSONString());
        }
        return true;
    }

    @Override
    protected JSONObject composeResponse(AbstractWorld world) {
        JSONObject response = new JSONObject();
        JSONObject data = world.getData(world.getPlayers().get(getClientName()));
        JSONObject state = world.getPlayers().get(getClientName()).getState();
        response.put("result", "OK");
        response.put("data", data);
        response.put("state", state);

        return response;
    }
}
