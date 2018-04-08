package alexshulga.model;

import javafx.util.Pair;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Base {

    private static Base instance;

    private List<Session> listAgent = new LinkedList<>();
    private List<Session> listClient = new LinkedList<>();
    private Map<Session, Parameters<Session, String>> mapClient = new HashMap<>();
    private Map<Pair, Session> mapAgent = new HashMap<>();
    private Map<Session, User> mapParametersUser = new HashMap<>();
    private Map<Session, LinkedList<String>> mapMessages = new HashMap<>();

    private Map<Long, Session> mapId = new HashMap<>();
    private final AtomicLong counterUsers = new AtomicLong();
    private final AtomicLong counterChats = new AtomicLong();

    public Base(){
    }

    public static Base getInstance(){
        if(instance == null){
            instance = new Base();
        }
        return instance;
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

    public Map<Session, User> getMapParameters() {
        return mapParametersUser;
    }

    public Map<Session, LinkedList<String>> getMapMessages() {
        return mapMessages;
    }

    public Map<Long, Session> getMapId() {
        return mapId;
    }

    public AtomicLong getCounterUsers() {
        return counterUsers;
    }

    public AtomicLong getCounterChats() {
        return counterChats;
    }
}
