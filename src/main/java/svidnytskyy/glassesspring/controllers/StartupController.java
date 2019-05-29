package svidnytskyy.glassesspring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartupController {
    @GetMapping("/")
    public String home() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!controller!");
//        return "forward:/index.html";
        return "index";
    }

}
