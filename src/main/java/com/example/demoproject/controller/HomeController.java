package com.example.demoproject.controller;

import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.entities.User;
import com.example.demoproject.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/")
    public String home(Model model) {

        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        return "about";
    }

    @RequestMapping("/signUp")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signUp";

    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/register_page")
    public String register_page(@Valid @ModelAttribute("user") User user,  BindingResult result1,@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {
        try {
            if (!agreement) {

                System.out.println("you have not accepted the terms and condition");
                throw new Exception("you have not accepted the terms and condition");
            }

            if(result1.hasErrors()){
                System.out.println("ERROR"+result1.toString());
                model.addAttribute("user",user);
                return "signUp";

            }
            user.setEnabled(true);
            user.setRole("ROLE_USER");
            user.setImgURL("default.png");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            User user1 = this.userRepository.save(user);
            System.out.println(user);
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("successfully registered", "alert-success"));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("something went wrong" + e.getMessage(), "alert-error"));
        }
        return "signUp";
    }

}
