package alexshulga.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint
public class ReadFromServer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int REGISTRATION = 1;
    private static final int LEAVE = 2;
    private static final int EXIT = 3;
    private static final int MESSAGE = 4;
    private static final int MESSAGE_ABOUT_CONNECT = 5;
    private static final String TAB_NAME = "console";

    @OnOpen
    public void onOpen(Session session){
        if(session.isOpen())
            LOGGER.log(Level.INFO, " --- You connected with server.");
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws IOException {
        String[] strings = msg.split("\\|");

        switch (Integer.parseInt(strings[0])) {
            case REGISTRATION: {
                LOGGER.log(Level.INFO, " --- "+strings[1] + " " + strings[2]+" registered");
                break;
            }
            case LEAVE: {
                LOGGER.log(Level.INFO, " --- "+strings[1] + " " + strings[2]+" leaved");
                break;
            }
            case EXIT: {
                LOGGER.log(Level.INFO, " --- "+strings[1] + " " + strings[2]+" exit");
                break;
            }
            case MESSAGE: {
                LOGGER.log(Level.INFO, " --- [" + strings[1] + " " + strings[2] + "] : " + strings[3]);
                break;
            }
            case MESSAGE_ABOUT_CONNECT:{
                LOGGER.log(Level.INFO, " --- You connected with " + strings[1] + " " + strings[2]);

                if("client".equals(strings[1]))
                {
                    JSONObject resultJson = new JSONObject();
                    resultJson.put("code", "5");
                    resultJson.put("tabId", TAB_NAME);
                    session.getBasicRemote().sendText(String.valueOf(resultJson));
                }

                break;
            }
        }
    }
}
