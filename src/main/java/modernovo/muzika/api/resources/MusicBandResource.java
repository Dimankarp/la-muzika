package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.dto.MusicBandDTO;
import modernovo.muzika.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/api/bands")
public class MusicBandResource extends RESTResource<MusicBand, MusicBandDTO, Long> {

    private final BandService bandService;

    MusicBandResource(final BandService bandService) {
        super(bandService);
        this.bandService = bandService;
    }

    @GetMapping(value = "")
    @Transactional
    public Page<MusicBandDTO> getBands(@RequestParam(required = false) String owner,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String description,
                                       HttpServletResponse response,
                                       @PageableDefault(sort = {"name"}, value = 50) Pageable p) throws IllegalServiceArgumentException {
        return bandService.getBandsDTO(owner, name, description, p);
    }


}
