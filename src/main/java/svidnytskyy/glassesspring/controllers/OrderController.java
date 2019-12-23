package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import svidnytskyy.glassesspring.models.Order;
import svidnytskyy.glassesspring.models.User;
import svidnytskyy.glassesspring.services.OrderService;
import svidnytskyy.glassesspring.services.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static svidnytskyy.glassesspring.models.User_.firstName;

@RestController
@RequestMapping({"/api/orders"})

public class OrderController {
    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;


    @GetMapping("/addOrder")
    public Order getOrder(@RequestBody Order order) throws ParseException, IOException, MessagingException {
        System.out.println(order);
        return orderService.save(order);
    }

    @PostMapping("/addOrder")
    public ResponseEntity<String> addOrder(@Valid @RequestBody Order order) throws ParseException, MessagingException, IOException {
//        this.handleValidationExceptions();
//        System.out.println(order);
//        System.out.println("FIELD ERROR" + result.getFieldErrors());
//        result.getFieldErrors().forEach(fieldError -> System.out.println("MY ERROR" + fieldError.getField()));
        orderService.save(order);
        return ResponseEntity.ok("User is valid");
    }


    @PostMapping("/sendOrderMailClient")
    public Long sendOrderMailClient(@RequestBody String id/*, @PathVariable Integer ids*/) {
        System.out.println("MY CHECK ID" + id);
//        System.out.println("MY CHECK IDS" + ids);
        System.out.println("SEND MAIL!!!");

        return Long.valueOf(1250);
    }

    @GetMapping("/sendorder")
    public String sendOrderById(@PathVariable Integer ids, @RequestParam String id) {
        System.out.println("MY CHECK IDS" + ids);
        System.out.println("MY CHECK ID" + id);


        return "HELLO";
    }

//    @GetMapping("/sendOrderMailClient/{ids}")
//    public String sendOrderMailClient(@PathVariable("ids") String ids) {
//        System.out.println("MY CHECK IDS" + ids);
//        System.out.println("SEND MAIL!!!");
//        return "SUPER!";
//    }

}
