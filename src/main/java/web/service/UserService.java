package web.service;

import web.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long userId);
    List<User> getAllUser();
    void deleteUSerById(long userId);
    void addUser(User user);
    void mergeUser(User user);
}
