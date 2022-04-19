package za.co.wethinkcode.ServerClient.commands;

import za.co.wethinkcode.ServerClient.client.Client;

import java.io.PrintStream;

public abstract class Command {
    private final String name;
    private String argument;

    public Command(String name) {
        this.name = name;
    }

    public Command(String name, String argument) {
        this.name = name;
        this.argument = argument;
    }

    public static Command create(String instruction) {
        String[] args = instruction.toLowerCase().trim().split(" ");

        switch (args[0]) {
            case "quit" :
                return new QuitCommand();
            case "purge" :
                return new PurgeCommand(args[1]);
            case "dump" :
                return new DumpCommand();
            case "launch" :
                return new LaunchCommand(args[1], args[2]);
            case "forward" :
                return new ForwardCommand(args[1]);
            case "look" :
                return new LookCommand();
            case "state" :
                return new StateCommand();
            case "save" :
                return new SaveCommand();
            default :
                throw new IllegalArgumentException(String.format("Instruction: '%s' is not supported.", instruction));
        }
    }

    public String getName() {
        return this.name;
    }

    public String getArgument() {
        return this.argument;
    }

    public abstract boolean execute(PrintStream out, Client client);
}
