package modernovo.muzika.services.entity.creators;

import modernovo.muzika.model.User;
import modernovo.muzika.services.DTOConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;


public interface EntityCreator<T, TDTO> {

    @Transactional
    T fromDTONew(TDTO dto, User owner) throws DTOConstraintViolationException;

    @Transactional
    T fromDTOUpdate(TDTO dto, User caller) throws DTOConstraintViolationException;
}
