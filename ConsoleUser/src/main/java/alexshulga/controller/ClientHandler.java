package alexshulga.controller;

import org.json.JSONObject;
import javax.websocket.Session;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ClientHandler {
    private static final String LEAVE = "2";
    private static final String EXIT = "3";
    private static final String MESSAGE = "4";
    private static final String COMMAND_LEAVE = "/leave";
    private static final String COMMAND_EXIT = "/exit";
    private static final String STATUS = "client";

    private Scanner reader;
    private String line;

    public void clientWrite(Session session) throws IOException {
        reader = new Scanner(new InputStreamReader(System.in));
        while (reader.hasNextLine()) {
            line = reader.nextLine();
            if (!"".equals(line)) {
                String[] strings = line.split(" ");
                JSONObject resultJson = new JSONObject();

                if (COMMAND_LEAVE.equals(strings[0])) {
                    resultJson.put("code", LEAVE);
                    session.getBasicRemote().sendText(String.valueOf(resultJson));
                } else if (COMMAND_EXIT.equals(strings[0])) {
                    resultJson.put("code", EXIT);
                    session.getBasicRemote().sendText(String.valueOf(resultJson));
                    break;
                } else {
                    resultJson.put("code", MESSAGE);
                    resultJson.put("status", STATUS);
                    resultJson.put("message", line);
                    session.getBasicRemote().sendText(String.valueOf(resultJson));
                }
            }
        }
    }
}
