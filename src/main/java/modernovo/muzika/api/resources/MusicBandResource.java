package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.dto.MusicBandDTO;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.BandService;
import modernovo.muzika.services.DTOConstraintViolationException;
import modernovo.muzika.services.UserService;
import modernovo.muzika.services.entity_creators.BandEntityCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController()
@RequestMapping("/api/bands")
public class MusicBandResource {

    private final Logger logger = LoggerFactory.getLogger(MusicBandResource.class);
    private final BandEntityCreatorService entityCreatorService;
    private final UserRepository userRepository;
    private final BandRepository bandRepository;
    private final UserService userService;
    private final BandService bandService;

    MusicBandResource(final UserRepository userRepository, final BandService bandService, BandRepository bandRepository, BandEntityCreatorService entityCreatorService, UserService userService) {
        this.userRepository = userRepository;
        this.bandService = bandService;
        this.bandRepository = bandRepository;
        this.entityCreatorService = entityCreatorService;
        this.userService = userService;
    }

    @PostMapping(value = "")
    public String postBand(@RequestBody MusicBandDTO dto, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userOpt = userRepository.findByUsername(auth.getName());
        if (userOpt.isEmpty()) {
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
    public String putBand(@RequestBody MusicBandDTO dto, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userOpt = userRepository.findByUsername(auth.getName());
        if (userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Caller not found among users";
        }
        var caller = userOpt.get();
        try {
            MusicBand band;
            if (Objects.equals(caller.getId(), dto.getOwnerId())) {
                band = entityCreatorService.fromDTORegularUpdate(dto, caller);
            } else {
                band = entityCreatorService.fromDTOAdminUpdate(dto, caller);
            }
            bandRepository.save(band);
        } catch (DTOConstraintViolationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Bad DTO: " + e.getMessage();
        }

        return "Updated";
    }


    @GetMapping(value = "")
    @Transactional
    public Page<MusicBandDTO> getBands(@RequestParam(required = false) String owner, HttpServletResponse response, @PageableDefault(sort = {"name"}, value = 50) Pageable p) {
        if (owner != null) {
            var bandsOpt = bandService.getBandsDTObyUsername(owner, p);
            if (bandsOpt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                return bandsOpt.get();
            }
        }
        logger.debug("Request to get all bands without owner");
        return bandService.getBandsDTO(p);

    }

    @DeleteMapping(value = "/{id}")
    @Transactional
    public String deleteBand(@PathVariable Long id, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var bandOpt = bandRepository.findById(id);
        if (bandOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Failed to find band";
        }
        var band = bandOpt.get();
        if (band.getOwner().getUsername().equals(auth.getName())) {
            bandRepository.delete(band);
            return "Deleted";
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "Unauthorized";
    }


}
