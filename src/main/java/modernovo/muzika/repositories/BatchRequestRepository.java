package modernovo.muzika.repositories;


import modernovo.muzika.model.BatchRequest;

import modernovo.muzika.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRequestRepository extends JpaRepository<BatchRequest, Long> {
    Page<BatchRequest> findAllByUser(User user, Pageable page);

}
