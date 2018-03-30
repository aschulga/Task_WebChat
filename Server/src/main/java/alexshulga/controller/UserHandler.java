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

    private static Base base = new Base();
    private static Controller controller = new Controller(base);
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject jsonObject = new JSONObject(message);

        System.out.println("1");
        System.out.println("client "+controller.getBase().getListClient().size());
        System.out.println("agent "+controller.getBase().getListAgent().size());
        System.out.println("mapParameters "+controller.getBase().getMapParameters().size());
        System.out.println("mapClient "+controller.getBase().getMapClient().size());
        System.out.println("mapAgent "+controller.getBase().getMapAgent().size());
        System.out.println("mapMessages "+controller.getBase().getMapMessages().size());

        if("1".equals(jsonObject.get("code"))) {
           controller.registration(session, jsonObject);
        }
        else if("2".equals(jsonObject.get("code"))){
            controller.leave(session);
        }
        else if("3".equals(jsonObject.get("code"))){
            if ("agent".equals(base.getMapParameters().get(session).getParameter1())) {
                if("console".equals(jsonObject.get("tabId"))){
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
        else if("4".equals(jsonObject.get("code"))) {
            if ("client".equals(jsonObject.get("status"))) {
                controller.sendMessageToAgent(session, jsonObject);
            }
            else{
                controller.sendMessageToClient(session, jsonObject);
            }
        }
        else if("5".equals(jsonObject.get("code"))){
            controller.createPair(jsonObject);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        if (base.getMapParameters().containsKey(session)) {
            if ("agent".equals(base.getMapParameters().get(session).getParameter1())) {
                controller.exitAgent(session);
            } else {
                controller.exitClient(session);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        LOGGER.error(" - User disconnect. "+thr);
    }
}

