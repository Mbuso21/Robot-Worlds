package za.co.wethinkcode.ServerClient.client;


import za.co.wethinkcode.ServerClient.commands.Command;
import za.co.wethinkcode.ServerClient.robots.Robot;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintStream output;
    private Robot robot;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            output = new PrintStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e);
            closeQuietly();
        }

        launchRobot();
        boolean shouldContinue = true;
        do {
            String instruction = getInput("What would you like to do next?");
            Command command = Command.create(instruction);
//            shouldContinue = command.execute(output, robot);
            shouldContinue = command.execute(output, this);
            System.out.println(shouldContinue);
            output.println(instruction);
            output.flush();
            try {
                String response;
                while ((response = input.readLine()) != null);{

                }
                System.out.println(response);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } while (shouldContinue);

        closeQuietly();
    }

    public static String getInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private void launchRobot() {
        System.out.println("Welcome To Robot Worlds\nUse the command 'launch <make> <name>' to launch the robot into the world");
        String instruction = getInput("-> ");
        Command command = Command.create(instruction);
        boolean launchedSuccessfully = command.execute(output, this);
    }
    public void closeQuietly() {
        System.out.println("Closing connection...");
        try {
            input.close();
            output.close();
            socket.close();
        } catch(IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        try {
            Client client = new Client(args[0], Integer.parseInt(args[1]));
        } catch (IndexOutOfBoundsException ix) {
            System.out.println("Need IP and Port");
        }
    }

    public Robot getRobot() {
        return this.robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
