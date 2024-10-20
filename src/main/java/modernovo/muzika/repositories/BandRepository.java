package modernovo.muzika.repositories;


import modernovo.muzika.model.MusicBand;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandRepository extends CrudRepository<MusicBand, Long> {


}
