package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Application;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.ApplicationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller that handles requests made to create, edit and delete
 * applications.
 * 
 * @author louis
 *
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/application")
public class ApplicationController {
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private AdvertisementRepository advertisementRepository;
	
	@Autowired
	private UserRepository userRepository;

	
	/*
	
	NOTE for later:
	
	we will need to add admin check
	if we get all applications, if it user --> return user's
	if admin return all
	
	 */
	
	
	/**
	 * Returns all applications.
	 *
	 * @param token The requester's token.
	 * @return the list of all applications
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllApplications(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null && requester.getUserType() == UserType.ADMIN)
			return new ResponseEntity<>(applicationService.getAllApplications(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Returns all applications.
	 * @return the list of all applications
	 * @param token The requester's token.
	 */
	@GetMapping("/allAccepted")
	public ResponseEntity<?> getAllAcceptedApplications(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null && requester.getUserType() == UserType.ADMIN)
			return new ResponseEntity<>(applicationService.getAllAcceptedApplications(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Returns all applications.
	 * @return the list of all applications
	 * @param token The requester's token.
	 */
	@GetMapping("/allUnaccepted")
	public ResponseEntity<?> getAllUnacceptedApplications(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null && requester.getUserType() == UserType.ADMIN)
			return new ResponseEntity<>(applicationService.getAllUnacceptedApplications(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Returns applications made by a user.
	 * @param user user targeted
	 * @param token Requester's token.
	 * @return that user's applications
	 */
	@GetMapping("/{user}")
	public ResponseEntity<?> getUserApplication(@PathVariable String user, @RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null && (requester.getUserType() == UserType.ADMIN || requester.getUserName().equals(user))) {
			return new ResponseEntity<>(applicationService.getAllUserApplications(requester.getUserName()), HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}
	
	/**
	 * Returns applications for a particular advertisement.
	 * @param advertisement the advertisement targeted
	 * @param token the requester's token.
	 * @return the advertisement's applications
	 */
	@GetMapping("/{allAdvertisementApplications}")
	public ResponseEntity<?> getAdvertisementApplication(@PathVariable long advertisementId, @RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		Advertisement advertisement = advertisementRepository.findAdvertisementById(advertisementId);
		if (requester != null && (requester.getUserType() == UserType.ADMIN)) {
			return new ResponseEntity<>(applicationService.getAllAdvertisementApplications(advertisement), HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

	/**
	 * Creates an application with the DTO
	 *
	 * @param applicationDTO JSON passed by the request body
	 * @param token          The requester's token.
	 * @return check if all the fields are good
	 */
	@PostMapping("/create")
	public ResponseEntity<?> createApplication(@RequestBody ApplicationDTO applicationDTO,
											   @RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			try {
				ApplicationDTO application = applicationService.createApplication(applicationDTO);
				return new ResponseEntity<>(application, HttpStatus.OK);
			} catch (IllegalArgumentException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Deletes an application from the database. By design, only an admin may
	 * delete an application.
	 *
	 * @param applicationId the application id of the application to delete
	 * @param token         the session token of the user
	 * @return the deleted application
	 */
	@DeleteMapping("/{applicationId}")
	public ResponseEntity<?> deleteApplication(@PathVariable long applicationId, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		Optional<Application> application = applicationRepository.findById(applicationId);
		if (user != null && application.isPresent() && user.getUserType().equals(UserType.ADMIN)) {
			// Delete application
			
			applicationRepository.delete(application.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	/**
	 * Updates the description of an application
	 * @param applicationId the application id
	 * @param newDescription the new description
	 * @param token the session token of the user
	 * @return the updated application dto
	 */
	@PutMapping("/{applicationId}")
	public ResponseEntity<?> updateApplication(@PathVariable long applicationId, @RequestBody String newDescription, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		Optional<Application> oldApplication = applicationRepository.findById(applicationId);
		if (
				user != null // verify user exists
				&& oldApplication.isPresent() // verify application exists  
				&& oldApplication.get().getUser().getId() == user.getId() // verify the user is the author of the application
				&& newDescription != null // verify that the description is valid
				&& !newDescription.trim().equals("") // verify that the description is valid
			) { 
			// Update application
			
			ApplicationDTO applicationDTO = applicationService.updateApplication(applicationId, newDescription);
			return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}
