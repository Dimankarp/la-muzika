package modernovo.muzika.services.dto.creators;

import modernovo.muzika.dto.AdminRequestDTO;
import modernovo.muzika.model.AdminRequest;
import org.springframework.stereotype.Service;

@Service
public class AdminRequestDTOCreatorService {
    public AdminRequestDTO toDTO(AdminRequest request) {

        var dto = new AdminRequestDTO();
        dto.setId(request.getId());
        dto.setCreationDate(request.getCreationDate());
        dto.setStatus(request.getStatus());
        dto.setSenderName(request.getUser().getUsername());
        dto.setSenderId(request.getUser().getId());
        return dto;
    }
}
