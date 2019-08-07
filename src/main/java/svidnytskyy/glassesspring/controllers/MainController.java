package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
//import svidnytskyy.glassesspring.models.Category;
import svidnytskyy.glassesspring.models.Product;
import svidnytskyy.glassesspring.models.User;
//import svidnytskyy.glassesspring.services.PhoneService;
import svidnytskyy.glassesspring.services.ProductService;
import svidnytskyy.glassesspring.services.UserService;

import javax.servlet.ReadListener;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
//    @Autowired
//    UserService userService;
//    @Autowired
//    ProductService productService;
//    @Autowired
//    PhoneService phoneService;

//    @PostMapping("/addUser")
//    public String addUser(@RequestBody User user) {
//        System.out.println(user);
//        System.out.println(user.getPhoneList().stream().findFirst());
//        System.out.println("USER!!!!!!!!!");
//        userService.save(user);


//Category phone = new Category();
//        phone.setUser(user);
//        phoneService.save(phone);

//        return "redirect:/";
//    }

//    @PostMapping("/products/addProduct")
//    public String addProduct(@RequestBody Product product){
//        System.out.println(product);
//        productService.save(product);
//        return "redirect:/";
//    }
    
//    @GetMapping("/products")
//    public String findAll (){
//        System.out.println(productService.findAll());
//        productService.findAll();
//        return "redirect:/";
//    }
}
