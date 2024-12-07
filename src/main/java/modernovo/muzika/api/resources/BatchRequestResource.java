package modernovo.muzika.api.resources;

import jakarta.transaction.Transactional;
import modernovo.muzika.model.dto.BatchRequestDTO;
import modernovo.muzika.services.BatchRequestService;
import modernovo.muzika.services.CallerIsNotAUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batches")
public class BatchRequestResource {

    private final BatchRequestService batchRequestService;

    BatchRequestResource(final BatchRequestService batchRequestService) {
        this.batchRequestService = batchRequestService;
    }

    @GetMapping(value = "")
    public Page<BatchRequestDTO> getRequests(Pageable p) throws CallerIsNotAUser {
        return batchRequestService.getRequests(p);

    }
}
