package io.bootify.agenda_gym.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation. GetMapping;

@Controller
public class HomeResource {

    @GetMapping("/index-antiguo")
    public String indexAntiguo() {
        return "index";
    }

    @GetMapping("/hello")
    public String hello() {
        return "\"Hello World! \"";
    }
}