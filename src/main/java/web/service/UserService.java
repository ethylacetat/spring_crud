package web.service;

import web.model.User;
import web.util.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long userId);
    List<User> getAllUser();
    List<User> getRangedUser(int includedFrom, int excludedTo);
    Page<User> getUsersPage(int pageNumber, int rowCountByPage);
    void deleteUSerById(long userId);
    void addUser(User user);
    void mergeUser(User user);
}
