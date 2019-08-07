package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.Order;
import svidnytskyy.glassesspring.models.User;
import svidnytskyy.glassesspring.services.OrderService;
import svidnytskyy.glassesspring.services.UserService;

import java.text.ParseException;

@RestController
@RequestMapping({"/orders"})

public class OrderController {
    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;


    @PostMapping("/addOrder")
    public Order addOrder(@RequestBody Order order) throws ParseException {
//        System.out.println(orderList);
        System.out.println(order);
        return orderService.save(order);
    }

}
