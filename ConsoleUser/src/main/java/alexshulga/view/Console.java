package alexshulga.view;

import alexshulga.controller.ReadFromServer;
import alexshulga.controller.WriteToServer;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

public class Console {

    private String url;
    private WebSocketContainer webSocketContainer;
    private Session session;

    public Console(String url){
        this.url = url;
    }

    public void init(){
        webSocketContainer = ContainerProvider.getWebSocketContainer();
        try {
            session = webSocketContainer.connectToServer(ReadFromServer.class, URI.create(url));
            WriteToServer writeToServer = new WriteToServer();
            writeToServer.write(session);
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exit(){
        System.exit(0);
    }
}
