package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.RegisterException;
import ca.mcgill.ecse321.petshelter.service.extrafeatures.EmailingService;
import ca.mcgill.ecse321.petshelter.service.extrafeatures.JWTTokenProvider;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Service to handle login and registration of users.
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTTokenProvider jwtTokenProvider;

	private EmailingService emailingService;

	public UserService(EmailingService emailingService) {
		this.emailingService = emailingService;
	}

	/**
	 * Creates a user if the input is valid and sends an email to the specified
	 * email address.
	 *
	 * @param user
	 * @return
	 */

	public UserDTO createUser(UserDTO user) throws RegisterException {
		if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
			throw new RegisterException("Password can't be null.");
		}
		String validationError = isUserDtoValid(user);
		if (validationError != null) {
			throw new RegisterException(validationError);
		}
		// check that the email and username are unique
		if (userRepository.findUserByEmail(user.getEmail()) != null)
			throw new RegisterException("Email is already taken.");
		if (userRepository.findUserByUserName(user.getUsername()) != null)
			throw new RegisterException("Username is already taken.");
		// create the user and set its attributes
		User user1 = new User();
		user1.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println("pipcouil");
		user1.setEmail(user.getEmail());
		user1.setUserName(user.getUsername());
		user1.setUserType(user.getUserType());
		String token = jwtTokenProvider.createToken(user1.getUserName());
		user1.setApiToken(token);
		// if the user is an admin, no need to validate (there is only one admin)
		if (user.getUserType().equals(UserType.ADMIN)) {
			user1.setIsEmailValidated(true);
		}
		// otherwise a validation email must be sent to the user
		else {
			emailingService.userCreationEmail(user.getEmail(), user.getUsername(), token);
		}
		// save it
		userRepository.save(user1);
		return userToDto(user1);
	}



	/**
	 * Validates the UserDto it is given. Email must be an email, and fields must
	 * not be empty. Returns null if no error is found. Returns an error message if
	 * a violation is found.
	 *
	 * @param userDto
	 * @return
	 */
	private String isUserDtoValid(UserDTO userDto) {
		// check if input is valid (email is an email, email and username are not empty)
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDto);
		for (ConstraintViolation<UserDTO> violation : violations) {
			return violation.getMessage();
		}
		return null;
	}

	/**
	 * Validates the PasswordChangeDto it is given. The new password must satisfy
	 * constraitns. Returns null if no error is found.
	 *
	 * @param passwordChangeDto
	 * @return errors if any
	 */
	private String isPasswordChangeValid(PasswordChangeDTO passwordChangeDto) {
		// check if input is valid (new password satisfies constraints)
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PasswordChangeDTO>> violations = validator.validate(passwordChangeDto);
		for (ConstraintViolation<PasswordChangeDTO> violation : violations) {
			return violation.getMessage();
		}
		return null;
	}

	/**
	 * This is the update method; only the password can be updated (design
	 * decision).
	 *
	 * @param passwordDto
	 * @return
	 */
	public UserDTO updateUser(PasswordChangeDTO passwordDto) throws IllegalArgumentException {
		if (passwordDto.getNewPassword() == null || passwordDto.getNewPassword().trim().length() == 0) {
			throw new IllegalArgumentException("Password cannot be null.");
		}
		// check if new password is valid
		String constraintViolation = isPasswordChangeValid(passwordDto);
		if (constraintViolation != null) {
			throw new IllegalArgumentException(constraintViolation);
		}
		User user = userRepository.findUserByUserName(passwordDto.getUserName());
		if (user == null) {
			throw new IllegalArgumentException("No user was found");
		}
		// the old password must be correct
		if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Wrong password");
		}
		user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
		userRepository.save(user);
		return userToDto(user);
	}

	/**
	 * Deletes a user. Returns false if the user could not be deleted.
	 *
	 * @param userDTO
	 * @return
	 */
	public boolean deleteUser(String username) {
		User user = userRepository.findUserByUserName(username);
		try {
			userRepository.deleteById(user.getId());
		} catch (RuntimeException e) {
			return false;
		}
		return true;
	}

	// converts a user into a userdto
	static UserDTO userToDto(User user) {
		UserDTO userDto = new UserDTO();
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUserName());
		userDto.setUserType(user.getUserType());
		userDto.setPicture(user.getPicture());
		userDto.setToken(user.getApiToken());
		return userDto;
	}
}
