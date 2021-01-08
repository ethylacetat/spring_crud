package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.IRoleDAO;
import web.dao.UserDao;
import web.model.Role.Role;
import web.model.User;
import web.util.JPAUtil;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserAuthorityService implements IAuthorityService {

    private final IRoleDAO roleDAO;

    private final UserDao userDao;

    @Autowired
    public UserAuthorityService(IRoleDAO roleDAO, UserDao userDao) {
        this.roleDAO = roleDAO;
        this.userDao = userDao;
    }

    @Override
    public Optional<Role> getDefaultRole() {
        return roleDAO.getRoleByName("USER");
    }

    @Override
    public List<Role> getAvailableRoles() {
        return roleDAO.getAvailableRoles();
    }

    // TODO: Могу ли я тут вернуть нулл?
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.getUserByEmail(s).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        try {
            throw new NullPointerException("Кто блять это делает?");
        } catch (Throwable t) {
            t.printStackTrace();
        }

        JPAUtil.initialize(user.getRoles());

        return user;
    }

    public Optional<Role> getRoleByID(long roleId) {
        return roleDAO.getRoleByID(roleId);
    }

    public Optional<Role> getRoleByName(String roleName) {
        return roleDAO.getRoleByName(roleName);
    }

    public void createRole(String roleName) {
        roleDAO.createRole(roleName);
    }
}
