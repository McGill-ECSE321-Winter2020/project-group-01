package ca.mcgill.ecse321.petshelter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.JWTTokenProvider;
import ca.mcgill.ecse321.petshelter.service.RegisterException;
import ca.mcgill.ecse321.petshelter.service.UserService;

/**
 * @author louis User controller class - allows for creation of users, login of
 *         users, and email validation of users.
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {

	// Declaration of needed services.
	@Autowired
	private UserService userService;

	@Autowired
	private JWTTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Creates a user account. The Request body is a UserDTO aka email, password
	 * username and UserType are provided. The method also validates if the
	 * username/email are already in use and if the any of the input is empty. Also
	 * checks if the email is an email. Upon registration, an email with an API
	 * token is sent to the user's email.
	 * 
	 * @param user UserDTO
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody(required = true) UserDTO user) {
		// check if input is valid (email is an email, email and username are not empty)
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
		for (ConstraintViolation<UserDTO> violation : violations) {
			return new ResponseEntity<>(violation.getMessage(), HttpStatus.BAD_REQUEST);
		}
		try {
			User user1 = userService.addUser(user);
			user.setUserType(user1.getUserType());
			user.setToken(user1.getApiToken());
			return new ResponseEntity<>(user, HttpStatus.CREATED); // return created HTTP status
		}
		// If one cannot log into the sender's email or if the message fails to be sent
		catch (MailException x) {
			return new ResponseEntity<>(x.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// If the Email/username is already in use
		catch (RegisterException x) {
			return new ResponseEntity<>(x.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Checks if the user can be logged in. User's email must be verified, and the
	 * account must exist.
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDTO user) {
		try {
			User ue = userService.loginUser(user);
			user.setUserType(ue.getUserType());
			user.setEmail(ue.getEmail());
			user.setUsername(ue.getUserName());
			user.setPassword(null);
			user.setToken(ue.getApiToken());
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (ca.mcgill.ecse321.petshelter.service.LoginException ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Verification of account through email.
	 * 
	 * @param token
	 * @return
	 */
	@GetMapping("/regitrationConfirmation")
	public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {
		// find a user by the verif. token; if none is found, the user does not exist
		User user = userRepo.findUserByApiToken(token);
		if (user == null) {
			return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
		}
		if (user.isIsEmailValidated()) {
			return new ResponseEntity<>("Account already validated", HttpStatus.BAD_REQUEST);
		}
		// check if the token is expired
		if (jwtTokenProvider.validateToken(token)) {
			return new ResponseEntity<>("Token expired", HttpStatus.UNAUTHORIZED);
		}
		// user is now verified
		user.setIsEmailValidated(true);
		userRepo.save(user);
		return new ResponseEntity<>("Account validated", HttpStatus.OK);
	}

	/**
	 * Resets the password and emails the user a link with the new password.
	 * 
	 * @param email
	 * @return
	 */
	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody UserDTO email) {
		User ue = userRepo.findUserByEmail(email.getEmail());
		// if no user is found with that email, bad request
		if (ue == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		// if the account is not validated, the password cant be changed
		if (!ue.isIsEmailValidated()) {
			return new ResponseEntity<>("Account not verified", HttpStatus.BAD_REQUEST);
		}
		// generate a random password for the user to log in and change later
		String tempPw = userService.generateRandomPassword();
		ue.setPassword(passwordEncoder.encode(tempPw));
		userRepo.save(ue);
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(ue.getEmail());
		msg.setSubject("Pet shelter password reset");
		msg.setText("Here is your temporary password " + tempPw);
		try {
			javaMailSender.send(msg);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (MailException x) {
			return new ResponseEntity<>(x.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Allows users to change passwords.
	 * 
	 * @param passwords
	 * @return
	 */
	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO passwords) {
		// check if new password is valid
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PasswordChangeDTO>> violations = validator.validate(passwords);
		for (ConstraintViolation<PasswordChangeDTO> violation : violations) {
			return new ResponseEntity<>(violation.getMessage(), HttpStatus.BAD_REQUEST);
		}
		User user = userRepo.findUserByUserName(passwords.getUserName());
		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		if (!passwordEncoder.matches(passwords.getOldPassword(), user.getPassword())) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		user.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
		userRepo.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Deletes a user. The person making the request must be an admin.
	 * 
	 * @param userName
	 * @return
	 */
	@DeleteMapping("/{userName}")
	public ResponseEntity<?> deleteUser(@PathVariable String userName, @RequestBody UserDTO admin) {
		User requester = userRepo.findUserByUserName(admin.getUsername());
		if (requester != null && requester.getUserType().equals(UserType.ADMIN)) {
			// find the deleted user by username
			User user = userRepo.findUserByUserName(userName);
			if (user != null) {
				userRepo.delete(user);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if the requester is not an admin
		}

	}

	/**
	 * Returns a specified user's information. The requester must be an admin.
	 * 
	 * @param userName
	 * @param admin
	 * @return
	 */
	@GetMapping("/{username}")
	public ResponseEntity<?> getUser(@PathVariable String userName, @RequestBody UserDTO admin) {
		User requester = userRepo.findUserByUserName(admin.getUsername());
		if (requester != null && requester.getUserType().equals(UserType.ADMIN)) {
			// find a user by username
			User user = userRepo.findUserByUserName(userName);
			if (user != null) {
				return new ResponseEntity<>(userToDto(user), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		} else { // if user isnt an admin
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Updates a specified user's information. Only the user tied to the account can
	 * make the request.
	 * 
	 * @param userName
	 * @param admin
	 * @return
	 */
	@PutMapping("/{username}")
	// note: the username and email cannot be changed
	public ResponseEntity<?> updateUser(@PathVariable String userName, @RequestBody UserDTO userDto) {
		// find a user by username
		User user = userRepo.findUserByUserName(userName);
		if (user != null) {
			// actually, only the picture is allowed to be updated (design decision)
			if (userDto.getPicture() != null) {
				user.setPicture(userDto.getPicture());
			}
			return new ResponseEntity<>(userToDto(user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Returns all users' information. The requester must be an admin.
	 * 
	 * @param userName
	 * @param admin
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getUsers(@RequestBody UserDTO admin) {
		User requester = userRepo.findUserByUserName(admin.getUsername());
		if (requester != null && requester.getUserType().equals(UserType.ADMIN)) {
			List<User> users = new ArrayList<>();
			try {
				Iterable<User> usersIterable = userRepo.findAll();
				usersIterable.forEach(users::add);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		} else { // if user isnt an admin
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// converts a user into a userdto
	private UserDTO userToDto(User user) {
		UserDTO userDto = new UserDTO();
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUserName());
		userDto.setUserType(user.getUserType());
		userDto.setPicture(user.getPicture());
		return userDto;
	}
}
