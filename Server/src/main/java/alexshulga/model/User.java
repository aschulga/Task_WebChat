package alexshulga.model;

public class User {
    private long id;
    private String status;
    private String name;
    private String message;

    public User(){

    }

    public User(long id, String status, String name){
        this.id = id;
        this.status = status;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
