package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;
import za.co.wethinkcode.ServerClient.world.WorldObject;

import java.io.PrintStream;

public class OrientationResponse extends Response {
    JSONObject response;

    public OrientationResponse(String clientName) {
        super(clientName);
    }

    @Override
    public boolean process(AbstractWorld world, PrintStream out) {

        JSONObject clientResponse = composeResponse(world);
        out.println(clientResponse.toJSONString());
        return true;
    }

    @Override
    protected JSONObject composeResponse(AbstractWorld world) {
        response = new JSONObject();
        JSONObject messageObj = new JSONObject();

        Robot target = world.getPlayers().get(getClientName());
        WorldObject.Direction direction = target.getCurrentDirection();
        response.put("result", "OK");
        messageObj.put("direction", direction);
        response.put("data", direction);
        response.put("state", target.getState());

        return response;
    }

}
