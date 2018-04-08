package alexshulga.controller.rest;

import alexshulga.model.Base;
import org.json.JSONObject;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.io.IOException;
import java.util.LinkedList;

@ClientEndpoint
public class RestUserHandler {

    private static final int MESSAGE = 4;
    private static final int MESSAGE_ABOUT_CONNECT = 5;
    private static final String TAB_NAME = "tabRestAgent";
    private Base base = Base.getInstance();

    @OnMessage
    public void onMessage(String msg, Session session) throws IOException {
        String[] strings = msg.split("\\|");

        switch (Integer.parseInt(strings[0])) {
            /*case MESSAGE:{
                if (!base.getMapMessages().containsKey(session)) {
                    base.getMapMessages().put(session, new LinkedList<>());
                }
                String message = "[" + strings[1] + " " + strings[2] + "] : " + strings[3];
                base.getMapMessages().get(session).push(message);
                break;
            }*/
            case MESSAGE_ABOUT_CONNECT:{
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
