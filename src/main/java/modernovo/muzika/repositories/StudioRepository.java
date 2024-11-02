package modernovo.muzika.repositories;

import modernovo.muzika.model.Studio;
import modernovo.muzika.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudioRepository extends JpaRepository<Studio, Long>, JpaSpecificationExecutor<Studio> {

    Page<Studio> getStudiosByOwner(User owner, Pageable pageable);


}
