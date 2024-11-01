package modernovo.muzika.services.dto_creators;

import modernovo.muzika.dto.StudioDTO;
import modernovo.muzika.model.Studio;
import org.springframework.stereotype.Service;

@Service
public class StudioDTOCreatorService {
    public StudioDTO toDTO(Studio studio) {

        var dto = new StudioDTO();
        dto.setId(studio.getId());
        dto.setName(studio.getName());
        dto.setAddress(studio.getAddress());
        dto.setOwnerId(studio.getOwner().getId());
        dto.setOwnerName(studio.getOwner().getUsername());
        return dto;

    }
}
