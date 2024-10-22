package modernovo.muzika.repositories;

import jakarta.transaction.Transactional;
import modernovo.muzika.model.Album;
import modernovo.muzika.model.AuditEntry;
import modernovo.muzika.model.Studio;

import java.util.Optional;

public interface AuxiliaryEntitiesRepository {


     void saveAlbum(Album album);


     void saveStudio(Studio studio);


     void saveAuditEntry(AuditEntry entry);

    Optional<Album> findAlbumById(Long id);
    Optional<Studio> findStudiobyId(Long id);
    Optional<AuditEntry> findAuditEntryById(Long id);
}
