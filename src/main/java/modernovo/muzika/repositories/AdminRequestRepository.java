package modernovo.muzika.repositories;

import modernovo.muzika.model.AdminRequest;
import modernovo.muzika.model.RequestStatus;
import modernovo.muzika.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long> {

    Page<AdminRequest> findAllByStatus(RequestStatus status, Pageable pageable);
    List<AdminRequest> findAllByUserAndStatus(User user, RequestStatus status);

}
