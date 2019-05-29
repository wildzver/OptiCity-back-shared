package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import svidnytskyy.glassesspring.models.Phone;
import svidnytskyy.glassesspring.models.User;
import svidnytskyy.glassesspring.services.PhoneService;
import svidnytskyy.glassesspring.services.UserService;

import javax.servlet.ReadListener;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    UserService userService;
    @Autowired
    PhoneService phoneService;

    @PostMapping("/addUser")
    public String addUser(@RequestBody User user) {
        System.out.println(user);
//        System.out.println(user.getPhoneList().stream().findFirst());
        System.out.println("USER!!!!!!!!!");
        userService.save(user);


Phone phone = new Phone();
        phone.setUser(user);
        phoneService.save(phone);

        return "redirect:/";
    }
}
