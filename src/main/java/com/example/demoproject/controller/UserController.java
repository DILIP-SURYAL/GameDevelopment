package com.example.demoproject.controller;

import com.example.demoproject.dao.ContactRepository;
import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.entities.Contact;
import com.example.demoproject.entities.User;
import com.example.demoproject.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String username = principal.getName();
//        System.out.println("USERNAME" + username);

        User user = this.userRepository.getUSerByUserName(username);
//        System.out.println("USER" + user);
        model.addAttribute("user", user);

    }

    @GetMapping("/index")
    public String dashBoard(Model model, Principal principal) {
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        model.addAttribute("flag", false);
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String process_contact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile multipartFile, Model model, Principal principal, HttpSession httpSession) {

//        if (!flag1) {
        boolean flag = false;
        try {
            String name = principal.getName();
            User user = this.userRepository.getUSerByUserName(name);
            contact.setUser(user);
            user.getList().add(contact);

            if (multipartFile.isEmpty()) {
                System.out.println("file is empty");
                contact.setImgURL("default.jpeg");
            } else {

                contact.setImgURL(multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), Paths.get(new ClassPathResource("static/img").getFile().getAbsolutePath() + File.separator + multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                flag = true;
            }

            this.userRepository.save(user);

        } catch (Exception e) {
            System.out.println("ERROR" + e.getMessage());
            e.printStackTrace();
        }

        if (flag) {
            httpSession.setAttribute("message", new Message("successfully added the contact", "alert-success"));

        } else {
            httpSession.setAttribute("message", new Message("Something went wrong", "alert-danger"));

        }
//        } else if (flag1) {
//            boolean flag = false;
//            try {
//
//                User user = this.userRepository.getUSerByUserName(principal.getName());
//                contact.setUser(user);
////                if (multipartFile.isEmpty()) {
////                    System.out.println("file is empty");
////                    contact.setImgURL("default.jpeg");
////                } else {
////
////                    contact.setImgURL(multipartFile.getOriginalFilename());
////                    Files.copy(multipartFile.getInputStream(), Paths.get(new ClassPathResource("static/img").getFile().getAbsolutePath() + File.separator + multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//                    flag = true;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            this.contactRepository.save(contact);
//            if (flag) {
//                httpSession.setAttribute("message", new Message("successfully Updated the contact", "alert-success"));
//
//            } else {
//                httpSession.setAttribute("message", new Message("Something went wrong", "alert-danger"));
//
//            }
//        }

        return "normal/add_contact_form";
    }

    @GetMapping("/show-contacts/{page}")
    public String show_contacts(@PathVariable("page") int page, Model model, Principal principal) {
        String name = principal.getName();
        User user = this.userRepository.getUSerByUserName(name);
        Pageable pageable = PageRequest.of(page, 3);
        Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());


        return "normal/show_contacts";
    }

    @GetMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") int cId, Model model, Principal principal) {
        Optional<Contact> contact = this.contactRepository.findById(cId);
        Contact contact1 = contact.get();
        String userName = principal.getName();
        User user = this.userRepository.getUSerByUserName(userName);
        if (user.getId() == contact1.getUser().getId()) {
            model.addAttribute("contact", contact1);
        }
        return "normal/contact_detail";
    }

    @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable("cId") int id, Principal principal, HttpSession httpSession) {

        Optional<Contact> optionalContact = this.contactRepository.findById(id);
        Contact contact = optionalContact.get();
        String userName = principal.getName();

        User user = this.userRepository.getUSerByUserName(userName);

        if (user.getId() == contact.getUser().getId()) {
            try {
                user.getList().removeIf(contact1 -> contact1.getcId() == contact.getcId());

//                Files.delete(Paths.get(new ClassPathResource("static/img").getFile().getAbsolutePath() + File.separator + contact.getImgURL()));

                this.userRepository.save(user);
                this.contactRepository.delete(contact);
                httpSession.setAttribute("message", new Message("contact deleted successfully", "alert-success"));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            httpSession.setAttribute("message", new Message("something went wrong", "alert-danger"));
        }
        return "redirect:/user/show-contacts/0";
    }

    @PostMapping("/update-contact/{cId}")
    public String updateContact(@PathVariable("cId") int id, Model model) {
        Optional<Contact> optionalContact = this.contactRepository.findById(id);
        Contact contact = optionalContact.get();
        model.addAttribute("contact", contact);
        model.addAttribute("flag", true);
        return "normal/add_contact_form";

    }

    @PostMapping("/update")
    public String updateContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile multipartFile, Model model, Principal principal, HttpSession httpSession) {

        System.out.println(contact.getName());
        System.out.println(contact.getcId());

                boolean flag = false;
        Contact oldContact = this.contactRepository.findById(contact.getcId()).get();
        System.out.println(contact);
        try {
            if (multipartFile.isEmpty()) {
                System.out.println("file is empty");
                contact.setImgURL("default.jpeg");
            } else {

                contact.setImgURL(multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), Paths.get(new ClassPathResource("static/img").getFile().getAbsolutePath() + File.separator + multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                flag = true;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        User user = this.userRepository.getUSerByUserName(principal.getName());
        contact.setUser(user);
        this.contactRepository.save(contact);
        if(flag){
         httpSession.setAttribute("message",new Message("successfully updated the contact","alert-success"));}
        else {
            httpSession.setAttribute("message",new Message("something went wrong","alert-danger"));}
        return "redirect:/user/"+contact.getcId()+"/contact";
    }


    @GetMapping("/profile")
    public String showProfile(){

        return "normal/profile";
    }
}
