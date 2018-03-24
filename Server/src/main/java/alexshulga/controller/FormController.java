package alexshulga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormController {

    @GetMapping("/client")
    public String pageClient() {
        return "client";
    }

    @GetMapping("/agent")
    public String pageAgent() {
        return "agent";
    }
}
