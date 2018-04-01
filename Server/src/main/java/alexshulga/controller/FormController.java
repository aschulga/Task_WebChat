package alexshulga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FormController {

    @RequestMapping("/client_page")
    public String pageClient() {
        return "client_page";
    }

    @RequestMapping("/agent_page")
    public String pageAgent() {
        return "agent_page";
    }
}
