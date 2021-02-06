package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.model.Role.Role;
import web.model.User;
import web.service.IAuthorityService;
import web.service.UserService;
import web.util.Page;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
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

    // TODO: Ренейм на пейдж
    // TODO: Изменить эндпоинт
    @GetMapping(path = AdminController.USERS_ROOT)
    public ResponseEntity<Page<User>> getAllUser(
            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestParam(name = "rowByPage", defaultValue = "10") int rowByPage
    ) {
        Page<User> userPage = userService.getUsersPage(pageNumber, rowByPage);
        // Не отправляем пользователю пароли пользователей
        // TODO: переделать на DTO?
        userPage.getPaginatedContent().forEach(user -> user.setPassword(null));

        return ResponseEntity.ok(userPage);
    }

    // Эндпоинт для получеия юзера по id
    @GetMapping(path = "/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") long userId) {
        if (userId < 0) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> optionalUser = userService.getUserByIdWithJoins(userId);

        // Не отправляем пользователю пароль
        optionalUser.ifPresent(user -> user.setPassword(null));

        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable(name = "id") long userId) {
        userService.deleteUSerById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // TODO: User -> UserDTO (и мержить руками)
    @PutMapping(path = "/users/{id}")
    public ResponseEntity<Void> mergeUser(
            @PathVariable(name = "id") long userId,
            @RequestParam(name = "role") Set<String> checkboxes,
            User mergedUser) {
        if (mergedUser.getEmail() == null || mergedUser.getEmail().equalsIgnoreCase("")) {
            return ResponseEntity.badRequest().build();
        }

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

        return ResponseEntity.ok().build();
    }

    // Адрес для запроса на создание пользователя
    // TODO: User -> UserDTO
    @PostMapping(path = "/users")
    public ResponseEntity<Void> addUser(User newUser, @RequestParam(name = "role") Set<String> roles) {
        if (newUser.getPassword() == null || newUser.getPassword().equalsIgnoreCase("")
                || newUser.getEmail() == null || newUser.getEmail().equalsIgnoreCase("")) {
            return ResponseEntity.badRequest().build();
        }

        Set<Role> existedRoles = userAuthorityService.getAvailableRoles()
                .stream()
                .filter(role -> roles.contains(role.getAuthority()))
                .collect(Collectors.toSet());

        newUser.setRoles(existedRoles);

        userService.addUser(newUser);

        URI userURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/../{id}").buildAndExpand(newUser.getId()).normalize().toUri();

        return ResponseEntity.created(userURI).build();
    }

    @GetMapping(path = "/roles")
    public ResponseEntity<List<Role>> getAllRole() {
        return ResponseEntity.ok(userAuthorityService.getAvailableRoles());
    }

    @GetMapping(path = "/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable(name = "id") long roleId) {
        return userAuthorityService.getRoleByID(roleId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // TODO: Теоритически сюда прилетит EntityExistsException, посмотреть как спринг оброаботает(нужна 409)
    @PostMapping(path = "/roles")
    public ResponseEntity<Void> createRole(String authority) {
        if (authority != null) {
            Role createdRole = userAuthorityService.createRole(authority);
            URI roleLocation = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdRole.getId())
                    .toUri();
            return ResponseEntity.created(roleLocation).build();
        }

        // TODO: Посмотреть что отвечать на пустой пользовательский запрос
        return ResponseEntity.badRequest().build();
    }
}
