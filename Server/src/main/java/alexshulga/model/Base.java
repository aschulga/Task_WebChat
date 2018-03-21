package alexshulga.model;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Base {

    private List<Session> listAgent = new LinkedList<Session>();
    private List<Session> listClient = new LinkedList<Session>();
    private Map<Session, Parameters<Session, Boolean>> mapPair = new HashMap<Session, Parameters<Session, Boolean>>();
    private Map<Session, Parameters<String, String>> mapParameters = new HashMap<Session, Parameters<String, String>>();
    private LinkedList<String> list = new LinkedList();

    public Base(){
    }

    public List<Session> getListAgent() {
        return listAgent;
    }

    public List<Session> getListClient() {
        return listClient;
    }

    public Map<Session, Parameters<Session, Boolean>> getMapPair() {
        return mapPair;
    }

    public Map<Session, Parameters<String, String>> getMapParameters() {
        return mapParameters;
    }

    public LinkedList<String> getList() {
        return list;
    }
}
