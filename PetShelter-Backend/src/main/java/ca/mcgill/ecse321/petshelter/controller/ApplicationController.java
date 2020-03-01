package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	private UserRepository userRepository;

	/**
	 * Returns all applications.
	 * @return the list of all applications
	 * @param token The requester's token.
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllApplications(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null)
			return new ResponseEntity<>(applicationService.getAllApplications(), HttpStatus.OK);
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
		if (requester != null) {
			return new ResponseEntity<>(
					applicationService.getAllUserApplications(userRepository.findUserByUserName(user)), HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

	/**
	 * Creates an application with the DTO
	 *
	 * @param applicationDTO JSON passed by the request body
	 * @param token The requester's token.
	 * @return check if all the fields are good
	 */
	@PostMapping()
	public ResponseEntity<?> createApplication(@RequestBody ApplicationDTO applicationDTO,
			@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			ApplicationDTO application = applicationService.createApplication(applicationDTO);
			try {
				applicationDTO.setDescription(application.getDescription());
				applicationDTO.setUsername(application.getUsername());
				applicationDTO.setAdvertisementTitle(application.getAdvertisementTitle());
				applicationDTO.setIsAccepted(application.getIsAccepted());
				return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
			} catch (IllegalArgumentException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
