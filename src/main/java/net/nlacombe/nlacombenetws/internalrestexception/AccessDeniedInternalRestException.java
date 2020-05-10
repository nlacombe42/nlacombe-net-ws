package net.nlacombe.nlacombenetws.internalrestexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessDeniedInternalRestException extends RuntimeException {

    public AccessDeniedInternalRestException() {
    }

    public AccessDeniedInternalRestException(String message) {
        super(message);
    }

    public AccessDeniedInternalRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
