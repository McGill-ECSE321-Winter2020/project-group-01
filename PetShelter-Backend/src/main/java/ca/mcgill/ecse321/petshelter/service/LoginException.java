package ca.mcgill.ecse321.petshelter.service;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

//exception when logging in: wrong pw, username not found, account not verified
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class LoginException extends RuntimeException {
	public LoginException(String msg) {
		super(msg);
	}
}
