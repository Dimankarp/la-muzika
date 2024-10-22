package modernovo.muzika.repositories;


import modernovo.muzika.model.MusicBand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BandRepository extends JpaRepository<MusicBand, Long>, AuxiliaryEntitiesRepository {

}
