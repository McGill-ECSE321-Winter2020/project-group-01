package ca.mcgill.ecse321.petshelter.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DonationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DonationException(String msg) {
        super(msg);
    }
}
