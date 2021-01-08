package web.dao;

import web.model.User;

import java.util.List;
import java.util.Optional;

// TODO: Что по контрактам с нулами?
public interface UserDao {
    Optional<User> getUserById(long userId);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUser();
    List<User> getRangedUser(int from, int count);
    void deleteUSerById(long userId);
    void addUser(User user);
    void mergeUser(User user);
}
