package modernovo.muzika.api.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.Ownable;
import modernovo.muzika.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

public abstract class RESTResource<T extends Ownable, TDTO, ID extends Serializable> {

    private Logger logger = LoggerFactory.getLogger(RESTResource.class);

    private final EntityService<T, TDTO, ID> entityService;

    public RESTResource(EntityService<T, TDTO, ID> entityService) {
        this.entityService = entityService;
    }

    @PostMapping(value = "")
    public String postResource(@RequestBody TDTO dto, HttpServletResponse response) throws CallerIsNotAUser, DTOConstraintViolationException, EntityConstraintViolationException {
        entityService.createEntity(dto);
        return "Created";
    }

    @PutMapping(value = "")
    public String putResource(@RequestBody TDTO dto, HttpServletResponse response) throws CallerIsNotAUser, DTOConstraintViolationException, EntityConstraintViolationException {
        entityService.updateEntity(dto);
        return "Updated";
    }

    @DeleteMapping(value = "/{id}")
    public String deleteResource(@PathVariable ID id, HttpServletResponse response) throws AuthorizationException, ResourceNotFound,
            CallerIsNotAUser {
        entityService.deleteEntity(id);
        return "Deleted";
    }

}
