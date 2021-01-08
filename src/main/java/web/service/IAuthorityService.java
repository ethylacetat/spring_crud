package web.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import web.model.Role.Role;

import java.util.List;
import java.util.Optional;

public interface IAuthorityService extends UserDetailsService {
    Optional<Role> getDefaultRole();

    List<Role> getAvailableRoles();

    Optional<Role> getRoleByID(long roleId);

    Optional<Role> getRoleByName(String roleName);

    void createRole(String roleName);
}
