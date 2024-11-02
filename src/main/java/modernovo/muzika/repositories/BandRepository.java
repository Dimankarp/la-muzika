package modernovo.muzika.repositories;


import modernovo.muzika.model.MusicBand;

import modernovo.muzika.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface BandRepository extends JpaRepository<MusicBand, Long>, JpaSpecificationExecutor<MusicBand> {

    Page<MusicBand> getMusicBandsByOwner(User owner, Pageable pageable);


}
