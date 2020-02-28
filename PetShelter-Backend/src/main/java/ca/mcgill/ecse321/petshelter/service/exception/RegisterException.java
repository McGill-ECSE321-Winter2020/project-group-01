package ca.mcgill.ecse321.petshelter.service.exception;


import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

//exceptions on registration: username/email in use
@ResponseStatus(value = HttpStatus.CONFLICT)
public class RegisterException extends Exception {
// default ID
	private static final long serialVersionUID = 1L;

	public RegisterException(String msg) {
		super(msg);
	}
}
