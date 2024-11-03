package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.dto.AlbumDTO;
import modernovo.muzika.dto.MusicBandDTO;
import modernovo.muzika.model.Album;
import modernovo.muzika.repositories.AlbumRepository;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.*;
import modernovo.muzika.services.entity_creators.AlbumEntityCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/albums")
public class AlbumResource extends RESTResource<Album, AlbumDTO, Long>{

    private final AlbumService albumService;

    @Autowired
    AlbumResource(AlbumService albumService) {
        super(albumService);
        this.albumService = albumService;
    }

    @GetMapping(value = "")
    @Transactional
    public Page<AlbumDTO> getAlbums(@RequestParam(required = false) String owner,
                                    @RequestParam(required = false) String name,
                                    @PageableDefault(sort = {"name"}, value = 50) Pageable p) throws IllegalServiceArgumentException {
       return albumService.getAlbumsDTO(owner, name, p);
    }


}
