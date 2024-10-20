package modernovo.muzika.services;

import modernovo.muzika.model.Role;
import modernovo.muzika.model.RoleMember;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User registerUser(String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
            var user = userRepo.save(new User(username, password));
            userRepo.addRole(user, Role.USER);
            return user;
    }
}