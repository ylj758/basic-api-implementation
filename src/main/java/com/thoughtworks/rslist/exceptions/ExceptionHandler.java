package com.thoughtworks.rslist.exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class ExceptionHandler {
    public static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler({IndexOutOfBoundsException.class, MethodArgumentNotValidException.class,
            InvalidIndexException.class, InvalidUserException.class})
    public ResponseEntity<CommentError> handleIndexOutOfBoundsException(Exception ex) {

        CommentError commentError = new CommentError();
        if (ex instanceof IndexOutOfBoundsException) {
            commentError.setError("invalid request param");
        }
        if (ex instanceof InvalidIndexException) {
            commentError.setError("invalid index");
        }
        if (ex instanceof MethodArgumentNotValidException) {
            commentError.setError("invalid param");
        }
        if (ex instanceof InvalidUserException) {
            commentError.setError("invalid user");
        }
        logger.error("打印日志"+ex.getMessage());
        return ResponseEntity.status(400).body(commentError);
    }
}
