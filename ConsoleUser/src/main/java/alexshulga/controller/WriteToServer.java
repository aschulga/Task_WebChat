package alexshulga.controller;

import alexshulga.view.Console;
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
    private static final String REGISTER = "1";
    private static final String LEAVE = "2";
    private static final String EXIT = "3";
    private static final String MESSAGE = "4";
    private static final String COMMAND_LEAVE = "/leave";
    private static final String COMMAND_EXIT = "/exit";

    private boolean isRegistration = true;
    private Scanner reader;
    private String line;

    public void write(Session session) throws IOException {

        reader = new Scanner(new InputStreamReader(System.in));

        while (reader.hasNextLine()) {
            line = reader.nextLine();
            String[] strings = line.split(" ");
            JSONObject resultJson = new JSONObject();

            if (isRegistration) {
                Validator validator = new Validator();
                if (!validator.isValidateRequest(line)) {
                    LOGGER.log(Level.INFO, " - Invalid command");
                } else {
                    resultJson.put("code", REGISTER);
                    resultJson.put("status", strings[1]);
                    resultJson.put("username", strings[2]);
                    session.getBasicRemote().sendText(String.valueOf(resultJson));
                    isRegistration = false;
                }
            }
            else if(COMMAND_LEAVE.equals(strings[0])){
                resultJson.put("code", LEAVE);
                session.getBasicRemote().sendText(String.valueOf(resultJson));
            }
            else if(COMMAND_EXIT.equals(strings[0])){
                resultJson.put("code", EXIT);
                session.getBasicRemote().sendText(String.valueOf(resultJson));
                reader.close();
                break;
            }
            else{
                resultJson.put("code", MESSAGE);
                resultJson.put("message", line);
                session.getBasicRemote().sendText(String.valueOf(resultJson));
            }
        }

        Console.exit();
    }
}


