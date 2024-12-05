package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import modernovo.muzika.model.User;
import modernovo.muzika.dto.UserDTO;
import modernovo.muzika.repositories.UserRepository;

import modernovo.muzika.services.dto.creators.UserDTOCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController()
@RequestMapping("/api/users")
public class UserResource {
    private final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final UserDTOCreatorService dtoCreatorService;
    private final UserRepository userRepo;

    public UserResource(UserRepository userRepo, UserDTOCreatorService dtoCreatorService) {
        this.userRepo = userRepo;
        this.dtoCreatorService = dtoCreatorService;
    }

    @GetMapping(value = "/{username}")
    public UserDTO getUser(@PathVariable String username, HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.getName().equals(username) && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return dtoCreatorService.toDTO(user.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

    }


}
