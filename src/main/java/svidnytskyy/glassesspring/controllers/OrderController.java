package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.Order;
import svidnytskyy.glassesspring.services.OrderService;
import svidnytskyy.glassesspring.services.UserService;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping({"/api/orders"})

public class OrderController {
    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @GetMapping("/addOrder")
    public Order getOrder(@RequestBody Order order) throws ParseException, IOException, MessagingException {
        return orderService.save(order);
    }

    @PostMapping("/addOrder")
    public ResponseEntity<String> addOrder(@Valid @RequestBody Order order) throws ParseException, MessagingException, IOException {
        orderService.save(order);
        return ResponseEntity.ok("User is valid");
    }
}
