package alexshulga.controller;

import alexshulga.model.Base;
import org.json.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/user")
public class UserHandler {

    private static Base base = new Base();
    private static Controller controller = new Controller(base);

    @OnOpen
    public void onOpen(Session s) {
        System.out.println("open");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject jsonObject = new JSONObject(message);

        System.out.println("1");
        System.out.println("client "+controller.getBase().getListClient().size());
        System.out.println("agent "+controller.getBase().getListAgent().size());
        System.out.println("mapParameters "+controller.getBase().getMapParameters().size());
        System.out.println("mapPair "+controller.getBase().getMapPair().size());

        if("1".equals(jsonObject.get("code"))) {
            if("client".equals(jsonObject.get("status")))
                controller.registration(session,"client", jsonObject.get("username").toString());
            else{
                controller.registration(session,"agent", jsonObject.get("username").toString());
            }
            controller.sendInfo(session, "1",jsonObject.get("status").toString(), jsonObject.get("username").toString());
            //session.getBasicRemote().sendText("6");
            controller.createPair();
        }
        else if("2".equals(jsonObject.get("code"))){
            controller.leave(session);
            controller.createPair();
        }
        else if("3".equals(jsonObject.get("code"))){
            controller.exit(session);
        }
        else if("4".equals(jsonObject.get("code"))){
            controller.sendMessageToUser(session, jsonObject);
        }

        System.out.println("2");
        System.out.println("client "+controller.getBase().getListClient().size());
        System.out.println("agent "+controller.getBase().getListAgent().size());
        System.out.println("mapParameters "+controller.getBase().getMapParameters().size());
        System.out.println("mapPair "+controller.getBase().getMapPair().size());
    }

    @OnClose
    public void onClose(){
        System.out.println("Close");
    }
}

