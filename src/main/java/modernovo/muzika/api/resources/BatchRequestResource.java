package modernovo.muzika.api.resources;

import modernovo.muzika.model.dto.BatchRequestDTO;
import modernovo.muzika.services.BatchRequestService;
import modernovo.muzika.services.CallerIsNotAUser;

import modernovo.muzika.services.IllegalServiceArgumentException;
import modernovo.muzika.services.ResourceNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @GetMapping(value = "/files/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody byte[] getRequestFile(@PathVariable Long id) throws CallerIsNotAUser, ResourceNotFound, IllegalServiceArgumentException, IOException {
        return batchRequestService.getRequestFile(id).readAllBytes();
    }


}
