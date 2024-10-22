package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.MusicBandDTO;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.BandService;
import modernovo.muzika.services.DTOCreatorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/bands")
public class MusicBandResource {

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
    public List<MusicBandDTO> getBands(@RequestParam(required = false) String owner, HttpServletResponse response) {
        if(owner != null){
            var bandsOpt =  bandService.getBandDTObyUsername(owner);
            if(bandsOpt.isEmpty()){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                return bandsOpt.get();
            }
        }
        return bandService.getBandDTO();
    }


}
