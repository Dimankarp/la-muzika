package modernovo.muzika.configuration;

import modernovo.muzika.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class GlobalControllerExceptionHandler {
    @ExceptionHandler(IllegalServiceArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleIllegalServiceArgumentException(IllegalServiceArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(CallerIsNotAUser.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public @ResponseBody String handleCallerIsNotAUser(CallerIsNotAUser e) {
        return e.getMessage();
    }

    @ExceptionHandler(ResourceNotFound.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public @ResponseBody String handleNotFound(ResourceNotFound e) {
        return e.getMessage();
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public @ResponseBody String handleAuthException(AuthorizationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(DTOConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleDTOConstraintViolation(DTOConstraintViolationException e) {
        return e.getMessage();
    }
}