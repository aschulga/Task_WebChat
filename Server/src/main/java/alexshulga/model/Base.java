package alexshulga.model;

import javafx.util.Pair;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public class Base {

    private static Base instance;

    private List<Session> listAgent = new LinkedList<>();
    private List<Session> listClient = new LinkedList<>();
    private Map<Session, Parameters<Session, String>> mapClient = new HashMap<>();
    private Map<Pair, Session> mapAgent = new HashMap<>();
    private Map<Session, Parameters<String, String>> mapParameters = new HashMap<>();
    private Map<Session, LinkedList<String>> mapMessages = new HashMap<>();

    private Map<Integer, Session> mapIdSession = new HashMap<>();
    private Map<Session, Integer> mapSessionId = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

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

    public Map<Session, Parameters<String, String>> getMapParameters() {
        return mapParameters;
    }

    public Map<Session, LinkedList<String>> getMapMessages() {
        return mapMessages;
    }

    public Map<Integer, Session> getMapIdSession() {
        return mapIdSession;
    }

    public Map<Session, Integer> getMapSessionId() {
        return mapSessionId;
    }

    public AtomicInteger getCounter() {
        return counter;
    }
}
