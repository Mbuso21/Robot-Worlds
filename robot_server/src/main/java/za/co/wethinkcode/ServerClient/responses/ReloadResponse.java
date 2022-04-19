package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;
import za.co.wethinkcode.ServerClient.world.IWorld;

import java.io.PrintStream;

public class ReloadResponse extends Response{

    protected ReloadResponse(String clientName) {
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
        target.setStatus(IWorld.Status.RELOAD);
        response.put("result", "OK");
        long secondsToSleep = 3;
        try {
            Thread.sleep(secondsToSleep * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int shots = Integer.parseInt(target.getShots());
        if (shots != target.getMaxShots()){
            target.setShots(String.valueOf(target.getMaxShots()));
        }
        messageObj.put("message", "Done");
        response.put("state", target.getState());
        target.setStatus(IWorld.Status.TODO);

        return response;
    }
}
