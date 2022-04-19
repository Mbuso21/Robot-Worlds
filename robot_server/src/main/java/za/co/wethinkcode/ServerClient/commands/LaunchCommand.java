package za.co.wethinkcode.ServerClient.commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.ServerClient.robots.Robot;
import za.co.wethinkcode.ServerClient.robots.SniperRobot;
import za.co.wethinkcode.ServerClient.client.Client;

import java.io.PrintStream;

import static za.co.wethinkcode.ServerClient.Request.composeRequest;

public class LaunchCommand extends Command {
    private final String name;
    private final String make;

    public LaunchCommand(String make, String name) {
        super("launch");
        this.name = name;
        this.make = make;
    }

    @Override
    public boolean execute(PrintStream out, Client client) {
        client.setRobot(new SniperRobot(this.name));

        System.out.println(client.getRobot().getState());
        String[] args = getArgument() == null ? new String[]{"no", "Args"} : getArgument().trim().split(" ");
        Robot robot = Robot.make(args[0], args[1]);
        client.setRobot(robot);
        Robot target = client.getRobot();
        String arguments = target.getAttributes();
        JSONObject requestObject = composeRequest(target.getName(), getName(), target.getAttributes());

        System.out.println(requestObject);
        return true;
    }
}
