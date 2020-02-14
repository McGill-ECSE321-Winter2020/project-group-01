package ca.mcgill.ecse321.petshelter.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DonationException extends RuntimeException {
    
    public DonationException(String msg) {
        super(msg);
    }
}
