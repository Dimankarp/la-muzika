package modernovo.muzika.api.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletResponse;
import modernovo.muzika.dto.MusicBandDTO;
import modernovo.muzika.services.BandService;
import modernovo.muzika.services.CallerIsNotAUser;
import modernovo.muzika.services.IllegalServiceArgumentException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/actions")
public class ActionsResource {


    private final BandService bandService;

    public ActionsResource(BandService bandService) {
        this.bandService = bandService;
    }

    @PostMapping("/removeByEstablishmentDate")
    MusicBandDTO removeByEstablishmentDate(@RequestBody @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm" +
            ":ss" +
            ".SSS'Z'", timezone = "UTC") ZonedDateTime date, HttpServletResponse response) throws CallerIsNotAUser {
        var opt = bandService.removeBandByEstablishmentDate(date);
        return opt.orElse(null);
    }

    @PostMapping("/getSumOfAllAlbums")
    Long getSumOfAllAlbums() {
        return bandService.sumAllAlbums();
    }

    @PostMapping("/countAllWithAlbumsCount")
    Long countAllWithAlbumsCount(@RequestBody Long albumsCount) {
        return bandService.countAllWithAlbumsCount(albumsCount);
    }

    @PostMapping("/addSingle")
    void addSingle(@RequestBody Long bandId) throws IllegalServiceArgumentException, CallerIsNotAUser {
        bandService.addSingle(bandId);
    }

    @PostMapping("/removeMember")
    void removeMember(@RequestBody Long bandId) throws IllegalServiceArgumentException, CallerIsNotAUser {
        bandService.removeMember(bandId);
    }


}
