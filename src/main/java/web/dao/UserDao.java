package web.dao;

import web.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long userId);
    List<User> getAllUser();
    List<User> getRangedUser(int from, int count);
    void deleteUSerById(long userId);
    void addUser(User user);
    void mergeUser(User user);
}
