package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.User;
import web.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getMainPage(Authentication authentication, ModelMap modelMap) {
        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase("ADMIN"));

            modelMap.addAttribute("isAdmin", isAdmin);
        }

        return "index";
    }

    @GetMapping
    @RequestMapping(path = "/user")
    public String getUserPage(Authentication authentication, ModelMap model) {
        Optional<User> optionalUser = userService.getUserByEmail(authentication.getName());
        optionalUser.ifPresent(user -> model.addAttribute("user", user));
        return "./user";
    }
}
