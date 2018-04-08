package alexshulga.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.websocket.Session;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class WriteToServer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String REGISTRATION = "1";
    private static final String TAB_NAME = "console";

    private Scanner reader;
    private String line;
    private JSONObject resultJson;

    public void write(Session session) throws IOException {

        reader = new Scanner(new InputStreamReader(System.in));
        resultJson = new JSONObject();

        while (reader.hasNextLine()) {
            line = reader.nextLine();

            if (!"".equals(line)) {
                String[] strings = line.split(" ");
                Validator validator = new Validator();
                if (!validator.isValidateRequest(line)) {
                    LOGGER.log(Level.INFO, " - Invalid command");
                } else {

                    resultJson.put("code", REGISTRATION);
                    resultJson.put("status", strings[1]);
                    resultJson.put("username", strings[2]);

                    if ("agent".equals(strings[1])) {
                        resultJson.put("numberClient", 1);
                        resultJson.put("tabId", TAB_NAME);

                        session.getBasicRemote().sendText(String.valueOf(resultJson));

                        AgentHandler agentHandler = new AgentHandler();
                        agentHandler.agentWrite(session);
                    } else {
                        session.getBasicRemote().sendText(String.valueOf(resultJson));

                        ClientHandler clientHandler = new ClientHandler();
                        clientHandler.clientWrite(session);
                    }
                    break;
                }
            }
        }
    }
}


