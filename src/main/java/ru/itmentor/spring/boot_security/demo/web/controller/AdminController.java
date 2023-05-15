package ru.itmentor.spring.boot_security.demo.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.repositories.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;
import ru.itmentor.spring.boot_security.demo.user.Role;
import ru.itmentor.spring.boot_security.demo.user.User;

import java.util.Collections;
import java.util.List;


@Controller
public class AdminController {

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

    @GetMapping(value = "/admin")
    public String adminPage(ModelMap model){
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        return "admin_page";
    }

    @GetMapping(value = { "/index", "/", "/welcome"})
    public String viewMainPage(ModelMap model) {
        //service.createTables();
        return "welcome";
    }

    @GetMapping("/admin/addUser")
    public String addUser(Model model) {
        // create model attribute to bind form data
        User user = new User();
        model.addAttribute("user", user);
        return "add_user";
    }
//
    @PostMapping("/admin/updateUser/")
    public String updateUser(@ModelAttribute("user") @Valid User user) {
        User oldUser = userRepository.findByName(user.getName());
        if(oldUser != null){
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            oldUser.setAge(user.getAge());
            userRepository.save(oldUser);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/saveUser")
    public String saveNewUser(@ModelAttribute("user") @Valid User user) {
        User someUser = userRepository.findByName(user.getName());
        if(someUser == null){
            if(user.getRoles()== null || user.getRoles().isEmpty()){
                user.setRoles(Collections.singleton(new Role("ROLE_USER")));
            }
            userRepository.save(user);
        }
        else{
            return "redirect:/admin/addUser";
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/update/{name}")
    public String openUpdatePage(@PathVariable(value = "name") String name, Model model) {
        User user = userRepository.findByName(name);
        model.addAttribute("user", user);
        return "update_user";
    }

    @RequestMapping(value = "/admin/deleteUser/{name}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable(value = "name") String name) {
        User user = userRepository.findByName(name);
        userRepository.delete(user);
        return "redirect:/admin";
    }

}
