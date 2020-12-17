package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.User;
import web.service.UserService;

@Controller
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Отдаём страничку с юзерами
    @GetMapping(path = "/")
    public String getAllUser(ModelMap model){
        model.addAttribute("UserPage", userService.getAllUser());
        return "paginatedUsers";
    }

    // Получаем страничку с информацией о пользователе(также кнопки удаления и сохранения)
    @GetMapping(path = "/{id}")
    public String getUserById(@PathVariable(name = "id") long userId, ModelMap model){
        User userById = userService.getUserById(userId).orElse(null);
        model.addAttribute("user", userById);

        return "users/user";
    }

    // Адрес для запроса на удаление пользователя
    @PostMapping(path = "delete/{id}")
    public String deleteUserById(@PathVariable(name = "id") long userId){
        userService.deleteUSerById(userId);
        return "redirect:/users/";
    }

    // Адрес для запроса на обновление пользователя
    @PostMapping(path = "/{id}/edit")
    public String mergeUser(@PathVariable(name = "id") long userId, User mergedUser){
        userService.mergeUser(mergedUser);
        return "redirect:/users/" + userId;
    }

    // Адрес для запроса на создание пользователя
    @PostMapping(path = "/post")
    public String addUser(User newUser) {
        userService.addUser(newUser);
        return "redirect:/users/";
    }

    // Статическая страница для добавления пользователя
    @GetMapping(path = "/post")
    public String getUserCreateForm() {
        return "users/post";
    }

}
