package alexshulga.controller.rest;

import alexshulga.controller.Controller;
import alexshulga.controller.rest.RestUserHandler;
import alexshulga.model.*;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class HandlerServiceCommand {

    private Base base = Base.getInstance();

    public ArrayList<User> getAllUsers(){
        ArrayList<User> list = new ArrayList<>(base.getMapParameters().values());
        ArrayList<User> listAllUsers = new ArrayList<>();

        for(int i = 0; i < list.size(); i++){
            listAllUsers.add(new User(list.get(i).getId(), list.get(i).getStatus(), list.get(i).getName()));
        }

        return listAllUsers;
    }

    public ArrayList<User> getAllAgent(){
        ArrayList<User> list = new ArrayList<>(base.getMapParameters().values());
        ArrayList<User> listAllAgent = new ArrayList<>();

        for(int i = 0; i < list.size(); i++){
            if("agent".equals(list.get(i).getStatus())){
                listAllAgent.add(new User(list.get(i).getId(), list.get(i).getStatus(), list.get(i).getName()));
            }
        }

        return listAllAgent;
    }

    public ArrayList<User> getFreeAgent(){
        Map<Session, User> mapFreeAgent = new HashMap<>();
        ArrayList<User> listAllAgent = new ArrayList<>();

        for(int i = 0; i < base.getListAgent().size(); i++){
            Session sessionAgent = base.getListAgent().get(i);

            mapFreeAgent.put(sessionAgent, new User(base.getMapParameters().get(sessionAgent).getId(),
                    base.getMapParameters().get(sessionAgent).getStatus(), base.getMapParameters().get(sessionAgent).getName()));
        }

        ArrayList<User> valuesMap = new ArrayList<>(mapFreeAgent.values());
        for(int i = 0; i < mapFreeAgent.size(); i++){
            listAllAgent.add(new User(valuesMap.get(i).getId(), valuesMap.get(i).getStatus(), valuesMap.get(i).getName()));
        }

        return listAllAgent;
    }

    public ArrayList<User> getAllClientInQueue(){
        ArrayList<User> listClient = new ArrayList<>();

        for(int i = 0; i < base.getListClient().size(); i++){
            Session sessionClient = base.getListClient().get(i);
            listClient.add(new User(base.getMapParameters().get(sessionClient).getId(),
                    base.getMapParameters().get(sessionClient).getStatus(),
                    base.getMapParameters().get(sessionClient).getName()));        }

        return listClient;
    }

    public void registrationClient(String username) throws IOException, DeploymentException {
        WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        Session session = webSocketContainer.connectToServer(RestUserHandler.class,
                URI.create("ws://localhost:8080/user"));

        JSONObject resultJson = new JSONObject();

        resultJson.put("code", "1");
        resultJson.put("status", "client");
        resultJson.put("username", username);

        session.getBasicRemote().sendText(String.valueOf(resultJson));
    }

    public void registrationAgent(String username) throws IOException, DeploymentException {
        WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        Session session = webSocketContainer.connectToServer(RestUserHandler.class,
                URI.create("ws://localhost:8080/user"));

        JSONObject resultJson = new JSONObject();

        resultJson.put("code", "1");
        resultJson.put("status", "agent");
        resultJson.put("username", username);
        resultJson.put("numberClient", 1);
        resultJson.put("tabId", "tabRestAgent");

        session.getBasicRemote().sendText(String.valueOf(resultJson));
    }

    public String exit(Long id) throws IOException {

        String message;
        Session session = base.getMapId().get(id);

        if(base.getMapParameters().containsKey(session)) {
            message = base.getMapParameters().get(session).getStatus() + " "+base.getMapParameters().get(session).getName()+" exit";
            User user = base.getMapParameters().get(session);
            Controller controller = new Controller();

            if ("client".equals(user.getStatus())) {
                controller.exitClient(session);
            } else {
                controller.exitAgent(session);
            }
        }else{
            message = "user with this id does not exist";
        }

        return message;
    }

    public String sendMessageFromUser(Long id, String message) throws IOException {
        String str;
        Session session = base.getMapId().get(id);

        if(base.getMapParameters().containsKey(session)) {
            str = "Message sent from "+base.getMapParameters().get(session).getStatus() + " " +
                    base.getMapParameters().get(session).getName();
            User user = base.getMapParameters().get(session);
            Controller controller = new Controller();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "4");
            jsonObject.put("message", message);

            if("client".equals(user.getStatus())){
                controller.sendMessageToAgent(session, jsonObject);
            }
            if ("agent".equals(user.getStatus())) {
                jsonObject.put("tabId", "tabRestAgent");
                controller.sendMessageToClient(session, jsonObject);
            }
        }else{
            str = "user with this id does not exist";
        }

        return str;
    }

    public ArrayList<Chat> getAllOpenChat(){
        ArrayList<Chat> listChat = new ArrayList<>();

        ArrayList<Session> listSessionClient = new ArrayList<>(base.getMapClient().keySet());

        for(int i = 0; i < listSessionClient.size(); i++){
            User client = base.getMapParameters().get(listSessionClient.get(i));
            Session sessionAgent = base.getMapClient().get(listSessionClient.get((i))).getParameter1();
            User agent = base.getMapParameters().get(sessionAgent);

            Long id = base.getCounterChats().incrementAndGet();
            listChat.add(new Chat(id, client, agent));
        }

        return listChat;
    }

    public ArrayList<User> getPaginationUser(ArrayList<User> list, int pageNumber, int pageSize){
        int allPages = (int) Math.ceil(list.size()/pageSize);
        int currentRecord = pageNumber * pageSize;
        int lastRecordInPage = 0;
        ArrayList<User> listUserInPage = new ArrayList<>();

        if(pageNumber <= allPages) {
            if (pageNumber == allPages) {
                lastRecordInPage = list.size();
            } else {
                lastRecordInPage = currentRecord + pageSize;
            }

            for (int i = currentRecord; i < lastRecordInPage; i++) {
                listUserInPage.add(list.get(i));
            }
        }

        return listUserInPage;
    }

    public LinkedList<String> getHistoryMessagesRestUser(Long id){
        Session sessionRestUser = base.getMapId().get(id);
        return base.getMapMessages().get(sessionRestUser);
    }
}
