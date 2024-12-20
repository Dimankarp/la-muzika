package modernovo.muzika.services;

import modernovo.muzika.model.*;
import modernovo.muzika.model.dto.BatchRequestDTO;
import modernovo.muzika.repositories.BatchRequestRepository;
import modernovo.muzika.services.dto.creators.BatchRequestDTOCreatorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Objects;

@Service
public class BatchRequestService {

    private final UserService userService;
    private final BatchRequestDTOCreatorService dtoCreator;
    private final BatchRequestRepository batchRequestRepository;
    private final ResourceUtils resourceUtils;
    private final MinIOService minIOService;


    public BatchRequestService(BatchRequestRepository batchRequestRepository, BatchRequestDTOCreatorService dtoCreator,
                               UserService userService, ResourceUtils resourceUtils, MinIOService minIOService) {
        this.dtoCreator = dtoCreator;
        this.userService = userService;
        this.resourceUtils = resourceUtils;
        this.batchRequestRepository = batchRequestRepository;
        this.minIOService = minIOService;
    }

    public Page<BatchRequestDTO> getRequests(Pageable page) throws CallerIsNotAUser {
        var caller = resourceUtils.getCaller();
        if (userService.hasAdminRights(caller)) {
            return batchRequestRepository.findAll(page).map(dtoCreator::toDTO);
        } else {
            return batchRequestRepository.findAllByUser(caller, page).map(dtoCreator::toDTO);
        }
    }

    public BatchRequest createRequest(User creator) {
        var request = new BatchRequest();
        request.setUser(creator);
        request.setStatus(RequestStatus.PENDING);
        var managed_request = batchRequestRepository.save(request);
        batchRequestRepository.flush();
        return managed_request;
    }

    @Transactional
    public InputStream getRequestFile(Long requestId) throws CallerIsNotAUser, ResourceNotFound, IllegalServiceArgumentException {
        var caller = resourceUtils.getCaller();
        var requestOpt = batchRequestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new ResourceNotFound("Requested request doesn't exist");
        }
        var request = requestOpt.get();
        if (!Objects.equals(request.getUser().getId(), caller.getId()) && !userService.hasAdminRights(caller)) {
            throw new AccessDeniedException("Not enough permission to access request");
        }
        if (request.getStatus() != RequestStatus.ACCEPTED) {
            throw new IllegalServiceArgumentException("Requested request is not accepted");
        }

        return minIOService.getFile(request.getId().toString());
    }

}
