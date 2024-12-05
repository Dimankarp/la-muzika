package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.dto.StudioDTO;
import modernovo.muzika.model.Studio;
import modernovo.muzika.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/studios")
public class StudioResource extends RESTResource<Studio, StudioDTO, Long> {

    private final StudioService studioService;

    StudioResource(StudioService studioService) {
        super(studioService);
        this.studioService = studioService;
    }

    @GetMapping(value = "")
    @Transactional
    public Page<StudioDTO> getStudios(@RequestParam(required = false) String owner,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String address,
                                      HttpServletResponse response,
                                      @PageableDefault(sort = {"name"}, value = 50) Pageable p) throws IllegalServiceArgumentException {
        return studioService.getStudiosDTO(owner, name, address, p);
    }


}
