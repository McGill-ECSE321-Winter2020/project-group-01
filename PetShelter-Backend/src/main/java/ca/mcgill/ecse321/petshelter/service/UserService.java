package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public User addUser(UserDTO user) {
		if (user.getPassword() == null) {
			throw new RegisterException("Password can't be null.");
		}
		String validationError = isUserDtoValid(user);
		if (validationError != null) {
			System.out.println(validationError);
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
		user1.setEmail(user.getEmail());
		user1.setUserName(user.getUsername());
		user1.setUserType(user.getUserType());
		String token = jwtTokenProvider.createToken(user1.getUserName());
		user1.setApiToken(token);
		// save it
		userRepository.save(user1);
		// Send email
		emailingService.userCreationEmail(user.getEmail(), user.getUsername(), token);
		return user1;
	}

	// method that only checks if a user could be logged in
	public User loginUser(UserDTO user) throws LoginException {
		// if no user is found by its username, it does not exist
		User user1 = userRepository.findUserByUserName(user.getUsername());
		if (user1 == null) {
			throw new LoginException("Username not found");
		}
		// if the password doesnt match the saved one
		String expectedPW = user1.getPassword();
		if (!passwordEncoder.matches(user.getPassword(), expectedPW)) {
			throw new LoginException("Incorrect password");
		}
		// if the user has not verified their account through email
		if (!user1.isIsEmailValidated())
			throw new LoginException("Account not verified");
		return user1;
	}

	public ResponseEntity<?> changeUserPassword(PasswordChangeDTO passwordDto) {
		// check if new password is valid
		String constraintViolation = isPasswordChangeValid(passwordDto);
		if (constraintViolation != null) {
			return new ResponseEntity<>(constraintViolation, HttpStatus.BAD_REQUEST);
		}
		User user = userRepository.findUserByUserName(passwordDto.getUserName());
		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		// the old password must be correct 
		if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
		userRepository.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Generates a strong temporary password to be used in case of password reset.
	 * 
	 * @return
	 */
	public String generateRandomPassword() {
		String upperCaseLetters = RandomStringUtils.random(1, 65, 90, true, true);
		String lowerCaseLetters = RandomStringUtils.random(1, 97, 122, true, true);
		String numbers = RandomStringUtils.randomNumeric(1);
		String totalChars = RandomStringUtils.randomAlphanumeric(6);
		String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(totalChars);
		List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
		Collections.shuffle(pwdChars);
		return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
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
	
	public ResponseEntity<?> deleteUser(UserDTO userDTO) {
		User user = userRepository.findUserByUserName(userDTO.getUsername());
		try {
			userRepository.deleteById(user.getId());
		} catch (RuntimeException e) {
			//todo i dont know what to return in case of failure
			//return ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
