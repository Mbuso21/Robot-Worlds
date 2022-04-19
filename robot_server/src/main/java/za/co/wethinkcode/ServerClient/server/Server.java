package za.co.wethinkcode.ServerClient.server;

import za.co.wethinkcode.ServerClient.responses.Response;
import za.co.wethinkcode.ServerClient.world.AbstractWorld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.net.Socket;

/**
 * Server class that the MultiServer runs which implements the Runnable interface.
 * @see MultiServers
 * @see Runnable
 */
public class Server implements Runnable {
    private BufferedReader in;
    private PrintStream out;
    private AbstractWorld world;
    private final String clientMachine;

    /**
     * Server class constructor that has two parameters.
     * @param socket The endpoint communication for the client and server.
     * @param world The server world which the client connects to.
     * @see AbstractWorld
     * @see Socket
     */
    public Server(Socket socket, AbstractWorld world) {

        String var10001 = socket.getInetAddress().getHostName();
        this.clientMachine = var10001 + ":" + socket.getPort();

        System.out.println(" > Connection from " + this.clientMachine);

        try {
            SequenceInputStream sequenceInputStream = new SequenceInputStream(socket.getInputStream(), System.in);
            in = new BufferedReader(new InputStreamReader(sequenceInputStream));
            out = new PrintStream(socket.getOutputStream());

            this.world = world;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean shouldContinue = true;
        String request;
        do {
            try {
                request = in.readLine();

                System.out.println(request);
                    try {
                        Response response = Response.handle(request, clientMachine, world);
                        shouldContinue = response.process(world, out);
                    }
                    // Since there is no request because request == null
                    // The program will change shouldContinue = false
                    // So then the program can close the client connections
                    catch (NullPointerException e) {
                        shouldContinue = false;
                        world.clearPlayerConnections();
                    }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } while (shouldContinue);
        closeQuietly();
    }

    /**
     * Closes the BufferedReader and PrintStream used by the socket to
     * communicate the socket's InputStream and OutputStream.
     * @see Socket
     * @see PrintStream
     * @see BufferedReader
     */
    public void closeQuietly() {
        try {
            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        world.clearPlayerConnections();
    }
}
