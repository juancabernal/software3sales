package com.proveedor.proveedor_mio.utils.exceptions;

public class BusinessException extends ApiException {

    public BusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}
