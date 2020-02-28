package ca.mcgill.ecse321.petshelter.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PasswordException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public PasswordException(String msg) {
        super(msg);
    }
}
