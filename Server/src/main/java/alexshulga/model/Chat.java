package alexshulga.model;

public class Chat {
    private long id;
    private User client;
    private User agent;

    public Chat(){

    }

    public Chat(long id, User client, User agent){
        this.id = id;
        this.client = client;
        this.agent = agent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }
}
