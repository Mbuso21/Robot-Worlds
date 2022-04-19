package za.co.wethinkcode.ServerClient;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;

/**
 * Request class
 */
public class Request {

    /**
     * Takes 3 parameters and composes a json request.
     * @param robotName the robot name
     * @param commandName the command name the robot must adhere to
     * @param arguments the arguments of the command
     * @return
     */
    public static JSONObject composeRequest(String robotName, String commandName, String arguments) {
        JSONObject jo = new JSONObject();
        jo.put("robot", robotName);
        jo.put("command", commandName);
        jo.put("argument", composeArgs(arguments));
        return jo;
    }

    /**
     * Takes a String parameter containing arguments and converts it to a JSONArray.
     * @param arguments arguments in a single string
     * @return arguments in json format
     */
    public static JSONArray composeArgs(String arguments) {

        JSONArray argumentsArray = new JSONArray();
        String[] args = arguments.trim().split(" ");

        argumentsArray.addAll(Arrays.asList(args));
        return argumentsArray;
    }
}