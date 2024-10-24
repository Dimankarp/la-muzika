package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.MusicBandDTO;
import modernovo.muzika.model.StudioDTO;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.StudioRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.BandService;
import modernovo.muzika.services.DTOConstraintViolationException;
import modernovo.muzika.services.EntityCreatorService;
import modernovo.muzika.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/bands")
public class StudioResource {

    private final Logger logger = LoggerFactory.getLogger(StudioResource.class);
    private final EntityCreatorService entityCreatorService;
    private final UserRepository userRepository;
    private final BandService bandService;
    private final StudioRepository studioRepository;
    private final UserService userService;

    StudioResource(final UserRepository userRepository, final BandService bandService, StudioRepository studioRepository, EntityCreatorService entityCreatorService, UserService userService) {
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
        if(userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Caller not found among users";
        }
        var owner = userOpt.get();
        try {
            var band = entityCreatorService.fromDTONew(dto, owner);
            bandRepository.save(band);

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
        if(userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Caller not found among users";
        }
        var caller = userOpt.get();
        try {
                var band = entityCreatorService.fromDTORegularUpdate(dto, caller);
                studioRepository.save(band);
        }
        catch (DTOConstraintViolationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Bad DTO: " + e.getMessage();
        }

        return "Created";
    }


    @GetMapping(value = "")
    @Transactional
    public Page<StudioDTO> getStudios(@RequestParam(required = false) String owner, HttpServletResponse response, Pageable p) {
        if(owner != null){
            var bandsOpt =  bandService.getBandsDTObyUsername(owner, p);
            if(bandsOpt.isEmpty()){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                return bandsOpt.get();
            }
        }
        logger.debug("Request to get all bands without owner");
        return bandService.getBandsDTO(p);

    }


}
