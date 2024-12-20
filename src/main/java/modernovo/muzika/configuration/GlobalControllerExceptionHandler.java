package modernovo.muzika.configuration;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import modernovo.muzika.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.sql.SQLException;

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

    @ExceptionHandler(EntityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleEntityConstraintViolation(EntityConstraintViolationException e) {
        return e.getMessage();
    }


    @ExceptionHandler(DatabindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleDataBindException(DatabindException e) {
        return "Failed to map objects from file to entities";
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleUnrecognizedProperty(UnrecognizedPropertyException e) {
        return "Failed to recognize property";
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public @ResponseBody String handleSQLException(SQLException e) {
        return "Something wen wrong";
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleIOException(IOException e) {
        return "Something happened to io";
    }
}