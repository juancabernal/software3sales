package com.proveedor.proveedor_mio.utils.exceptions;

public class ValidationException extends ApiException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}
