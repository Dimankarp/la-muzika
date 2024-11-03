package modernovo.muzika.services.entity_creators;

import modernovo.muzika.dto.AlbumDTO;
import modernovo.muzika.model.Album;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.AlbumRepository;
import modernovo.muzika.services.DTOConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class AlbumEntityCreatorService implements EntityCreator<Album, AlbumDTO> {

    private final AlbumRepository albumRepository;

    AlbumEntityCreatorService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }


    private Album fromDTOGeneral(AlbumDTO dto) throws DTOConstraintViolationException {
        if (dto.getName() == null || dto.getName().isEmpty())
            throw new DTOConstraintViolationException("Album Name must be non empty");

        if (dto.getTracks() != null && dto.getTracks() <= 0)
            throw new DTOConstraintViolationException("Music band must contain at least one participant");

        var entity = new Album();

        entity.setName(dto.getName());
        entity.setTracks(dto.getTracks());
        return entity;
    }

    @Transactional
    public Album fromDTONew(AlbumDTO dto, User owner) throws DTOConstraintViolationException {
        if (dto == null) throw new DTOConstraintViolationException("Album DTO is Null");
        var newEntity = fromDTOGeneral(dto);
        newEntity.setOwner(owner);
        return newEntity;

    }

    @Transactional
    public Album fromDTOUpdate(AlbumDTO dto, User caller) throws DTOConstraintViolationException {
        if (dto == null) throw new DTOConstraintViolationException("Album DTO is Null");
        if (dto.getId() == null)
            throw new DTOConstraintViolationException("FromDTOUpdate is called with null album id");
        if (dto.getOwnerId() == null) throw new DTOConstraintViolationException("Music band DTO must have an owner id");

        var entityOpt = albumRepository.findById(dto.getId());
        if (entityOpt.isEmpty()) {
            throw new DTOConstraintViolationException("FromDTOUpdate is called with non existing album");
        }

        var persistedEntity = entityOpt.get();

        if (!persistedEntity.getOwner().getId().equals(caller.getId())) {
            throw new DTOConstraintViolationException("FromDTOUpdate is called for DTO owned by different owner");
        }

        if (!Objects.equals(dto.getOwnerId(), caller.getId())) {
            throw new DTOConstraintViolationException("From DTO for regular update is called by a non-owner of DTO");
        }

        var updatingEntity = fromDTOGeneral(dto);
        updatingEntity.setId(persistedEntity.getId());
        updatingEntity.setOwner(caller);
        return updatingEntity;

    }
}
