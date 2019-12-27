package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.User;
import svidnytskyy.glassesspring.services.EmailsService;
import svidnytskyy.glassesspring.services.UserService;

import java.util.List;

@RestController
@RequestMapping({"/api/users"})
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    EmailsService emailsService;

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable("id") int id) {
        return userService.getOne(id);
    }


    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @DeleteMapping(path = {"/{id}"})
    public User delete(@PathVariable("id") int id) {
        return userService.delete(id);
    }
}
