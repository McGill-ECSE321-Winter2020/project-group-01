package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.UserService;
import ca.mcgill.ecse321.petshelter.service.exception.LoginException;
import ca.mcgill.ecse321.petshelter.service.exception.RegisterException;
import ca.mcgill.ecse321.petshelter.service.extrafeatures.EmailingService;
import ca.mcgill.ecse321.petshelter.service.extrafeatures.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.ecse321.petshelter.service.UserService.userToDto;

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
	public ResponseEntity<?> register(@RequestBody UserDTO user) {
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
	 * @return confirmation msg
	 */
	@GetMapping("/registrationConfirmation")
	public ResponseEntity<?> confirmRegistration(@RequestParam String token) {
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
	 * @param username username to reset password
	 * @return check if pwd is reset
	 */
	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody String username) {
		User ue = userRepo.findUserByUserName(username);
		// if no user is found with that email, bad request
		if (ue == null) {

			return new ResponseEntity<>("No account has that username",HttpStatus.BAD_REQUEST);
		}
		// if the account is not validated, the password cant be changed
		if (!ue.isIsEmailValidated()) {
			return new ResponseEntity<>("Account not verified", HttpStatus.BAD_REQUEST);
		}
		// generate a random password for the user to log in and change later

		try {
			String tempPw = userService.resetPassword(username);
			emailingService.userForgotPasswordEmail(ue.getEmail(), tempPw, ue.getUserName());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (MailException | RegisterException x) {
			return new ResponseEntity<>(x.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Checks if the user can be logged in. User's email must be verified, and the
	 * account must exist. Generates a new token on successful login.
	 *
	 * @param userDto user's login credentials
	 * @return check if can login
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDTO userDto) {
		try {
			User user = userRepo.findUserByUserName(userDto.getUsername());
			if (user == null) {
				throw new LoginException("Username not found");
			}
			// if the password doesnt match the saved one
			if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
				throw new LoginException("Incorrect password");
			}

			// if the user has not verified their account through email
			if (!user.isIsEmailValidated())
				throw new LoginException("Account not verified");
			// generate a new token and save the token if user is not admin
			if (!user.getUserType().equals(UserType.ADMIN)) {
				user.setApiToken(jwtTokenProvider.createToken(userDto.getUsername()));
				userRepo.save(user);
			}
			userDto.setUserType(user.getUserType());
			userDto.setEmail(user.getEmail());
			userDto.setPassword(null);
			userDto.setToken(user.getApiToken());
			return new ResponseEntity<>(userDto, HttpStatus.OK);
		} catch (LoginException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Deletes a user. The person making the request must be an admin.
	 *
	 * @param username user's account to delete
	 * @return delete was successful or not
	 */
	@DeleteMapping("/{username}")
	public ResponseEntity<?> deleteUser(@PathVariable String username, @RequestHeader String token)
			throws RegisterException {
		User requester = userRepo.findUserByApiToken(token);
		if (requester != null && requester.getUserType().equals(UserType.ADMIN)) {
			if (userService.deleteUser(username)) { // if the user is successfully deleted
				return new ResponseEntity<>(HttpStatus.OK);
			} else {

				return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Only admins may delete accounts",HttpStatus.BAD_REQUEST); // if the requester is not an admin
		}

	}

	/**
	 * Returns a specified user's information. The requester must be an admin.
	 *
	 * @param username user to be deleted
	 * @param token    admin only token
	 * @return error if cant get user
	 */
	@GetMapping("/{username}")
	public ResponseEntity<?> getUser(@PathVariable String username, @RequestHeader String token) {
		User requester = userRepo.findUserByApiToken(token);
		// find a user by username
		User user = userRepo.findUserByUserName(username);
		if ((requester != null && user != null)
				&& (requester.getUserType().equals(UserType.ADMIN) || requester.getUserName().equals(username))) {
			return new ResponseEntity<>(userToDto(user), HttpStatus.OK);
			// if the user making the request is not an admin or the one we are searching
			// for, bad request
		} else {

			return new ResponseEntity<>("You do not have permission to get that account",HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Updates a specified user's information. Only the user tied to the account can
	 * * make the request.
	 *
	 * @param username username
	 * @param userDto  userDTO
	 * @param token    user's token
	 * @return picture changed
	 */
	@PutMapping("/{username}")
	// note: the username and email cannot be changed
	public ResponseEntity<?> updateUserPicture(@PathVariable String username, @RequestBody UserDTO userDto,
			@RequestHeader String token) {
		// find a user by username
		User user = userRepo.findUserByUserName(username);
		// the user updating the profile must be the requester, as in it should be his
		// profile
		if (user != null && token.equals(user.getApiToken()) && user.isIsEmailValidated()) {
			// actually, only the picture is allowed to be updated (design decision)
			if (userDto.getPicture() != null) {
				user.setPicture(userDto.getPicture());
			}
			return new ResponseEntity<>(userToDto(user), HttpStatus.OK);
		} else {

			return new ResponseEntity<>("You may not edit another user's picture", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Returns all users' information. The requester must be an admin.
	 *
	 * @param token user's token
	 * @return targeted user
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
				return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		} else { // if user isnt an admin

			return new ResponseEntity<>("Only admins may do this",HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Allows users to change passwords.
	 *
	 * @param passwordDto password transfer obj
	 * @return changed password response
	 */
	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO passwordDto, @RequestHeader String token) {
		System.out.println(passwordDto);
		User requester = userRepo.findUserByApiToken(token);
		if (requester != null && requester.getUserName().equals(passwordDto.getUserName())
				|| requester.getUserType().equals(UserType.ADMIN)) {
			try {
				UserDTO user = userService.updateUser(passwordDto);
				user.setPassword(null);
				return new ResponseEntity<>(user, HttpStatus.OK);
			} catch (IllegalArgumentException ex) {
				return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<>("You may only change your own password",HttpStatus.BAD_REQUEST);
	}

	/**
	 * Deletes the user's token from the database. The user is now seen as logged
	 * out.
	 *
	 * @param token user's token
	 * @return deletes user token
	 */
	@GetMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader String token) {
		User user = userRepo.findUserByApiToken(token);
		// if the user cannot be found
		if (user == null) {
			return new ResponseEntity<>("Account not found",HttpStatus.BAD_REQUEST);
		} else {
			// For ease of use, the admin will not have their token deleted
			if (user.getUserType().equals(UserType.ADMIN)) {
				user.setApiToken(null);
				userRepo.save(user);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	/**
	 * Determines if the token it is provided belongs to a user.
	 * This is used to determine what to display on the website.
	 * @param token user's token
	 * @return deletes user token
	 */
	@PostMapping("/validUser")
	public ResponseEntity<?> validUser(@RequestBody String token) {
		User user = userRepo.findUserByApiToken(token);
		// if the user cannot be found
		if (user == null) {
			return new ResponseEntity<>("Account not found",HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	/**
	 * Determines if the token it is provided belongs to an admin.
	 * This is used to determine what to display on the website.
	 * @param token user's token
	 * @return deletes user token
	 */
	@PostMapping("/admin")
	public ResponseEntity<?> admin(@RequestBody String token) {
		User user = userRepo.findUserByApiToken(token);
		// if the user cannot be found
		if (user == null) {
			return new ResponseEntity<>("Account not found",HttpStatus.BAD_REQUEST);
		} else {
			if(user.getUserType().equals(UserType.ADMIN)) {
				return new ResponseEntity<>(true,HttpStatus.OK);
			}
			return new ResponseEntity<>(false,HttpStatus.OK);
		}
	}


}
