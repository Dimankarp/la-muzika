package modernovo.muzika.services.entity_creators;

import modernovo.muzika.dto.StudioDTO;
import modernovo.muzika.model.Studio;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.StudioRepository;
import modernovo.muzika.services.DTOConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class StudioEntityCreatorService {
    private final StudioRepository studioRepository;

    StudioEntityCreatorService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    private Studio fromDTOGeneral(StudioDTO dto) throws DTOConstraintViolationException {
        if (dto.getName() == null || dto.getName().isEmpty())
            throw new DTOConstraintViolationException("Album Name must be non empty");
        var entity = new Studio();

        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        return entity;
    }

    @Transactional
    public Studio fromDTONew(StudioDTO dto, User owner) throws DTOConstraintViolationException {
        if (dto == null) throw new DTOConstraintViolationException("Studio DTO is Null");
        var newEntity = fromDTOGeneral(dto);
        newEntity.setOwner(owner);
        return newEntity;

    }

    @Transactional
    public Studio fromDTOUpdate(StudioDTO dto, User caller) throws DTOConstraintViolationException {
        if (dto == null) throw new DTOConstraintViolationException("Studio DTO is Null");
        if (dto.getId() == null)
            throw new DTOConstraintViolationException("FromDTOUpdate is called with null studio id");
        if (dto.getOwnerId() == null)
            throw new DTOConstraintViolationException("Music band DTO must have an owner id");

        var entityOpt = studioRepository.findById(dto.getId());
        if (entityOpt.isEmpty()) {
            throw new DTOConstraintViolationException("FromDTOUpdate is called with non existing studio");
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
