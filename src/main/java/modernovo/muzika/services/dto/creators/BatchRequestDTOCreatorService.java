package modernovo.muzika.services.dto.creators;

import modernovo.muzika.model.AdminRequest;
import modernovo.muzika.model.BatchRequest;
import modernovo.muzika.model.dto.AdminRequestDTO;
import modernovo.muzika.model.dto.BatchRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class BatchRequestDTOCreatorService {
    public BatchRequestDTO toDTO(BatchRequest request) {

        var dto = new BatchRequestDTO();
        dto.setId(request.getId());
        dto.setCreationDate(request.getCreationDate());
        dto.setStatus(request.getStatus());
        dto.setSenderName(request.getUser().getUsername());
        dto.setSenderId(request.getUser().getId());
        dto.setAddedCount(request.getAddedCount());
        return dto;
    }
}
