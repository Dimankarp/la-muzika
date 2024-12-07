package modernovo.muzika.services;

import modernovo.muzika.model.dto.AdminRequestDTO;
import modernovo.muzika.model.AdminRequest;
import modernovo.muzika.model.RequestStatus;
import modernovo.muzika.model.Role;
import modernovo.muzika.repositories.AdminRequestRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.dto.creators.AdminRequestDTOCreatorService;
import modernovo.muzika.services.entity.constraints.AdminRequestConstraintsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminRequestService {

    private final AdminRequestRepository adminRequestRepository;
    private final UserService userService;
    private final AdminRequestDTOCreatorService dtoCreator;
    private final UserRepository userRepository;
    private final AdminRequestConstraintsService adminRequestConstraintsService;
    private final ResourceUtils resourceUtils;

    public AdminRequestService(AdminRequestRepository adminRequestRepository, AdminRequestDTOCreatorService dtoCreator, UserService userService, UserRepository userRepository, AdminRequestConstraintsService adminRequestConstraintsService, ResourceUtils resourceUtils) {
        this.adminRequestRepository = adminRequestRepository;
        this.dtoCreator = dtoCreator;
        this.userService = userService;
        this.userRepository = userRepository;
        this.adminRequestConstraintsService = adminRequestConstraintsService;
        this.resourceUtils = resourceUtils;
    }

    public Page<AdminRequestDTO> getPendingRequestsDTO(Pageable page) {
        return adminRequestRepository.findAllByStatus(RequestStatus.PENDING, page).map(dtoCreator::toDTO);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void acceptRequset(Long requestId) throws IllegalServiceArgumentException{
        var requestOpt = adminRequestRepository.findById(requestId);
        if (requestOpt.isPresent()) {
            var request = requestOpt.get();
            if (request.getStatus() == RequestStatus.PENDING) {
                userService.addRole(request.getUser(), Role.ADMIN);
                request.setStatus(RequestStatus.ACCEPTED);
            } else {
                throw new IllegalServiceArgumentException("Request with provided id is not pending");
            }
        } else {
            throw new IllegalServiceArgumentException("Request with provided id doesn't exist");
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createRequest(String username) throws IllegalServiceArgumentException{
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            if (!adminRequestConstraintsService.userHasPendingRequest(user)) {
                var req = new AdminRequest();
                req.setStatus(RequestStatus.PENDING);
                req.setUser(user);
                adminRequestRepository.save(req);
            } else{
                throw new IllegalServiceArgumentException("A pending request with the provided username already " +
                        "exists");
            }
        } else {
            throw new IllegalServiceArgumentException("Failed to find a user with the provided username");
        }
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Optional<AdminRequestDTO> getMyRequest() throws IllegalServiceArgumentException, CallerIsNotAUser {
        var caller = resourceUtils.getCaller();
        return getPending(caller.getUsername());
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Optional<AdminRequestDTO> getPending(String username) throws IllegalServiceArgumentException{
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            var requests = adminRequestRepository.findAllByUserAndStatus(user, RequestStatus.PENDING);
            if(!requests.isEmpty()){
                return Optional.of(dtoCreator.toDTO(requests.get(0)));
            }
            return Optional.empty();
        } else {
            throw new IllegalServiceArgumentException("Failed to find a user with the provided username");
        }
    }


}
