package alexshulga.controller.rest;

import alexshulga.model.Chat;
import alexshulga.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

@RestController
@RequestMapping("/")
@Api(value="webChat", description="Operations pertaining to Web-Chat")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
}
)
public class ServiceController {
    private HandlerServiceCommand handlerServiceCommand = new HandlerServiceCommand();

    @ApiOperation(value = "View a list of all agents",response = ArrayList.class)
    @RequestMapping(value = "/allAgents", method = RequestMethod.GET)
    public ArrayList<User> getAllAgents(@RequestParam(value = "pageNumber") int pageNumber,
                                         @RequestParam(value = "pageSize") int pageSize) {

        ArrayList<User> list = handlerServiceCommand.getAllAgent();
        return handlerServiceCommand.getPaginationUser(list, pageNumber, pageSize);
    }

    @ApiOperation(value = "View a list of free agents",response = ArrayList.class)
    @RequestMapping(value = "/freeAgents", method = RequestMethod.GET)
    public ArrayList<User> getFreeAgents(@RequestParam(value = "pageNumber") int pageNumber,
                                          @RequestParam(value = "pageSize") int pageSize) {
        ArrayList<User> list = handlerServiceCommand.getFreeAgent();
        return handlerServiceCommand.getPaginationUser(list, pageNumber, pageSize);
    }

    @ApiOperation(value = "View a number of free agents",response = Integer.class)
    @RequestMapping(value = "/freeAgents/number", method = RequestMethod.GET)
    public int getFreeAgentsNumber() {
        return handlerServiceCommand.getFreeAgent().size();
    }

    @ApiOperation(value = "View a list of clients in queue",response = ArrayList.class)
    @RequestMapping(value = "/allClientsInQueue", method = RequestMethod.GET)
    public ArrayList<User> getAllClientsInQueue(@RequestParam(value = "pageNumber") int pageNumber,
                                                  @RequestParam(value = "pageSize") int pageSize){
        ArrayList<User> list = handlerServiceCommand.getAllClientInQueue();
        return handlerServiceCommand.getPaginationUser(list, pageNumber, pageSize);
    }

    @ApiOperation(value = "View a list of clients in queue",response = ArrayList.class)
    @RequestMapping(value = "/allUsers", method = RequestMethod.GET)
    public ArrayList<User> getAllUsers(@RequestParam(value = "pageNumber") int pageNumber,
                                       @RequestParam(value = "pageSize") int pageSize) {

        ArrayList<User> list = handlerServiceCommand.getAllUsers();
        return handlerServiceCommand.getPaginationUser(list, pageNumber, pageSize);
    }

    @ApiOperation(value = "View a list of all open chats",response = ArrayList.class)
    @RequestMapping(value = "/allOpenChat", method = RequestMethod.GET)
    public ArrayList<Chat> getAllOpenChat(){
        return handlerServiceCommand.getAllOpenChat();
    }

    @ApiOperation(value = "Register client",response = String.class)
    @RequestMapping(value="/registration/client", method = RequestMethod.POST)
    public String registrationClient(@RequestBody User user, HttpServletRequest request) throws IOException, DeploymentException {
        handlerServiceCommand.registrationClient(user.getName());
        return "client "+user.getName()+" registered";
    }

    @ApiOperation(value = "Register agent",response = String.class)
    @RequestMapping(value="/registration/agent", method = RequestMethod.POST)
    public String registrationAgent(@RequestBody User user) throws IOException, DeploymentException {
        handlerServiceCommand.registrationAgent(user.getName());
        return "agent "+user.getName()+" registered";
    }

    @ApiOperation(value = "Send message from user",response = String.class)
    @RequestMapping(value="/sendMessageFromUser", method = RequestMethod.POST)
    public String sendMessageFromUser(@RequestBody User user) throws IOException {
        return handlerServiceCommand.sendMessageFromUser(user.getId(), user.getMessage());
    }

    @RequestMapping(value="/historyMessages", method = RequestMethod.POST)
    public LinkedList<String> getHistoryMessages(@RequestBody User user){
        return handlerServiceCommand.getHistoryMessagesRestUser(user.getId());
    }

    @ApiOperation(value = "Exit user",response = String.class)
    @RequestMapping(value="/exit", method = RequestMethod.POST)
    public String exit(@RequestBody User user) throws IOException{
        return handlerServiceCommand.exit(user.getId());
    }
}
