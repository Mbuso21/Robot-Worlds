package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;
import za.co.wethinkcode.ServerClient.world.IWorld;

import java.io.PrintStream;

public class MineResponse extends Response{

    protected MineResponse(String clientName) {
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
        long secondsToSleep = 2;
        try {
            Thread.sleep(secondsToSleep * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        world.setMine(target);
        target.setStatus(IWorld.Status.SETMINE);
        response.put("result", "OK");
        messageObj.put("message", translateMessage(IWorld.Message.DONE));
        response.put("data", messageObj);
        response.put("state", target.getState());
        target.setStatus(IWorld.Status.TODO);

        return response;
    }






















}
