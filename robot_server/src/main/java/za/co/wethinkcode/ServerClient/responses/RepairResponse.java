package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;
import za.co.wethinkcode.ServerClient.world.IWorld;

import java.io.PrintStream;

public class RepairResponse extends Response {

    protected RepairResponse(String clientName) {
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
        IWorld.Message message;
        target.setStatus(IWorld.Status.REPAIR);
        response.put("result", "OK");
//        messageObj.put("message",message);state
        long secondsToSleep = 3;
        try {
            Thread.sleep(secondsToSleep * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int shield = Integer.parseInt(target.getShields());
        if (shield != target.getMaxShields()){
            target.setShields(target.getShields());
        }
        messageObj.put("message", "Done");
        response.put("state", target.getState());
        target.setStatus(IWorld.Status.TODO);

        return response;
    }
}
