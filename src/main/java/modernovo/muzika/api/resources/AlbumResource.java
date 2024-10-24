package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.AlbumDTO;
import modernovo.muzika.model.MusicBandDTO;
import modernovo.muzika.repositories.AlbumRepository;
import modernovo.muzika.repositories.BandRepository;
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
public class AlbumResource {

    private final Logger logger = LoggerFactory.getLogger(AlbumResource.class);
    private final EntityCreatorService entityCreatorService;
    private final UserRepository userRepository;
    private final BandService bandService;
    private final BandRepository bandRepository;
    private final UserService userService;
    private final AlbumRepository albumRepository;

    AlbumResource(final UserRepository userRepository, final BandService bandService, BandRepository bandRepository, EntityCreatorService entityCreatorService, UserService userService, AlbumRepository albumRepository) {
        this.userRepository = userRepository;
        this.bandService = bandService;
        this.bandRepository = bandRepository;
        this.entityCreatorService = entityCreatorService;
        this.userService = userService;
        this.albumRepository = albumRepository;
    }

    @PostMapping(value = "")
    public String postAlbum(@RequestBody AlbumDTO dto, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userOpt = userRepository.findByUsername(auth.getName());
        if(userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Caller not found among users";
        }
        var owner = userOpt.get();
        try {
            var band = entityCreatorService.fromDTONew(dto, owner);
            albumRepository.save(band);

        } catch (DTOConstraintViolationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Bad DTO: " + e.getMessage();
        }
        return "Created";
    }

    @PutMapping(value = "")
    public String putAlbum(@RequestBody AlbumDTO dto, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userOpt = userRepository.findByUsername(auth.getName());
        if(userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Caller not found among users";
        }
        var caller = userOpt.get();
        try {
                var band = entityCreatorService.fromDTOUpdate(dto, caller);
                albumRepository.save(band);
        }
        catch (DTOConstraintViolationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Bad DTO: " + e.getMessage();
        }

        return "Created";
    }


    @GetMapping(value = "")
    @Transactional
    public Page<MusicBandDTO> getAlbums(@RequestParam(required = false) String owner, HttpServletResponse response, Pageable p) {
        if(owner != null){
            var bandsOpt =  bandService.getAlbumsDTO(p);
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
