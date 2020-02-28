package ca.mcgill.ecse321.petshelter.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when forum services fail.
 *
 * @author mathieu
 */

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ForumException extends RuntimeException {
	public ForumException(String msg) {
		super(msg);
	}
}
