package modernovo.muzika.services;

import modernovo.muzika.model.User;
import modernovo.muzika.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResourceUtils {

    private final UserRepository userRepository;

    public ResourceUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCaller() throws CallerIsNotAUser {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElseThrow(() -> new CallerIsNotAUser("Failed to find " +
                "caller " +
                "username among users"));
    }

}
