package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;
import za.co.wethinkcode.ServerClient.world.Position;
import za.co.wethinkcode.ServerClient.world.RealWorld;

import java.io.PrintStream;
import java.util.Arrays;

public class ForwardResponse extends Response {

    protected ForwardResponse(String clientName, JSONArray argument) {
        super("forward", clientName, argument);
    }

    @Override
    public boolean process(AbstractWorld world, PrintStream out) {
        JSONObject clientResponse = composeResponse(world);
        System.out.println("ForwardResponse: " + clientResponse.toJSONString());
        out.println(clientResponse.toJSONString());
        return true;
    }

    @Override
    protected JSONObject composeResponse(AbstractWorld world) {
        JSONObject response = new JSONObject();
        JSONObject dataObj = new JSONObject();

        int nrSteps = Integer.parseInt((String) getArgument().get(0));
        Robot target = world.getPlayers().get(getClientName());
        String message = world.updatePosition(nrSteps, target);

        dataObj.put("message", message);
        dataObj.put("position", world.convertPosToInt(target));

        response.put("result", "OK");
        response.put("data", dataObj);
        response.put("state", target.getState());

        return response;
    }
}
