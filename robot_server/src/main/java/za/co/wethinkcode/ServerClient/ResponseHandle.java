package za.co.wethinkcode.ServerClient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class ResponseHandle implements Runnable {
    private BufferedReader inputStream;

    public ResponseHandle(BufferedReader socketInputStream) {
        this.inputStream = socketInputStream;
    }

    @Override
    public void run() {
        String response = "";
        while (true) {
            try {
                response = inputStream.readLine();
                if (response != null) {
                    try {
                        JSONParser parser = new JSONParser();
                        JSONObject newResponse = (JSONObject) parser.parse(response);
                        printResponse(newResponse);
                    } catch (ParseException e) {
                        System.out.println(e);
                    }
                    System.out.println("What would you like to do next");
                }
                else {
                    System.out.println("No response");
                }
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    public void printResponse(JSONObject response){
        JSONObject state = new JSONObject();
        JSONObject data  = (JSONObject) response.get("data");
        try {
            state = (JSONObject) response.get("state");
        } catch (ClassCastException i) {
        }

        for (Object keyStr : response.keySet()) {
            Object keyValue = response.get(keyStr);
            if (keyStr.toString().equals("data")) {
                System.out.println("data: {");
                for (Object newkeyStr : data.keySet()) {
                    Object newkeyValue = data.get(newkeyStr);
                    System.out.println("    " + newkeyStr + " : " + newkeyValue);
                }
                System.out.println("}");

            } else if (keyStr.toString().equals("state")) {
                System.out.println("state: {");
                for (Object newkeyStr : state.keySet()) {
                    Object newkeyValue = state.get(newkeyStr);
                    System.out.println("    " + newkeyStr + " : " + newkeyValue);
                }
                System.out.println("}");
            }else {
                System.out.println(keyStr + " : " + keyValue);
            }
        }
    }

}

