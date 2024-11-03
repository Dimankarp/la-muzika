package modernovo.muzika.api.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.transaction.Transactional;
import modernovo.muzika.dto.MusicBandDTO;
import modernovo.muzika.repositories.BandRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actions")
public class ActionsResource {


    private final BandRepository bandRepository;

    public ActionsResource(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

//    @PostMapping("/removeByEstablishmentDate")
//    @Transactional
//    MusicBandDTO removeByEstablishmentDate(@RequestBody @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm" +
//            ":ss" +
//            ".SSS'Z'", timezone = "UTC") String date) {
//
//
//    }

}
