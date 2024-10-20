package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import modernovo.muzika.model.UserDTO;
import modernovo.muzika.repositories.UserRepository;

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

    private final UserRepository userRepo;

    public UserResource(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping(value = "/{username}")
    public UserDTO getUser(@PathVariable String username, HttpServletResponse response) {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.getName().equals(username) && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        Optional<UserDTO> dto = userRepo.findUserDTOByName(username);
        if (dto.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return dto.get();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

    }


}
