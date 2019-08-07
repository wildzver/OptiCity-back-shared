package svidnytskyy.glassesspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.IOUtils;
import svidnytskyy.glassesspring.models.Product;
import svidnytskyy.glassesspring.models.User;
import svidnytskyy.glassesspring.services.EmailsService;
import svidnytskyy.glassesspring.services.UserService;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping({"/users"})
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    EmailsService emailsService;

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) throws IOException, MessagingException {
        System.out.println(user);
//        System.out.println(user.getPhoneList().stream().findFirst());
        System.out.println("USER!!!!!!!!!");

        File file = new File("public/upload-dir/5d1dc3e46d1bd680503848.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
        emailsService.sendEmail(user.getEmail(), multipartFile);

        return userService.save(user);
    }

    @PostMapping("/successLogined")
    public String successLogined(
            @AuthenticationPrincipal Authentication authentication,
            @AuthenticationPrincipal UserDetails userDetails,
            @AuthenticationPrincipal Principal principal) {
        Authentication currentAuthUser = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("1" + authentication);
        System.out.println("2" + userDetails);
        System.out.println("3" + principal);
        System.out.println("4" + currentAuthUser);
        System.out.println("SUCCESS LOGINED!!!!!!!!!");
        return "";
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable("id") int id) {
        return userService.getOne(id);
    }


    @GetMapping
    public List<User> findAll() {
        System.out.println("GET USER!!!!!!!!!!");
        System.out.println(userService.findAll());
        return userService.findAll();
    }

    @DeleteMapping(path = {"/{id}"})
    public User delete(@PathVariable("id") int id) {
        return userService.delete(id);
    }

}
