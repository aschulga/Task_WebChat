package alexshulga.controller;

import alexshulga.model.Base;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/user")
public class UserHandler {

    private static final String REGISTRATION = "1";
    private static final String LEAVE = "2";
    private static final String EXIT = "3";
    private static final String MESSAGE = "4";
    private static final String MESSAGE_ABOUT_CONNECT = "5";

    private Base base = Base.getInstance();
    private Controller controller = new Controller();
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject jsonObject = new JSONObject(message);

        System.out.println("1");
        System.out.println("client "+base.getListClient().size());
        System.out.println("agent "+base.getListAgent().size());
        System.out.println("mapParameters "+base.getMapParameters().size());
        System.out.println("mapClient "+base.getMapClient().size());
        System.out.println("mapAgent "+base.getMapAgent().size());
        System.out.println("mapMessages "+base.getMapMessages().size());

        if(REGISTRATION.equals(jsonObject.get("code"))) {
           controller.registration(session, jsonObject);
        }
        else if(LEAVE.equals(jsonObject.get("code"))){
            controller.leave(session);
        }
        else if(EXIT.equals(jsonObject.get("code"))){
            if ("agent".equals(base.getMapParameters().get(session).getStatus())) {
                if("console".equals(jsonObject.get("tabId")) || "tabRestAgent".equals(jsonObject.getString("tabId"))){
                    controller.exitAgent(session);
                }
                else {
                    controller.exitTabAgent(session, jsonObject);
                }
            }
            else{
                controller.exitClient(session);
            }
        }
        else if(MESSAGE.equals(jsonObject.get("code"))) {
            if ("client".equals(jsonObject.get("status"))) {
                controller.sendMessageToAgent(session, jsonObject);
            }
            else{
                controller.sendMessageToClient(session, jsonObject);
            }
        }
        else if(MESSAGE_ABOUT_CONNECT.equals(jsonObject.get("code"))){
            controller.createPair(jsonObject);
        }

        System.out.println("2");
        System.out.println("client "+base.getListClient().size());
        System.out.println("agent "+base.getListAgent().size());
        System.out.println("mapParameters "+base.getMapParameters().size());
        System.out.println("mapClient "+base.getMapClient().size());
        System.out.println("mapAgent "+base.getMapAgent().size());
        System.out.println("mapMessages "+base.getMapMessages().size());
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        if (base.getMapParameters().containsKey(session)) {
            if ("agent".equals(base.getMapParameters().get(session).getStatus())) {
                controller.exitAgent(session);
            } else {
                controller.exitClient(session);
            }
        }

        System.out.println("3");
        System.out.println("client "+base.getListClient().size());
        System.out.println("agent "+base.getListAgent().size());
        System.out.println("mapParameters "+base.getMapParameters().size());
        System.out.println("mapClient "+base.getMapClient().size());
        System.out.println("mapAgent "+base.getMapAgent().size());
        System.out.println("mapMessages "+base.getMapMessages().size());
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        LOGGER.error(" - User disconnect. "+thr);
    }
}

