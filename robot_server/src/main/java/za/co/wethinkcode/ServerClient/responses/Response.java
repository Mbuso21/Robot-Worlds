package za.co.wethinkcode.ServerClient.responses;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;
import za.co.wethinkcode.ServerClient.world.IWorld;

import java.io.PrintStream;

public abstract class Response {
    private final String commandName;
    private final String nameCheckMe;
    private final String clientName;
    private final JSONArray argument;
    private final String message;

    protected Response(String name, String clientName, JSONArray argument) {
        this.commandName = name;
        this.clientName = clientName;
        this.nameCheckMe = clientName;
        this.argument = argument;
        this.message = null;
    }

    protected  Response(String name) {
        this.commandName = null;
        this.clientName = name;
        this.nameCheckMe = clientName;
        this.argument = null;
        this.message = null;
    }

    protected Response(String name, String message) {
        this.commandName = name;
        this.clientName = null;
        this.nameCheckMe = clientName;
        this.argument = null;
        this.message = message;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public String getClientName() {
        return this.clientName;
    }

    public JSONArray getArgument() {
        return this.argument;
    }

    public String getMessage() {
        return  this.message;
    }

    public static Response handle(String instruction, String clientMachine, AbstractWorld world) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonRequest = (JSONObject) parser.parse(instruction);
            JSONArray arguments = (JSONArray) jsonRequest.get("arguments");

            String robotName = "";
            if (!jsonRequest.get("command").toString().equalsIgnoreCase("save") &&
                    !jsonRequest.get("command").toString().equalsIgnoreCase("restore")) {
                robotName = jsonRequest.get("robot").toString();
            }

            // If robot isn't Launched, it should give an error message
            if(!jsonRequest.get("command").toString().contains("launch") && !notValidCommand(jsonRequest)) {
                if (!world.checksIfRobotIsLaunched(robotName)) {
                    System.out.println("Robot does not exist");
                    return new ErrorResponse("Robot does not exist");
                }
            }

            switch (jsonRequest.get("command").toString()) {
                case "launch":
                    return new LaunchResponse(robotName, arguments, clientMachine);
                case "forward":
                    return new ForwardResponse(robotName, arguments);
                case "back":
                    return new BackResponse(robotName, arguments);
                case "turn":
                    switch ((String) arguments.get(0)) {
                        case "left":
                            return new LeftResponse(robotName);
                        case "right":
                            return new RightResponse(robotName);
                    }
                case "orientation":
                    return new OrientationResponse(robotName);
                case "mine":
                    return new MineResponse(robotName);
                case "repair":
                    return new RepairResponse(robotName);
                case "reload":
                    return new ReloadResponse(robotName);
                case "look":
                    return new LookResponse(robotName);
                case "fire":
                    return new FireResponse(robotName);
                case "state":
                    return new StateResponse(robotName, arguments, clientMachine);
                case "save":
                    return new SaveResponse((String) arguments.get(0));
                case "restore":
                    return new RestoreResponse((String) arguments.get(0));
                default:
                    return new ErrorResponse("Unsupported command");
            }
        } catch (ParseException e) {
            return new ErrorResponse("Could not parse arguments");
        }
    }

    public abstract boolean process(AbstractWorld world, PrintStream out);

    protected abstract JSONObject composeResponse(AbstractWorld world);

    public String translateMessage(IWorld.Message message) {
        switch (message) {
            case DONE : return "DONE";
            case FELL : return "FELL";
            case MINE : return "MINE";
            default : return "OBSTRUCTED";
        }
    }

    public String translateDirection(IWorld.Direction direction) {
         switch (direction) {
            case NORTH : return "NORTH";
            case SOUTH : return "SOUTH";
            case EAST : return "EAST";
            default : return "WEST";
        }
    }

    /**
     * We created this command to make sure that the state command can work
     * Please look at the handle command in Response Abstract class
     * @param jsonRequest the request object
     * @return true if command is not valid
     */
    public static boolean notValidCommand(JSONObject jsonRequest) {

        switch (jsonRequest.get("command").toString()) {
            case "launch":
            case "forward":
            case "back":
            case "turn":
            case "orientation":
            case "mine":
            case "repair":
            case "reload":
            case "look":
            case "fire":
            case "state":
            case "save":
            case "restore":
                return false;
            default:
                return true;
        }
    }
}
