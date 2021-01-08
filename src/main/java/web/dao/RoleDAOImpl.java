package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDAOImpl implements IRoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public RoleDAOImpl() {
        // NO-OP
    }

    public List<Role> getAvailableRoles() {
        return entityManager.createNamedQuery("getAll", Role.class).getResultList();
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        List<Role> roles = entityManager
                .createNamedQuery("getByName", Role.class)
                .setParameter("name", roleName)
                .getResultList();

        // TODO: Всё таже проблема с листом и синглРезалтом
        if (!roles.isEmpty()) {
            return Optional.of(roles.get(0));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Role> getRoleByID(long roleId) {
        Role role = entityManager.find(Role.class, roleId);
        return Optional.ofNullable(role);
    }

    @Override
    public void createRole(String roleName) {
        entityManager.persist(new Role(roleName));
    }
}
