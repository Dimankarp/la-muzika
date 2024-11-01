package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.dto.StudioDTO;
import modernovo.muzika.repositories.StudioRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.BandService;
import modernovo.muzika.services.DTOConstraintViolationException;
import modernovo.muzika.services.UserService;
import modernovo.muzika.services.entity_creators.StudioEntityCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/studios")
public class StudioResource {

    private final Logger logger = LoggerFactory.getLogger(StudioResource.class);
    private final StudioEntityCreatorService entityCreatorService;
    private final UserRepository userRepository;
    private final BandService bandService;
    private final StudioRepository studioRepository;
    private final UserService userService;

    StudioResource(final UserRepository userRepository, final BandService bandService, StudioRepository studioRepository, StudioEntityCreatorService entityCreatorService, UserService userService) {
        this.userRepository = userRepository;
        this.bandService = bandService;
        this.studioRepository = studioRepository;
        this.entityCreatorService = entityCreatorService;
        this.userService = userService;
    }

    @PostMapping(value = "")
    public String postStudio(@RequestBody StudioDTO dto, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userOpt = userRepository.findByUsername(auth.getName());
        if (userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Caller not found among users";
        }
        var owner = userOpt.get();
        try {
            var studio = entityCreatorService.fromDTONew(dto, owner);
            studioRepository.save(studio);

        } catch (DTOConstraintViolationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Bad DTO: " + e.getMessage();
        }
        return "Created";
    }

    @PutMapping(value = "")
    public String putStudio(@RequestBody StudioDTO dto, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userOpt = userRepository.findByUsername(auth.getName());
        if (userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Caller not found among users";
        }
        var caller = userOpt.get();
        try {
            var studio = entityCreatorService.fromDTOUpdate(dto, caller);
            studioRepository.save(studio);
        } catch (DTOConstraintViolationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Bad DTO: " + e.getMessage();
        }

        return "Created";
    }


    @GetMapping(value = "")
    @Transactional
    public Page<StudioDTO> getStudios(@RequestParam(required = false) String owner, HttpServletResponse response, Pageable p) {
        if (owner != null) {
            var studiosOpt = bandService.getStudiosDTObyUsername(owner, p);
            if (studiosOpt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                return studiosOpt.get();
            }
        }
        return bandService.getStudiosDTO(p);

    }


    @DeleteMapping(value = "/{id}")
    @Transactional
    public String deleteStudio(@PathVariable Long id, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var studioOpt = studioRepository.findById(id);
        if (studioOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Failed to find studio";
        }
        var studio = studioOpt.get();
        if (studio.getOwner().getUsername().equals(auth.getName())) {
            studioRepository.delete(studio);
            return "Deleted";
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "Unauthorized";
    }


}
