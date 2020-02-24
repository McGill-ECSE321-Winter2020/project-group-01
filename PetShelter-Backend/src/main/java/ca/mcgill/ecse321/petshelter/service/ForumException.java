package ca.mcgill.ecse321.petshelter.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ForumException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ForumException(String msg) {
        super(msg);
    }
}
