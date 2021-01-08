package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.Role.Role;
import web.model.User;
import web.service.IAuthorityService;
import web.service.UserService;
import web.util.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Controller
@RequestMapping(path = AdminController.CONTROLLER_ROOT)
public class AdminController {

    public static final String CONTROLLER_ROOT = "/admin";
    public static final String USERS_ROOT = "/users";


    private final UserService userService;
    private final IAuthorityService userAuthorityService;

    @Autowired
    public AdminController(UserService userService, IAuthorityService userAuthorityService) {
        this.userService = userService;
        this.userAuthorityService = userAuthorityService;
    }

    @GetMapping
    @RequestMapping
    public String getAdminPage() {
        return "admin/adminMain";
    }

    // Отдаём страничку с юзерами
    @GetMapping(path = AdminController.USERS_ROOT)
    public String getAllUser(
            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestParam(name = "rowByPage", defaultValue = "20") int rowByPage,
            ModelMap model) {

        Page<User> userPage = userService.getUsersPage(pageNumber, rowByPage);
        model.addAttribute("page", userPage);
        return "admin/users/paginatedUsers";
    }

    // Получаем страничку с информацией о пользователе(также кнопки удаления и сохранения)
    @GetMapping(path = AdminController.USERS_ROOT + "/{id}")
    public String getUserById(
            @PathVariable(name = "id") long userId,
            ModelMap model) {

        Optional<User> optionalUser = userService.getUserByIdWithJoins(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Role> roles = user.getRoles();

            model.addAttribute("user", user);

            Map<Role, Boolean> roleCheckboxes = userAuthorityService.getAvailableRoles()
                    .stream().collect(Collectors.toMap(Function.identity(), role -> roles.contains(role)));

            model.addAttribute("roleCheckboxes", roleCheckboxes);
        } else {
            // TODO: 404
        }

        return "admin/users/user";
    }

    // Адрес для запроса на удаление пользователя
    @PostMapping(path = "/users/delete/{id}")
    public String deleteUserById(@PathVariable(name = "id") long userId) {
        userService.deleteUSerById(userId);
        return "redirect:/admin/users/";
    }

    // Адрес для запроса на обновление пользователя
    @PostMapping(path = "/users/{id}/edit")
    public String mergeUser(
            @PathVariable(name = "id") long userId,
            @RequestParam(name = "role") Set<String> checkboxes,
            User mergedUser) {
        Optional<User> optional = userService.getUserById(userId);

        if (optional.isPresent()) {
            User dbUser = optional.get();
            mergedUser.setPassword(dbUser.getPassword());

            List<Role> roleList = userAuthorityService.getAvailableRoles();

            Set<Role> roles = roleList
                    .stream()
                    .filter(role -> checkboxes.contains(role.getAuthority()))
                    .collect(Collectors.toSet());

            mergedUser.addAllRole(roles);

            userService.mergeUser(mergedUser);

        }

        return String.format("redirect:/admin/users/%s/", userId);
    }

    // Адрес для запроса на создание пользователя
    @PostMapping(path = "/users/post")
    public String addUser(User newUser, @RequestParam(name = "role") Set<String> checkboxes) {

        // TODO: Валидировать нуллполя
        Set<Role> roles = userAuthorityService.getAvailableRoles()
                .stream()
                .filter(role -> checkboxes.contains(role.getAuthority()))
                .collect(Collectors.toSet());

        newUser.setRoles(roles);

        userService.addUser(newUser);

        return "redirect:/admin/users/";
    }

    // Статическая страница для добавления пользователя
    @GetMapping(path = "/users/post")
    public String getUserCreateForm(ModelMap model) {

        model.addAttribute("availableRoles", userAuthorityService.getAvailableRoles());

        return "admin/users/post";
    }

    @GetMapping(path = "/roles")
    public String getAllRole(ModelMap model) {
        model.addAttribute("availableRoles", userAuthorityService.getAvailableRoles());
        return "admin/roleCreate";
    }

    @PostMapping(path = "/roles")
    public String createRole(String authority) {
        if (authority != null) {
            userAuthorityService.createRole(authority);
        }
        return "redirect:/admin/roles/";
    }
}
