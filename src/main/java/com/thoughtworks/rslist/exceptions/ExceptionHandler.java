package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({IndexOutOfBoundsException.class,NullPointerException.class})
    public ResponseEntity<CommentError> handleIndexOutOfBoundsException(Exception ex){
        CommentError commentError = new CommentError();
        if(ex instanceof IndexOutOfBoundsException){
            commentError.setError("invalid request param");
            return ResponseEntity.status(400).body(commentError);
        }
        return ResponseEntity.badRequest().body(commentError);
    }
}
