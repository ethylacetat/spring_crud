package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.dao.UserDao;
import web.model.User;
import web.util.Page;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    public List<User> getRangedUser(int includedFrom, int excludedTo) {
        return userDao.getRangedUser(includedFrom, excludedTo);
    }

    public Page<User> getUsersPage(int pageNumber, int rowCountByPage) {
        boolean hasPrev = false;
        boolean hasNext = false;

        if (pageNumber < 1) {
            return Page.emptyPage(pageNumber, false, false, rowCountByPage);
        }

        int firstUser = rowCountByPage * (pageNumber - 1);

        List<User> users = userDao.getRangedUser(firstUser, rowCountByPage + 1);

        if (pageNumber > 1 && !users.isEmpty()) {
            hasPrev = true;
        }

        if (users.size() > rowCountByPage) {
            hasNext = true;
            users = users.subList(0, users.size() - 1);
        }

        return new Page<>(users, pageNumber, hasPrev, hasNext, rowCountByPage);
    }

    @Override
    public void deleteUSerById(long userId) {
        userDao.deleteUSerById(userId);
    }

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public void mergeUser(User user) {
        userDao.mergeUser(user);
    }
}
