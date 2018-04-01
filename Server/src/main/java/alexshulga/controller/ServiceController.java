package alexshulga.controller;

import alexshulga.model.Agent;
import alexshulga.model.Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.ArrayList;

@RestController
public class ServiceController {

    private HandlerServiceCommand handlerServiceCommand = new HandlerServiceCommand();

    @RequestMapping("/allAgents")
    public ArrayList<Agent> getAllAgents(@RequestParam(value = "pageNumber") int pageNumber,
                                         @RequestParam(value = "pageSize") int pageSize) {

        ArrayList<Agent> list = handlerServiceCommand.getAllAgent();
        return getPaginationAgent(list, pageNumber, pageSize);
    }

    @RequestMapping("/freeAgents")
    public ArrayList<Agent> getFreeAgents(@RequestParam(value = "pageNumber") int pageNumber,
                                          @RequestParam(value = "pageSize") int pageSize) {
        ArrayList<Agent> list = handlerServiceCommand.getFreeAgent();
        return getPaginationAgent(list, pageNumber, pageSize);
    }

    @RequestMapping("/freeAgents/number")
    public int getFreeAgentsNumber() {
        return handlerServiceCommand.getFreeAgent().size();
    }

    @RequestMapping("/allClientsInQueue")
    public ArrayList<Client> getAllClientsInQueue(@RequestParam(value = "pageNumber") int pageNumber,
                                                  @RequestParam(value = "pageSize") int pageSize){
        ArrayList<Client> list = handlerServiceCommand.getAllClientInQueue();
        return getPaginationClient(list, pageNumber, pageSize);
    }

    @RequestMapping("/registration/client")
    public String registrationClient(@RequestParam(value = "username") String username) throws IOException, DeploymentException {
        handlerServiceCommand.registrationClient(username);
        return "client "+username+" registered";
    }

    @RequestMapping("/registration/agent")
    public String registrationAgent(@RequestParam(value = "username") String username) throws IOException, DeploymentException {
        handlerServiceCommand.registrationAgent(username);
        return "agent "+username+" registered";
    }

    public ArrayList<Client> getPaginationClient(ArrayList<Client> list, int pageNumber, int pageSize){
        int allPages = (int) Math.ceil(list.size()/pageSize);
        int currentRecord = pageNumber * pageSize;
        int lastRecordInPage = 0;
        ArrayList<Client> listClientInPage = new ArrayList<>();

        if(pageNumber <= allPages) {
            if (pageNumber == allPages) {
                lastRecordInPage = list.size();
            } else {
                lastRecordInPage = currentRecord + pageSize;
            }

            for (int i = currentRecord; i < lastRecordInPage; i++) {
                listClientInPage.add(list.get(i));
            }
        }

        return listClientInPage;
    }

    public ArrayList<Agent> getPaginationAgent(ArrayList<Agent> list, int pageNumber, int pageSize){
        int allPages = (int) Math.ceil(list.size()/pageSize);
        int currentRecord = pageNumber * pageSize;
        int lastRecordInPage = 0;
        ArrayList<Agent> listAgentInPage = new ArrayList<>();

        if(pageNumber <= allPages) {
            if (pageNumber == allPages) {
                lastRecordInPage = list.size();
            } else {
                lastRecordInPage = currentRecord + pageSize;
            }

            for (int i = currentRecord; i < lastRecordInPage; i++) {
                listAgentInPage.add(list.get(i));
            }
        }

        return listAgentInPage;
    }

}
