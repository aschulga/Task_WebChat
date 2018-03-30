package alexshulga.controller;

import org.json.JSONObject;

import javax.websocket.Session;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class AgentHandler {

    private static final String EXIT = "3";
    private static final String MESSAGE = "4";
    private static final String COMMAND_EXIT = "/exit";
    private static final String STATUS = "agent";
    private static final String TAB_NAME = "console";

    private Scanner reader;
    private String line;

    public void agentWrite(Session session) throws IOException {

        reader = new Scanner(new InputStreamReader(System.in));
        while (reader.hasNextLine()) {
            line = reader.nextLine();
            if (!"".equals(line)) {
                String[] strings = line.split(" ");
                JSONObject resultJson = new JSONObject();

                if (COMMAND_EXIT.equals(strings[0])) {
                    resultJson.put("code", EXIT);
                    resultJson.put("tabId", TAB_NAME);
                    session.getBasicRemote().sendText(String.valueOf(resultJson));
                    break;
                } else {
                    resultJson.put("code", MESSAGE);
                    resultJson.put("status", STATUS);
                    resultJson.put("message", line);
                    resultJson.put("tabId", TAB_NAME);
                    session.getBasicRemote().sendText(String.valueOf(resultJson));
                }
            }
        }
    }
}
