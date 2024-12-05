package modernovo.muzika.services;

import modernovo.muzika.model.*;
import modernovo.muzika.model.dto.AdminRequestDTO;
import modernovo.muzika.model.dto.BatchRequestDTO;
import modernovo.muzika.repositories.AdminRequestRepository;
import modernovo.muzika.repositories.BatchRequestRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.dto.creators.AdminRequestDTOCreatorService;
import modernovo.muzika.services.dto.creators.BatchRequestDTOCreatorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class BatchRequestService {

    private final UserService userService;
    private final BatchRequestDTOCreatorService dtoCreator;
    private final BatchRequestRepository batchRequestRepository;
    private ResourceUtils resourceUtils;

    public BatchRequestService(BatchRequestRepository batchRequestRepository, BatchRequestDTOCreatorService dtoCreator,
                               UserService userService, ResourceUtils resourceUtils) {
        this.dtoCreator = dtoCreator;
        this.userService = userService;
        this.resourceUtils = resourceUtils;
        this.batchRequestRepository = batchRequestRepository;
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

}
