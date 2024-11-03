package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.dto.AlbumDTO;
import modernovo.muzika.dto.StudioDTO;
import modernovo.muzika.repositories.StudioRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.*;
import modernovo.muzika.services.entity_creators.StudioEntityCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/studios")
public class StudioResource {

    private final Logger logger = LoggerFactory.getLogger(StudioResource.class);
    private final StudioEntityCreatorService entityCreatorService;
    private final StudioService studioService;
    private final StudioRepository studioRepository;
    private final ResourceUtils resourceUtils;

    StudioResource(StudioService studioService, StudioRepository studioRepository, StudioEntityCreatorService entityCreatorService, UserService userService, ResourceUtils resourceUtils) {
        this.studioService = studioService;
        this.studioRepository = studioRepository;
        this.entityCreatorService = entityCreatorService;
        this.resourceUtils = resourceUtils;
    }

    @PostMapping(value = "")
    public String postStudio(@RequestBody StudioDTO dto, HttpServletResponse response) throws CallerIsNotAUser {
        var owner = resourceUtils.getCaller();
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
    public String putStudio(@RequestBody StudioDTO dto, HttpServletResponse response) throws CallerIsNotAUser {
        var caller = resourceUtils.getCaller();
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
    public Page<StudioDTO> getStudios(@RequestParam(required = false) String owner,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String address,
                                      HttpServletResponse response,
                                      @PageableDefault(sort = {"name"}, value = 50) Pageable p) throws IllegalServiceArgumentException {
        var studioOpt = studioService.getStudiosDTO(owner, name, address, p);
        if (studioOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } else {
            return studioOpt.get();
        }
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
