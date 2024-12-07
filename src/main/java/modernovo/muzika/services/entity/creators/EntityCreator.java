package modernovo.muzika.services.entity.creators;

import modernovo.muzika.model.User;
import modernovo.muzika.services.DTOConstraintViolationException;
import modernovo.muzika.services.EntityConstraintViolationException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


public interface EntityCreator<T, TDTO> {

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    T fromDTONew(TDTO dto, User owner) throws DTOConstraintViolationException, EntityConstraintViolationException;

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    T fromDTOUpdate(TDTO dto, User caller) throws DTOConstraintViolationException, EntityConstraintViolationException;
}
