package ca.mcgill.ecse321.petshelter.service;


import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

//exceptions on registration: username/email in use
@ResponseStatus(value = HttpStatus.CONFLICT)
public class RegisterException extends RuntimeException {
// default ID
	private static final long serialVersionUID = 1L;

	public RegisterException(String msg) {
		super(msg);
	}
}
