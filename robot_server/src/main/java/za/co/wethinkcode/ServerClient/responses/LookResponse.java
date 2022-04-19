package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;
import za.co.wethinkcode.ServerClient.world.WorldObject;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class LookResponse extends Response{

    public LookResponse(String clientName) {
        super(clientName);
    }


    @Override
    public boolean process(AbstractWorld world, PrintStream out) {
        JSONObject response = composeResponse(world);
        out.println(response);
        return true;
    }

    @Override
    protected JSONObject composeResponse(AbstractWorld world) {
        JSONObject response = new JSONObject();
        JSONObject data   = new JSONObject();
        JSONArray objects = new JSONArray();

        Robot target = world.getPlayers().get(getClientName());
        List<WorldObject> visibleObjects = target.getObjectsInRange(world.getWorldObjects());
        for ( WorldObject object: visibleObjects ) {
            objects.add(object.objectData(target));
        }

        data.put("objects", objects);
        JSONObject state = target.getState();
        response.put("result", "OK");
        response.put("data", data);
        response.put("state", state);

        return response;
    }
}
