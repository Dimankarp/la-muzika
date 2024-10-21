package modernovo.muzika.repositories;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.AdminRequest;
import modernovo.muzika.model.Album;
import modernovo.muzika.model.AuditEntry;
import modernovo.muzika.model.Studio;
import org.springframework.stereotype.Repository;

@Repository
public class AuxiliaryEntitiesRepository {

    private final EntityManager entityManager;

    AuxiliaryEntitiesRepository(
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
    public void saveAdminRequest(AdminRequest request) {
        entityManager.persist(request);
    }


}


