package web.dao;

import web.model.Role.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleDAO {
    List<Role> getAvailableRoles();

    Optional<Role> getRoleByName(String roleName);

    Optional<Role> getRoleByID(long roleId);

    Role createRole(String roleName);
}
