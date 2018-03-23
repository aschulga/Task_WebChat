package alexshulga.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;

@ClientEndpoint
public class ReadFromServer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int REGISTER = 1;
    private static final int LEAVE = 2;
    private static final int EXIT = 3;
    private static final int MESSAGE = 4;
    private static final int MESSAGE_ABOUT_CONNECT = 5;

    @OnOpen
    public void onOpen(){
        LOGGER.log(Level.INFO, " --- You connected with server.");
    }

    @OnMessage
    public void onMessage(String msg){
        String[] strings = msg.split("\\|");

        switch (Integer.parseInt(strings[0])) {
            case REGISTER: {
                LOGGER.log(Level.INFO, " --- "+strings[1] + " " + strings[2]+" registred");
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
                break;
            }
            case 6:{
                LOGGER.log(Level.INFO, " --- Please wait agent");
                break;
            }
        }
    }
}
