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
            ModelMap modelMap,
            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestParam(name = "rowByPage", defaultValue = "10") int rowByPage
        ) {
        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase("ADMIN"));

            modelMap.addAttribute("isAdmin", isAdmin);

            if (isAdmin) {
                Page<User> userPage = userService.getUsersPage(pageNumber, rowByPage);
                modelMap.addAttribute("page", userPage);
                modelMap.addAttribute("availableRoles", userAuthorityService.getAvailableRoles());
            }

            Optional<User> loggedUser = userService.getUserByEmail(authentication.getName());
            loggedUser.ifPresent(user -> modelMap.addAttribute("logged_user", user));

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
