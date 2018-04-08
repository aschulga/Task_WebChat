package alexshulga.controller;

import alexshulga.model.Base;
import alexshulga.model.Parameters;
import alexshulga.model.User;
import javafx.util.Pair;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


public class Controller {

    //private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final Logger LOGGER = LogManager.getLogger();

    private Base base = Base.getInstance();

    public void checkForPair() throws IOException {

        if ((base.getListClient().size() != 0) && (base.getListAgent().size()) != 0) {
            Session session1 = base.getListClient().get(0);
            Session session2 = base.getListAgent().get(0);

            session1.getBasicRemote().sendText("5" + "|" + base.getMapParameters().get(session2).getStatus()
                    + "|" + base.getMapParameters().get(session2).getName());

            session2.getBasicRemote().sendText("5" + "|" + base.getMapParameters().get(session1).getStatus()
                    + "|" + base.getMapParameters().get(session1).getName());
        }
    }

    public void createPair(JSONObject jsonObject) throws IOException {
        Session session1 = base.getListClient().remove(0);
        Session session2 = base.getListAgent().remove(0);

        base.getMapClient().put(session1, new Parameters<>(session2, jsonObject.get("tabId").toString()));

        Pair key = new Pair(session2, jsonObject.get("tabId").toString());
        base.getMapAgent().put(key, session1);

        LOGGER.info( " - The beginning of a chat between "+base.getMapParameters().get(session1).getStatus()+" "+
                base.getMapParameters().get(session1).getName()+" and "+base.getMapParameters().get(session2).getStatus()+" "+
                base.getMapParameters().get(session2).getName());

        checkForPair();
        clearMapMessages(session1);
    }

    public void registration(Session session, JSONObject jsonObject) throws IOException {
        if ("client".equals(jsonObject.get("status"))) {
            base.getListClient().add(session);
        } else {
            for(int i = 0; i < Integer.parseInt(jsonObject.get("numberClient").toString()); i++)
                base.getListAgent().add(session);
        }

        Long id = base.getCounterUsers().incrementAndGet();

        base.getMapParameters().put(session, new User(id, jsonObject.get("status").toString(),
                jsonObject.get("username").toString()));

        base.getMapId().put(id, session);

        session.getBasicRemote().sendText("1" + "|" + base.getMapParameters().get(session).getStatus()
                + "|" + base.getMapParameters().get(session).getName());

        LOGGER.info(" - The appearance of "+jsonObject.get("status")+" "+jsonObject.get("username")+" in the system");

        checkForPair();
    }

    public void sendMessageToClient(Session sessionAgent, JSONObject jsonObject) throws IOException {
        if (base.getMapAgent().size() != 0) {
            Pair key = new Pair(sessionAgent, jsonObject.get("tabId").toString());
            Session sessionClient = base.getMapAgent().get(key);

            String textMessage = jsonObject.get("code") + "|"
                    + base.getMapParameters().get(sessionAgent).getStatus() + "|"
                    + base.getMapParameters().get(sessionAgent).getName() + "|"
                    + jsonObject.get("message") + "|" + jsonObject.get("tabId").toString();

            sessionAgent.getBasicRemote().sendText(textMessage);
            sessionClient.getBasicRemote().sendText(textMessage);
        }
    }

    public void sendMessageToAgent(Session sessionClient, JSONObject jsonObject) throws IOException {
        if (!base.getMapMessages().containsKey(sessionClient) && !base.getMapClient().containsKey(sessionClient)) {
            base.getMapMessages().put(sessionClient, new LinkedList<>());
        }

        String textMessage = jsonObject.get("code") + "|"
                + base.getMapParameters().get(sessionClient).getStatus() + "|"
                + base.getMapParameters().get(sessionClient).getName() + "|"
                + jsonObject.get("message");

        sessionClient.getBasicRemote().sendText(textMessage);
        sendMessage(sessionClient, jsonObject);
    }

    public void sendMessage(Session sessionClient, JSONObject jsonObject) throws IOException {
        if (base.getMapClient().size() != 0 && base.getMapClient().containsKey(sessionClient)) {

            String tabId = base.getMapClient().get(sessionClient).getParameter2();
            Session sessionAgent = base.getMapClient().get(sessionClient).getParameter1();

            String textMessage = jsonObject.get("code") + "|"
                    + base.getMapParameters().get(sessionClient).getStatus() + "|"
                    + base.getMapParameters().get(sessionClient).getName() + "|"
                    + jsonObject.get("message") + "|" + tabId;

            sessionAgent.getBasicRemote().sendText(textMessage);
        } else {
            String textMessage = jsonObject.get("code") + "|"
                    + base.getMapParameters().get(sessionClient).getStatus() + "|"
                    + base.getMapParameters().get(sessionClient).getName() + "|"
                    + jsonObject.get("message");

            base.getMapMessages().get(sessionClient).push(textMessage);
        }
    }

    public void clearMapMessages(Session session) throws IOException {
        if (base.getMapMessages().size() != 0) {
            String tabId = base.getMapClient().get(session).getParameter2();
            Session sessionAgent = base.getMapClient().get(session).getParameter1();

            for (int i = 0; i < base.getMapMessages().get(session).size(); i++) {
                String oneMessage = base.getMapMessages().get(session).remove(i) + "|" + tabId;
                base.getMapMessages().get(session).add(i, oneMessage);
            }

            for (int i = base.getMapMessages().get(session).size() - 1; i >= 0; i--) {
                String oneMessage = base.getMapMessages().get(session).get(i);
                sessionAgent.getBasicRemote().sendText(oneMessage);
            }

            base.getMapMessages().remove(session);
        }
    }

    public void leave(Session sessionClient) throws IOException {
        if (base.getMapClient().containsKey(sessionClient)) {
            if ("client".equals(base.getMapParameters().get(sessionClient).getStatus())) {
                String tabId = base.getMapClient().get(sessionClient).getParameter2();
                Session sessionAgent = base.getMapClient().get(sessionClient).getParameter1();

                String textMessage = 2 + "|"
                        + base.getMapParameters().get(sessionClient).getStatus() + "|"
                        + base.getMapParameters().get(sessionClient).getName() + "|" + tabId;

                sessionAgent.getBasicRemote().sendText(textMessage);

                LOGGER.info(" - The end of the chat between " + base.getMapParameters().get(sessionClient).getStatus() + " " +
                        base.getMapParameters().get(sessionClient).getName() + " and " + base.getMapParameters().get(sessionAgent).getStatus() + " " +
                        base.getMapParameters().get(sessionAgent).getName());

                Pair key = new Pair(sessionAgent, tabId);
                base.getMapAgent().remove(key);
                base.getMapClient().remove(sessionClient);
                base.getListAgent().add(sessionAgent);
                base.getListClient().add(sessionClient);
                checkForPair();
            }
        }
    }

    public void exitTabAgent(Session session, JSONObject jsonObject) throws IOException{
        Pair key = new Pair(session, jsonObject.get("tabId").toString());

        if (base.getMapAgent().containsKey(key)) {
            Session sessionClient = base.getMapAgent().get(key);

            String textMessage = 3 + "|"
                    + base.getMapParameters().get(session).getStatus() + "|"
                    + base.getMapParameters().get(session).getName();

            sessionClient.getBasicRemote().sendText(textMessage);

            LOGGER.info(" - The "+base.getMapParameters().get(session).getStatus()+" "+
                    base.getMapParameters().get(session).getName()+" left the system");

            LOGGER.info(" - The end of the chat between "+base.getMapParameters().get(sessionClient).getStatus()+" "+
                    base.getMapParameters().get(sessionClient).getName()+" and "+base.getMapParameters().get(session).getStatus()+" "+
                    base.getMapParameters().get(session).getName());

            base.getMapAgent().remove(key);
            base.getMapClient().remove(sessionClient);
            base.getListAgent().add(session);
            base.getListClient().add(sessionClient);
            checkForPair();
        }
        else{
            for (int i = 0; i < base.getListAgent().size(); i++) {
                if (base.getListAgent().get(i).equals(session)) {
                    base.getListAgent().remove(i);
                    --i;
                }
            }

            LOGGER.info(" - The "+base.getMapParameters().get(session).getStatus()+" "+
                    base.getMapParameters().get(session).getName()+" left the system");

            Long id = base.getMapParameters().get(session).getId();

            base.getMapId().remove(id);
            base.getMapParameters().remove(session);

            checkForPair();
        }
    }

    public void exitAgent(Session session)throws IOException {
        if (base.getMapAgent().size() != 0) {

            ArrayList<Session> arrayList = new ArrayList<>(base.getMapAgent().values());

            for (int i = 0; i < arrayList.size(); i++) {
                Session sessionClient = arrayList.get(i);

                if (base.getMapClient().get(sessionClient).getParameter1().equals(session)) {
                    String tabId = base.getMapClient().get(sessionClient).getParameter2();

                    String textMessage = 3 + "|"
                            + base.getMapParameters().get(session).getStatus() + "|"
                            + base.getMapParameters().get(session).getName();

                    sessionClient.getBasicRemote().sendText(textMessage);

                    LOGGER.info(" - The "+base.getMapParameters().get(session).getStatus()+" "+
                            base.getMapParameters().get(session).getName()+" left the system");

                    LOGGER.info(" - The end of the chat between "+base.getMapParameters().get(sessionClient).getStatus()+" "+
                            base.getMapParameters().get(sessionClient).getName()+" and "+base.getMapParameters().get(session).getStatus()+" "+
                            base.getMapParameters().get(session).getName());

                    Pair key = new Pair(session, tabId);
                    base.getMapAgent().remove(key);
                    base.getMapClient().remove(sessionClient);
                    base.getListClient().add(sessionClient);
                }
            }
        }

        for (int i = 0; i < base.getListAgent().size(); i++) {
            if (base.getListAgent().get(i).equals(session)) {
                base.getListAgent().remove(i);
                --i;
            }
        }

        Long id = base.getMapParameters().get(session).getId();
        base.getMapId().remove(id);
        base.getMapParameters().remove(session);

        checkForPair();
    }

    public void exitClient(Session session) throws IOException {

        if (base.getMapClient().containsKey(session)) {
            String tabId = base.getMapClient().get(session).getParameter2();
            Session sessionAgent = base.getMapClient().get(session).getParameter1();

            String textMessage = 3 + "|"
                    + base.getMapParameters().get(session).getStatus() + "|"
                    + base.getMapParameters().get(session).getName() + "|" + tabId;

            sessionAgent.getBasicRemote().sendText(textMessage);

            LOGGER.info(" - The "+base.getMapParameters().get(session).getStatus()+" "+
                    base.getMapParameters().get(session).getName()+" left the system");

            LOGGER.info(" - The end of the chat between "+base.getMapParameters().get(session).getStatus()+" "+
                    base.getMapParameters().get(session).getName()+" and "+base.getMapParameters().get(sessionAgent).getStatus()+" "+
                    base.getMapParameters().get(sessionAgent).getName());

            Pair key = new Pair(sessionAgent, tabId);
            base.getMapAgent().remove(key);
            base.getMapClient().remove(session);
            base.getListAgent().add(sessionAgent);

            Long id = base.getMapParameters().get(session).getId();
            base.getMapId().remove(id);
            base.getMapParameters().remove(session);

            checkForPair();
        } else if (base.getMapParameters().containsKey(session)) {

            LOGGER.info(" - The "+base.getMapParameters().get(session).getStatus()+" "+
                    base.getMapParameters().get(session).getName()+" left the system");

            base.getListClient().remove(session);
            base.getMapMessages().remove(session);

            Long id = base.getMapParameters().get(session).getId();
            base.getMapId().remove(id);
            base.getMapParameters().remove(session);
        }
    }
}