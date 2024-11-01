package modernovo.muzika.services;

import modernovo.muzika.model.Role;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public void registerUser(String username, String password) throws DataAccessException {
        var user = userRepo.save(new User(username, password));
        addRole(user, Role.USER);
    }

    @Transactional
    public void addRole(User user, Role role) {
        if (!user.getRoles().contains(role)) {
            userRepo.addRole(user, role);
        }
    }

    public boolean hasAdminRights(User user) {
        return user.getRoles().contains(Role.ADMIN);
    }

}