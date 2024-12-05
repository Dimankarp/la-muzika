package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modernovo.muzika.model.User;
import modernovo.muzika.dto.UserDTO;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.UserService;
import modernovo.muzika.services.dto.creators.UserDTOCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController()
@RequestMapping("/api/auth")
public class AuthResource {

    private UserService userService;
    private UserRepository userRepository;
    private UserDTOCreatorService dtoCreator;
    private Logger logger = LoggerFactory.getLogger(AuthResource.class);

    public AuthResource(UserService userService, UserRepository userRepository, UserDTOCreatorService dtoCreator) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.dtoCreator = dtoCreator;
    }


    @GetMapping("/account")
    public UserDTO account(HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        if (user.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return dtoCreator.toDTO(user.get());
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return null;
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
