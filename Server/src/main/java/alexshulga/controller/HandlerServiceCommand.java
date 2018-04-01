package alexshulga.controller;

import alexshulga.model.Agent;
import alexshulga.model.Base;
import alexshulga.model.Client;
import alexshulga.model.Parameters;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HandlerServiceCommand {

    private Base base = Base.getInstance();

    public ArrayList<Agent> getAllAgent(){
        int id = 1;
        ArrayList<Parameters> list = new ArrayList<>(base.getMapParameters().values());
        ArrayList<Agent> listAllAgent = new ArrayList<>();

        for(int i = 0; i < list.size(); i++){
            if("agent".equals(list.get(i).getParameter1())){
                listAllAgent.add(new Agent(id, list.get(i).getParameter2().toString()));
                id++;
            }
        }

        return listAllAgent;
    }

    public ArrayList<Agent> getFreeAgent(){
        Map<Session, String> mapFreeAgent = new HashMap<>();
        ArrayList<Agent> listAllAgent = new ArrayList<>();
        int id = 1;

        for(int i = 0; i < base.getListAgent().size(); i++){
            mapFreeAgent.put(base.getListAgent().get(i),
                    base.getMapParameters().get(base.getListAgent().get(i)).getParameter2());
        }

        ArrayList<String> valuesMap = new ArrayList<>(mapFreeAgent.values());
        for(int i = 0; i < mapFreeAgent.size(); i++){
            listAllAgent.add(new Agent(id, valuesMap.get(i)));
            id++;
        }

        return listAllAgent;
    }

    public ArrayList<Client> getAllClientInQueue(){
        int id = 1;
        ArrayList<Client> listClient = new ArrayList<>();

        for(int i = 0; i < base.getListClient().size(); i++){

            Session sessionClient = base.getListClient().get(i);
            String name = base.getMapParameters().get(sessionClient).getParameter2();
            listClient.add(new Client(id, name));
            id++;
        }

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
}
