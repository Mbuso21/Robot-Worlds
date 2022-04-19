package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;

import java.io.PrintStream;

public class StateResponse extends Response{

    public StateResponse(String robotName, JSONArray arguments, String clientMachine) {
        super("state" , robotName, arguments);
    }

    @Override
    public boolean process(AbstractWorld world, PrintStream out) {
        JSONObject response = composeResponse(world);
        out.println(response);
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
