package modernovo.muzika.repositories;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.Role;
import modernovo.muzika.model.RoleMember;
import modernovo.muzika.model.User;
import org.hibernate.Session;

public class RoleRepositoryImpl implements RoleRepository {

    private final EntityManager entityManager;

    RoleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void addRole(User user, Role role) {
        Session session = entityManager.unwrap(Session.class);
        session.persist(new RoleMember(role, user));
    }
}
