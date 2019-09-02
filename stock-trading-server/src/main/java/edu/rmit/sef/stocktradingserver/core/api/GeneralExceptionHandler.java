package edu.rmit.sef.stocktradingserver.core.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
//        return null;
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
//        return null;
//    }
//}