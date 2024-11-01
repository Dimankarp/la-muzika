package modernovo.muzika.services;

import modernovo.muzika.dto.AdminRequestDTO;
import modernovo.muzika.dto.AlbumDTO;
import modernovo.muzika.model.AdminRequest;
import modernovo.muzika.model.AdminRequestStatus;
import modernovo.muzika.model.Role;
import modernovo.muzika.repositories.AdminRequestRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.dto_creators.AdminRequestDTOCreatorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminRequestService {

    private final AdminRequestRepository adminRequestRepository;
    private final UserService userService;
    private final AdminRequestDTOCreatorService dtoCreator;
    private final UserRepository userRepository;

    public AdminRequestService(AdminRequestRepository adminRequestRepository, AdminRequestDTOCreatorService dtoCreator, UserService userService, UserRepository userRepository) {
        this.adminRequestRepository = adminRequestRepository;
        this.dtoCreator = dtoCreator;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public Page<AdminRequestDTO> getPendingRequestsDTO(Pageable page) {
        return adminRequestRepository.findAllByStatus(AdminRequestStatus.PENDING, page).map(dtoCreator::toDTO);
    }

    @Transactional
    public void acceptRequset(Long requestId) throws IllegalServiceArgumentException{
        var requestOpt = adminRequestRepository.findById(requestId);
        if (requestOpt.isPresent()) {
            var request = requestOpt.get();
            if (request.getStatus() == AdminRequestStatus.PENDING) {
                userService.addRole(request.getUser(), Role.ADMIN);
                request.setStatus(AdminRequestStatus.ACCEPTED);
            } else {
                throw new IllegalServiceArgumentException("Request with provided id is not pending");
            }
        } else {
            throw new IllegalServiceArgumentException("Request with provided id doesn't exist");
        }
    }

    @Transactional
    public void createRequest(String username) throws IllegalServiceArgumentException{
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            var requests = adminRequestRepository.findAllByUserAndStatus(user, AdminRequestStatus.PENDING);
            if (requests.isEmpty()) {
                var req = new AdminRequest();
                req.setStatus(AdminRequestStatus.PENDING);
                req.setUser(user);
                adminRequestRepository.save(req);
            } else{
                throw new IllegalServiceArgumentException("A request with the provided username already exists");
            }
        } else {
            throw new IllegalServiceArgumentException("Failed to find a user with the provided username");
        }
    }

    @Transactional
    public Optional<AdminRequestDTO> getPending(String username) throws IllegalServiceArgumentException{
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            var requests = adminRequestRepository.findAllByUserAndStatus(user, AdminRequestStatus.PENDING);
            if(!requests.isEmpty()){
                return Optional.of(dtoCreator.toDTO(requests.get(0)));
            }
            return Optional.empty();
        } else {
            throw new IllegalServiceArgumentException("Failed to find a user with the provided username");
        }
    }


}
