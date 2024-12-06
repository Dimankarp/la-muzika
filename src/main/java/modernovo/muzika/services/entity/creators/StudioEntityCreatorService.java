package modernovo.muzika.services.entity.creators;

import modernovo.muzika.model.dto.StudioDTO;
import modernovo.muzika.model.Studio;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.StudioRepository;
import modernovo.muzika.services.DTOConstraintViolationException;
import modernovo.muzika.services.EntityConstraintViolationException;
import modernovo.muzika.services.entity.constraints.StudioConstraintsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class StudioEntityCreatorService implements EntityCreator<Studio, StudioDTO> {
    private final StudioRepository studioRepository;
    private final StudioConstraintsService studioConstraintsService;

    StudioEntityCreatorService(StudioRepository studioRepository, StudioConstraintsService studioConstraintsService) {
        this.studioRepository = studioRepository;
        this.studioConstraintsService = studioConstraintsService;
    }

    @Transactional
    private Studio fromDTOGeneral(StudioDTO dto) throws DTOConstraintViolationException, EntityConstraintViolationException {
        if (dto.getName() == null || dto.getName().isEmpty())
            throw new DTOConstraintViolationException("Album Name must be non empty");
        var entity = new Studio();

        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        if(!studioConstraintsService.uniqueNameAndAddress(entity)){
            throw new EntityConstraintViolationException("Unique name & address constraint broken");
        }
        return entity;
    }

    @Transactional
    public Studio fromDTONew(StudioDTO dto, User owner) throws DTOConstraintViolationException, EntityConstraintViolationException {
        if (dto == null) throw new DTOConstraintViolationException("Studio DTO is Null");
        var newEntity = fromDTOGeneral(dto);
        newEntity.setOwner(owner);
        return newEntity;

    }

    @Transactional
    public Studio fromDTOUpdate(StudioDTO dto, User caller) throws DTOConstraintViolationException, EntityConstraintViolationException {
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
