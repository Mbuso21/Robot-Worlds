package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONObject;

import za.co.wethinkcode.ServerClient.server.MultiServers;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;

import java.io.PrintStream;

public class SaveResponse extends Response {
    private final String worldName;

    public SaveResponse(String worldName) {
        super("save");
        this.worldName = worldName;
    }

    @Override
    public boolean process(AbstractWorld world, PrintStream out) {
        String message = MultiServers.saveWorld(worldName);
        if (!message.equalsIgnoreCase("SUCCESS")) {
            Response error = new ErrorResponse(message);
            error.process(world, out);
            return false;
        }

        JSONObject clientResponse = composeResponse(world);
        System.out.println("SaveResponse: " + clientResponse.toJSONString());
        out.println(clientResponse.toJSONString());
        return true;
    }

    @Override
    protected JSONObject composeResponse(AbstractWorld world) {
        JSONObject response = new JSONObject();
        JSONObject dataObj = new JSONObject();

        dataObj.put("size", world.getSize());
        dataObj.put("mines", world.getMines().size());
        dataObj.put("pits", world.getPits().size());
        dataObj.put("obstacles", world.getObstacles().size());

        response.put("result", "OK");
        response.put("data", dataObj);

        return response;
    }
}
