package za.co.wethinkcode.ServerClient.commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.client.Client;

import java.io.PrintStream;

import static za.co.wethinkcode.ServerClient.Request.composeRequest;

public class DumpCommand extends Command{
    public DumpCommand() {
        super("dump");
    }

    @Override
    public boolean execute(PrintStream out, Client client) {
        Robot target = client.getRobot();
        String name =  target.getName();
        String argument = getArgument() == null ? "no arg" : getArgument();
        JSONObject requestObject = composeRequest(name, getName(), argument);
        System.out.println(requestObject);
        return true;
    }
}
