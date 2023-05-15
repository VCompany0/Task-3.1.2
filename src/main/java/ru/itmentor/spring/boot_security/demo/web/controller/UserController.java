package ru.itmentor.spring.boot_security.demo.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmentor.spring.boot_security.demo.repositories.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;
import ru.itmentor.spring.boot_security.demo.user.User;

@Controller
public class UserController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = "/user")
    public String userPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByName(authentication.getName());
        model.addAttribute("user", currentUser);
        return "user_page";
    }

    //
//    @PostMapping("/user/update")
//    public String updateUser(@ModelAttribute("user") @Valid User user) {
//        User oldUser = userRepository.findByName(user.getName());
//        if(oldUser != null){
//            oldUser.setName(user.getName());
//            oldUser.setEmail(user.getEmail());
//            oldUser.setAge(user.getAge());
//            userRepository.save(oldUser);
//        }
//        return "redirect:/user_page";
//    }

    @PostMapping("/user/update/")
    public String updateUser(@ModelAttribute("user") @Valid User user) {
        User oldUser = userRepository.findByName(user.getName());
        if(oldUser != null){
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            oldUser.setAge(user.getAge());
            userRepository.save(oldUser);
        }
        return "redirect:/user";
    }
}
