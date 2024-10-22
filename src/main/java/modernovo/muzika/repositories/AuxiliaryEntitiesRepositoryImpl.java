package modernovo.muzika.repositories;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.Album;
import modernovo.muzika.model.AuditEntry;
import modernovo.muzika.model.Studio;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuxiliaryEntitiesRepositoryImpl implements AuxiliaryEntitiesRepository {

    private final EntityManager entityManager;

    AuxiliaryEntitiesRepositoryImpl(
            EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveAlbum(Album album) {
        entityManager.persist(album);
    }

    @Transactional
    public void saveStudio(Studio studio) {
        entityManager.persist(studio);
    }

    @Transactional
    public void saveAuditEntry(AuditEntry entry) {
        entityManager.persist(entry);
    }

    @Transactional
    public Optional<Album> findAlbumById(Long id) {
        var album = entityManager.find(Album.class, id);
        return Optional.ofNullable(album);
    }

    @Transactional
    public Optional<Studio> findStudiobyId(Long id) {
        var studio = entityManager.find(Studio.class, id);
        return Optional.ofNullable(studio);
    }

    @Transactional
    public Optional<AuditEntry> findAuditEntryById(Long id) {
        var auditEntry = entityManager.find(AuditEntry.class, id);
        return Optional.ofNullable(auditEntry);
    }


}


