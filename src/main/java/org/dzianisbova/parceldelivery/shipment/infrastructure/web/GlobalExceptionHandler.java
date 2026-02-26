package org.dzianisbova.parceldelivery.shipment.infrastructure.web;

import org.dzianisbova.parceldelivery.shipment.application.ShipmentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ShipmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ShipmentNotFoundException ex) {
        return new ErrorResponse(404, ex.getMessage());
    }
}
