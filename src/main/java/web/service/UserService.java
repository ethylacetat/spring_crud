package web.service;

import web.model.User;
import web.util.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long userId);
    Optional<User> getUserByIdWithJoins(long userId);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByEmailWithJoins(String email);
    List<User> getAllUser();
    List<User> getRangedUser(int includedFrom, int excludedTo);
    Page<User> getUsersPage(int pageNumber, int rowCountByPage);
    void deleteUSerById(long userId);
    void addUser(User user);
    void mergeUser(User user);
}
