package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImpl() {
    }

    @Override
    public Optional<User> getUserById(long userId) {
        User user = entityManager.find(User.class, userId);

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAllUser() {
        return entityManager.createNamedQuery("getAllUser", User.class).getResultList();
    }

    @Override
    public List<User> getRangedUser(int from, int count) {
        return entityManager
                .createNamedQuery("getAllUser", User.class)
                .setFirstResult(from)
                .setMaxResults(count)
                .getResultList();
    }

    @Override
    public void deleteUSerById(long userId) {
        User user = entityManager.find(User.class, userId);

        // TODO: Бросать рантайм?
        if (user != null) {
            entityManager.remove(user);
        }


    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void mergeUser(User user) {
        entityManager.merge(user);
    }
}
