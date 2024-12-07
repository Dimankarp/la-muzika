package modernovo.muzika.services.entity.constraints;

import modernovo.muzika.model.RequestStatus;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.AdminRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service

public class AdminRequestConstraintsService {

    private final AdminRequestRepository adminRequestRepository;
    public AdminRequestConstraintsService(AdminRequestRepository adminRequestRepository) {
        this.adminRequestRepository = adminRequestRepository;
    }
    @Transactional(propagation=Propagation.MANDATORY, readOnly = true, isolation = Isolation.READ_COMMITTED)
    public boolean userHasPendingRequest(User user){
        return !adminRequestRepository.findAllByUserAndStatus(user, RequestStatus.PENDING).isEmpty();
    }

}
