package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.dao.IRoleDAO;
import web.dao.UserDao;
import web.model.Role.Role;
import web.model.User;
import web.util.JPAUtil;
import web.util.Page;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final IRoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, IRoleDAO roleDAO, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Optional<User> getUserByIdWithJoins(long userId) {
        Optional<User> optionalUser = userDao.getUserById(userId);
        // Принудительно загружаем Роли обращением к коллекции
        optionalUser.ifPresent(user -> JPAUtil.initialize(user.getRoles()));
        return optionalUser;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public Optional<User> getUserByEmailWithJoins(String email) {
        Optional<User> optionalUser = userDao.getUserByEmail(email);
        // Принудительно загружаем Роли обращением к коллекции
        optionalUser.ifPresent(user -> JPAUtil.initialize(user.getRoles()));

        return optionalUser;
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
        encodePasswordFor(user);
        userDao.addUser(user);
    }

    @Override
    public void addUser(User user, Set<String> userRoles) {
        Set<Role> existedRoles = roleDAO.getAvailableRoles()
                .stream()
                .filter(role -> userRoles.contains(role.getAuthority()))
                .collect(Collectors.toSet());

        user.setRoles(existedRoles);
        encodePasswordFor(user);
        userDao.addUser(user);
    }

    @Override
    public void mergeUser(User user) {
        encodePasswordFor(user);
        userDao.mergeUser(user);
    }

    @Override
    public void mergeUser(User user, Set<String> userRoles) {
        Optional<User> optional = userDao.getUserById(user.getId());

        if (optional.isPresent()) {
            User dbUser = optional.get();

            if (user.getPassword() == null || "".equalsIgnoreCase(user.getPassword())) {
                user.setPassword(dbUser.getPassword());
            } else {
                encodePasswordFor(user);
            }

            Set<Role> roles = roleDAO.getAvailableRoles()
                    .stream()
                    .filter(role -> userRoles.contains(role.getAuthority()))
                    .collect(Collectors.toSet());

            user.addAllRole(roles);

            userDao.mergeUser(user);
        }
    }

    private void encodePasswordFor(User user) {
        String userPass = user.getPassword();

        if (!"".equalsIgnoreCase(userPass)) {
            String encodedPass = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPass);
        }
    }
}
