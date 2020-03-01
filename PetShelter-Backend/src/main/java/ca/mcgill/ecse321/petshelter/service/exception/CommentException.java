package ca.mcgill.ecse321.petshelter.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when comment services fail.
 *
 * @author mathieu
 */

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CommentException extends Exception {
	public CommentException(String msg) {
		super(msg);
	}
}