package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;

import java.io.PrintStream;

public class FireResponse extends Response{

    protected FireResponse(String clientName) {
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
        Robot target   = world.getPlayers().get(getClientName());
        String message = world.isPathBlocked(target);
        int remainingShots = Integer.parseInt(target.getShots());
        target.setShots(String.valueOf(remainingShots - 1));
        String result;
        if ("Hit".equals(message)) {
            result = message;
        } else {
            result = "Miss";
        }

        response.put("result", "OK");
        messageObj.put("message", result);
        response.put("data" , messageObj);
        response.put("state", target.getState());

        return response;
    }
}
