package za.co.wethinkcode.ServerClient.commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.client.Client;

import java.io.PrintStream;

import static za.co.wethinkcode.ServerClient.Request.composeRequest;

public class RestoreCommand extends Command {

    public RestoreCommand() {
        super("save");
    }

    @Override
    public boolean execute(PrintStream out, Client client) {
        JSONObject requestObject = composeRequest("", "save", "");
        System.out.println(requestObject);
        return true;
    }
}
