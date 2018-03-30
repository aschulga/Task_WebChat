package alexshulga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormController {

    @GetMapping("/client_page")
    public String pageClient() {
        return "client_page";
    }

    @GetMapping("/agent_page")
    public String pageAgent() {
        return "agent_page";
    }

}
