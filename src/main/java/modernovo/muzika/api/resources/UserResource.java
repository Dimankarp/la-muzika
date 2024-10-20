package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import modernovo.muzika.model.UserDTO;
import modernovo.muzika.repositories.UserRepository;

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

//        Principal principal = secCtx.getCallerPrincipal();
//        if (!secCtx.isCallerInRole(Role.ADMIN.name()) && !Objects.equals(username, principal.getName()) || principal == null) {
//            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Requested user doesn't correspond to the credentials").build();
//        }

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
