package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;

import java.io.PrintStream;

public class ErrorResponse extends Response {
    protected ErrorResponse(String message) {
        super("ERROR", message);
    }

    @Override
    public boolean process(AbstractWorld world, PrintStream out) {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();

        data.put("message", getMessage());
        response.put("result", getCommandName());
        response.put("data", data);

        out.println(response.toJSONString());
        return true;
    }

    @Override
    protected JSONObject composeResponse(AbstractWorld world) {
        return null;
    }

}
