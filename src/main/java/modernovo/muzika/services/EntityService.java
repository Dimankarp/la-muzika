package modernovo.muzika.services;

import modernovo.muzika.model.Ownable;
import modernovo.muzika.services.entity.creators.EntityCreator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public abstract class EntityService<T extends Ownable, TDTO, ID extends Serializable> {

    private final ResourceUtils resourceUtils;
    private final JpaRepository<T, ID> repository;
    private final EntityCreator<T, TDTO> entityCreatorService;

    protected EntityService(ResourceUtils resourceUtils, JpaRepository<T, ID> repository, EntityCreator<T, TDTO> entityCreatorService) {
        this.resourceUtils = resourceUtils;
        this.repository = repository;
        this.entityCreatorService = entityCreatorService;
    }

    public T createEntity(TDTO dto) throws DTOConstraintViolationException, CallerIsNotAUser, EntityConstraintViolationException {
        var owner = resourceUtils.getCaller();
        var resource = entityCreatorService.fromDTONew(dto, owner);
        repository.save(resource);
        return resource;
    }

    public void updateEntity(TDTO dto) throws DTOConstraintViolationException, CallerIsNotAUser, EntityConstraintViolationException {
        var caller = resourceUtils.getCaller();
        var resource = entityCreatorService.fromDTOUpdate(dto, caller);
        repository.save(resource);
    }

    public void deleteEntity(ID id) throws CallerIsNotAUser, ResourceNotFound, AuthorizationException {
        var caller = resourceUtils.getCaller();
        var resourceOpt = repository.findById(id);
        if (resourceOpt.isEmpty()) {
            throw new ResourceNotFound("Failed to find resource with id " + id);
        }
        var resource = resourceOpt.get();
        if (resource.getOwner().getUsername().equals(caller.getUsername())) {
            repository.delete(resource);
        } else {
            throw new AuthorizationException("Caller doesn't own resource requested for deletion");
        }

    }

}
