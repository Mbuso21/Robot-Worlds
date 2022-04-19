package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONObject;

import za.co.wethinkcode.ServerClient.server.MultiServers;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;

import java.io.PrintStream;

public class RestoreResponse extends Response {
    private final String worldName;

    public RestoreResponse(String worldName) {
        super("restore");
        this.worldName = worldName;
    }

    @Override
    public boolean process(AbstractWorld world, PrintStream out) {
        String message = MultiServers.restoreWorld(worldName);
        if (!message.equalsIgnoreCase("SUCCESS")) {
            Response error = new ErrorResponse(message); //stack trace
            error.process(world, out);
            return false;
        }

        JSONObject clientResponse = composeResponse(world);
        System.out.println("RestoreResponse: " + clientResponse.toJSONString());
        out.println(clientResponse.toJSONString());
        return true;
    }

    @Override
    protected JSONObject composeResponse(AbstractWorld world) {
        JSONObject response = new JSONObject();
        JSONObject dataObj = new JSONObject();

        // code to restore world from the database must be invoked here
        // also code to set the restored world attributes (size, objects etc.)
        dataObj.put("attributes", new org.json.JSONObject(world.getWorldAttributes()));
        dataObj.put("pits", world.getPits().size());
        dataObj.put("obstacles", world.getObstacles().size());

        response.put("result", "OK");
        response.put("data", dataObj);

        return response;
    }
}
