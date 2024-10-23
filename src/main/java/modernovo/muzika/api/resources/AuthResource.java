package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modernovo.muzika.model.UserDTO;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.security.UserDetailsServiceImpl;
import modernovo.muzika.services.DTOCreatorService;
import modernovo.muzika.services.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController()
@RequestMapping("/api/auth")
public class AuthResource {

    private UserService userService;
    private UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(AuthResource.class);

    public AuthResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            userService.registerUser(username, password);
            return "Successfully registered!";
        } catch(DataAccessException e) {
            logger.debug("Failed to register {}, because of: {}", username, e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return "Failed to register user. Most probably it's already registered!";
        }

    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "Successfully logged out!";
    }

}
