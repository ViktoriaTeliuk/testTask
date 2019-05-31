package rest.files.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rest.files.to.ResponseAnswer;
import rest.files.to.ResponseTo;
import rest.files.util.exceptions.ErrorType;

import javax.servlet.http.HttpServletRequest;


@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseTo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, ErrorType.APP_ERROR);
    }

    private ResponseTo logAndGetErrorInfo(HttpServletRequest req, Exception e, ErrorType errorType) {
        return new ResponseTo(ResponseAnswer.ERROR, String.format("%s: %s: %s",req.getRequestURL(), errorType, e.getMessage()));
    }
}