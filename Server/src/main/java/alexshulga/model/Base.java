package alexshulga.model;

import javafx.util.Pair;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Base {

    private List<Session> listAgent = new LinkedList<>();
    private List<Session> listClient = new LinkedList<>();
    private Map<Session, Parameters<Session, String>> mapClient = new HashMap<>();
    private Map<Pair, Session> mapAgent = new HashMap<>();
    private Map<Session, Parameters<String, String>> mapParameters = new HashMap<>();
    private Map<Session, LinkedList<String>> mapMessages = new HashMap<>();

    public Base(){
    }

    public List<Session> getListAgent() {
        return listAgent;
    }

    public List<Session> getListClient() {
        return listClient;
    }

    public Map<Session, Parameters<Session, String>> getMapClient() {
        return mapClient;
    }

    public Map<Pair, Session> getMapAgent() {
        return mapAgent;
    }

    public Map<Session, Parameters<String, String>> getMapParameters() {
        return mapParameters;
    }

    public Map<Session, LinkedList<String>> getMapMessages() {
        return mapMessages;
    }
}
