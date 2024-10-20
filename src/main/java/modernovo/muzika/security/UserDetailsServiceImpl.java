package modernovo.muzika.security;

import jakarta.transaction.Transactional;
import modernovo.muzika.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loaded user by username " + username);
        logger.debug("Loaded user by username {}", username);
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            logger.debug("Founded user username: {} pass: {}, roles:{}", user.getUsername(), user.getPasswordHash(), user.getRoles().stream().map(Object::toString).toList());
            return new UserDetailsImpl(user.getUsername(), user.getPasswordHash(), user.getRoles());
        }
        throw new UsernameNotFoundException(String.format("User %s not found", username));
    }
}
