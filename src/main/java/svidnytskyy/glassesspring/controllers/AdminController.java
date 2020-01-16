package svidnytskyy.glassesspring.controllers;

import org.dom4j.io.SAXContentHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String admin() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Hello" + name);

        return "Hello" + name;
    }
}
