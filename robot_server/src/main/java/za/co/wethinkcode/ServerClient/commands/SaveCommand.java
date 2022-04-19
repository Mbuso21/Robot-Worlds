package za.co.wethinkcode.ServerClient.commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.client.Client;
import za.co.wethinkcode.ServerClient.robots.Robot;

import java.io.PrintStream;

import static za.co.wethinkcode.ServerClient.Request.composeRequest;

public class SaveCommand extends Command {

    public SaveCommand() {
        super("save");
    }

    @Override
    public boolean execute(PrintStream out, Client client) {
        JSONObject requestObject = composeRequest(client.getRobot().getName(), "save", "");
        System.out.println(requestObject);
        return true;
    }
}
