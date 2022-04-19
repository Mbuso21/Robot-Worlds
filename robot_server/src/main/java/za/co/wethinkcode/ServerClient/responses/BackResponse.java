package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;

import java.io.PrintStream;

public class BackResponse extends Response {

    protected BackResponse(String clientName, JSONArray argument) {
        super("back", clientName, argument);
    }

    @Override
    public boolean process(AbstractWorld world, PrintStream out) {

        JSONObject clientResponse = composeResponse(world);
        out.println(clientResponse.toJSONString());
        return true;
    }

    @Override
    protected JSONObject composeResponse(AbstractWorld world) {
        JSONObject response = new JSONObject();
        JSONObject messageObj = new JSONObject();

        int nrSteps = Integer.parseInt((String) getArgument().get(0));
        Robot target = world.getPlayers().get(getClientName());
//        IWorld.Message message = world.updatePosition(-nrSteps, target);
        String message = world.updatePosition(-nrSteps, target);
        response.put("result", "OK");
        messageObj.put("message", message);
        response.put("data", messageObj);
        response.put("state", target.getState());

        return response;
    }
}
