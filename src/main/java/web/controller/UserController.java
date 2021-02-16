package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.User;
import web.service.IAuthorityService;
import web.service.UserService;
import web.util.Page;

import java.util.HashSet;
import java.util.Optional;

@Controller
@RequestMapping
public class UserController {

    private final UserService userService;
    private final IAuthorityService userAuthorityService;

    @Autowired
    public UserController(UserService userService, IAuthorityService userAuthorityService) {
        this.userService = userService;
        this.userAuthorityService = userAuthorityService;
    }

    @GetMapping
    public String getMainPage(
            Authentication authentication,
            ModelMap modelMap
        ) {
        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase("ADMIN"));

            modelMap.addAttribute("isAdmin", isAdmin);

            if (isAdmin) {
                modelMap.addAttribute("availableRoles", userAuthorityService.getAvailableRoles());
            }

            userService.getUserByEmail(authentication.getName())
                    .ifPresent(user -> modelMap.addAttribute("logged_user", user));

        }

        return "index";
    }

}
