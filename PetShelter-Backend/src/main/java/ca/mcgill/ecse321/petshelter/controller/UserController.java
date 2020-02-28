package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
	private EmailingService emailingService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// converts a user into a userdto
	public static UserDTO userToDto(User user) {
		UserDTO userDto = new UserDTO();
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUserName());
		userDto.setUserType(user.getUserType());
		userDto.setPicture(user.getPicture());
		return userDto;
	}
	
	/**
	 * Creates a user account. The Request body is a UserDTO aka email, password
	 * username and UserType are provided. The method also validates if the
	 * username/email are already in use and if the any of the input is empty. Also
	 * checks if the email is an email. Upon registration, an email with an API
	 * token is sent to the user's email.
	 *
	 * @param user UserDTO
	 * @return user's parameter
	 */
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody(required = true) UserDTO user) {
		try {
			UserDTO user1 = userService.createUser(user);
			return new ResponseEntity<>(user1, HttpStatus.CREATED); // return created HTTP status
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
	 * Verification of account through email.
	 *
	 * @param token randomly generated token
	 * @return check if it was clicked within 24h
	 */
	@GetMapping("/registrationConfirmation")
	public ResponseEntity<?> confirmRegistration(@RequestHeader String token) {
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
	 * @param email user's email to reset password
	 * @return confirmation that email has been sent
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
		try {
			emailingService.userForgotPasswordEmail(ue.getEmail(), tempPw, ue.getUserName());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (MailException x) {
			return new ResponseEntity<>(x.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Checks if the user can be logged in. User's email must be verified, and the
	 * account must exist.
	 *
	 * @param user user's login credentials
	 * @return logged in or not
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDTO user) {
		try {
			User ue = userRepo.findUserByUserName(user.getUsername());
			if (ue == null) {
				throw new LoginException("Username not found");
			}
			// if the password doesnt match the saved one
			String expectedPW = ue.getPassword();
			if (!passwordEncoder.matches(user.getPassword(), expectedPW)) {
				throw new LoginException("Incorrect password");
			}
			// if the user has not verified their account through email
			if (!ue.isIsEmailValidated())
				throw new LoginException("Account not verified");
			user.setUserType(ue.getUserType());
			user.setEmail(ue.getEmail());
			user.setUsername(ue.getUserName());
			user.setPassword(null);
			user.setToken(ue.getApiToken());
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (ca.mcgill.ecse321.petshelter.service.LoginException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Deletes a user. The person making the request must be an admin.
	 *
	 * @param userName user's account to delete
	 * @return delete was successful or not
	 */
	@DeleteMapping("/{userName}")
	public ResponseEntity<?> deleteUser(@PathVariable String userName, @RequestHeader String token) {
		User requester = userRepo.findUserByApiToken(token);
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
	//todo fix cette pisse @louismouisine
	/**
	 * Returns a specified user's information. The requester must be an admin.
	 * @param userName
	 * @param token
	 * @return
	 */
	@GetMapping("/{username}")
	public ResponseEntity<?> getUser(@PathVariable String userName, @RequestHeader String token) {
		User requester = userRepo.findUserByApiToken(token);
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
	 * 	 * make the request.
	 * @param userName
	 * @param userDto
	 * @param token
	 * @return
	 */
	@PutMapping("/{username}")
	// note: the username and email cannot be changed
	public ResponseEntity<?> updateUser(@PathVariable String userName, @RequestBody UserDTO userDto,
			@RequestHeader String token) {
		// find a user by username
		User user = userRepo.findUserByUserName(userName);
		// the user updating the profile must be the requester, as in it should be his
		// profile
		if (user != null && token.equals(user.getApiToken())) {
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
	 * @param token
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getUsers(@RequestHeader String token) {
		User requester = userRepo.findUserByApiToken(token);
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
	
	/**
	 * Allows users to change passwords.
	 *
	 * @param passwordDto password transfer obj
	 * @return changed password response
	 */
	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO passwordDto) {
		try {
			UserDTO user = userService.updateUser(passwordDto);
			user.setPassword(null);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (IllegalArgumentException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
