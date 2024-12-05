package modernovo.muzika.services.dto.creators;

import modernovo.muzika.model.dto.AlbumDTO;
import modernovo.muzika.model.Album;
import org.springframework.stereotype.Service;

@Service
public class AlbumDTOCreatorService {
    public AlbumDTO toDTO(Album album) {

        var dto = new AlbumDTO();
        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setTracks(album.getTracks());
        dto.setOwnerId(album.getOwner().getId());
        dto.setOwnerName(album.getOwner().getUsername());
        return dto;
    }
}
