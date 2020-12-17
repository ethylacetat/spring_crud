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

    public UserDaoImpl(){}

    @Override
    public Optional<User> getUserById(long userId) {
        User user = entityManager.find(User.class, userId);

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAllUser() {
        return entityManager.createQuery("SELECT user FROM User user", User.class).getResultList();
    }

    @Override
    public void deleteUSerById(long userId) {
        User user = entityManager.find(User.class, userId);
        entityManager.remove(user);
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
