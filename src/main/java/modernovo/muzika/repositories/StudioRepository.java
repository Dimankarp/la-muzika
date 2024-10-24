package modernovo.muzika.repositories;

import modernovo.muzika.model.Album;
import modernovo.muzika.model.Studio;
import modernovo.muzika.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudioRepository extends JpaRepository<Studio, Long> {

    Page<Studio> getStudiosByOwner(User owner, Pageable pageable);


}
