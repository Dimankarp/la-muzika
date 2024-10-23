package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.MusicBandDTO;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.security.UserDetailsServiceImpl;
import modernovo.muzika.services.BandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/bands")
public class MusicBandResource {

    private final Logger logger = LoggerFactory.getLogger(MusicBandResource.class);
    private final UserRepository userRepository;
    private final BandService bandService;
    private final BandRepository bandRepository;

    MusicBandResource(final UserRepository userRepository, final BandService bandService, BandRepository bandRepository) {
        this.userRepository = userRepository;
        this.bandService = bandService;
        this.bandRepository = bandRepository;
    }

    @PostMapping(value = "")
    public void postBand(@RequestBody MusicBandDTO band, HttpServletResponse response) {
        if(band == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @GetMapping(value = "")
    @Transactional
    public Page<MusicBandDTO> getBands(@RequestParam(required = false) String owner, HttpServletResponse response, Pageable p) {
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
