package modernovo.muzika.repositories;

import modernovo.muzika.model.Album;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {


    Page<Album> getAlbumsByOwner(User owner, Pageable pageable);


}
