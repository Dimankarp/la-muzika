package modernovo.muzika.services.entity.constraints;

import modernovo.muzika.model.RequestStatus;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.AdminRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional(propagation = Propagation.REQUIRED)

public class AdminRequestConstraintsService {

    private final AdminRequestRepository adminRequestRepository;
    public AdminRequestConstraintsService(AdminRequestRepository adminRequestRepository) {
        this.adminRequestRepository = adminRequestRepository;
    }
    @Transactional(readOnly = true)
    public boolean userHasPendingRequest(User user){
        return !adminRequestRepository.findAllByUserAndStatus(user, RequestStatus.PENDING).isEmpty();
    }

}
