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
            if ("agent".equals(base.getMapParameters().get(session).getParameter1())) {
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

