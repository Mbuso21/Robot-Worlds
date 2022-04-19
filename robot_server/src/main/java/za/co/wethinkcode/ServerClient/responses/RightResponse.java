package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;

import java.io.PrintStream;

public class RightResponse extends Response {

    protected RightResponse(String clientName) {
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
        JSONObject response = new JSONObject();
        JSONObject messageObj = new JSONObject();

        Robot target = world.getPlayers().get(getClientName());
//        IWorld.Message message = world.updatePosition(0, target);
        String message = world.updatePosition(0, target);
        world.updateDirection(true, target);
        response.put("result", "OK");
        messageObj.put("message", message);
        response.put("data", messageObj);
        response.put("state", target.getState());

        return response;
    }
}
