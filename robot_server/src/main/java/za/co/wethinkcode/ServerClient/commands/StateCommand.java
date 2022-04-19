package za.co.wethinkcode.ServerClient.commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.client.Client;
import za.co.wethinkcode.ServerClient.robots.Robot;

import java.io.PrintStream;

import static za.co.wethinkcode.ServerClient.Request.composeRequest;

public class StateCommand extends Command{

    public StateCommand(){
        super("state");
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
