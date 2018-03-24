package alexshulga.controller;

import alexshulga.model.Base;
import alexshulga.model.Parameters;
import org.json.JSONObject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.Session;
import java.io.IOException;

public class Controller {

    private static final Logger LOGGER = LogManager.getLogger();
    private Base base;
    private int number = 2;

    public Controller(Base base){
        this.base = base;
    }

    public Base getBase() {
        return base;
    }

    public void createPair() throws IOException {

        LOGGER.log(Level.INFO, " --- registred");

        if (!base.getListClient().isEmpty() && !base.getListAgent().isEmpty()) {

            Session session1 = base.getListClient().remove(0);
            Session session2 = base.getListAgent().remove(0);

            session1.getBasicRemote().sendText("5"+"|"+base.getMapParameters().get(session2).getParameter1()
                    + "|" + base.getMapParameters().get(session2).getParameter2());

            session2.getBasicRemote().sendText("5"+"|"+base.getMapParameters().get(session1).getParameter1()
                    + "|" + base.getMapParameters().get(session1).getParameter2());

            base.getMapPair().put(session1, new Parameters<>(session2, true));
            base.getMapPair().put(session2, new Parameters<>(session1, false));

            clearListMessages(session2);
        }
    }

    public void clearListMessages(Session session) throws IOException {
        while (!base.getList().isEmpty()) {
            session.getBasicRemote().sendText(base.getList().removeLast());
            base.getMapPair().get(session).setParameter2(true);
        }
    }

    public Session findValue(Session session) {
        return base.getMapPair().get(session).getParameter1();
    }

    public void sendInfo(Session session, String code, String status, String userName) throws IOException {
        session.getBasicRemote().sendText(code + "|" + status + "|" + userName);
    }

    public void registration(Session session, String status, String name) throws IOException {
        if ("agent".equals(status)) {
            base.getListAgent().add(session);
        } else if ("client".equals(status)) {
            base.getListClient().add(session);
        }

        base.getMapParameters().put(session, new Parameters<>(status, name));
    }

    public void sendMessageToUser(Session session, JSONObject jsonObject) throws IOException {
        String textMessage = jsonObject.get("code") + "|"
                + base.getMapParameters().get(session).getParameter1() + "|"
                + base.getMapParameters().get(session).getParameter2() + "|"
                + jsonObject.get("message");

        session.getBasicRemote().sendText(textMessage);

        if("client".equals(base.getMapParameters().get(session).getParameter1()))
        {
            base.getList().push(textMessage);
        }

        if (base.getMapPair().containsKey(session)) {
            if (base.getMapPair().get(session).getParameter2()) {
                if ("agent".equals(base.getMapParameters().get(session).getParameter1())) {
                    base.getList().push(textMessage);
                }
                Session data = findValue(session);
                clearListMessages(data);
            }
        }
    }

    public void leave(Session session) throws IOException {
        if (base.getMapPair().containsKey(session)){

            if("client".equals(base.getMapParameters().get(session).getParameter1())) {

                sendInfo(findValue(session), "2", base.getMapParameters().get(session).getParameter1(),
                        base.getMapParameters().get(session).getParameter2());

                Session data = findValue(session);

                base.getListAgent().add(data);
                base.getListClient().add(session);

                base.getMapPair().remove(data);
                base.getMapPair().remove(session);
            }
            else {
                session.getBasicRemote().sendText("4" + "|" + base.getMapParameters().get(session).getParameter1() + "|"
                        + base.getMapParameters().get(session).getParameter2() + "|" + "/leave");

                findValue(session).getBasicRemote().sendText("4" + "|" + base.getMapParameters().get(session).getParameter1() + "|"
                        + base.getMapParameters().get(session).getParameter2() + "|" + "/leave");
            }
        }
    }

    public void exit(Session session) throws IOException {
        if (base.getMapPair().containsKey(session)) {

            sendInfo(findValue(session), "3", base.getMapParameters().get(session).getParameter1(),
                    base.getMapParameters().get(session).getParameter2());

            if ("client".equals(base.getMapParameters().get(session).getParameter1()))
                base.getListAgent().add(findValue(session));
            else
                base.getListClient().add(findValue(session));


            Session data = findValue(session);

            base.getMapPair().remove(data);
            base.getMapPair().remove(session);
            base.getMapParameters().remove(session);
            createPair();
        }
        else if(base.getMapParameters().containsKey(session)){

            if("client".equals(base.getMapParameters().get(session).getParameter1()))
                base.getListClient().remove(session);
            else
                base.getListAgent().remove(session);

            base.getMapParameters().remove(session);
        }
    }
}
