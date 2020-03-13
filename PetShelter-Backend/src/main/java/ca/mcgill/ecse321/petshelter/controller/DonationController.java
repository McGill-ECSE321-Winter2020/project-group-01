package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.DonationService;
import ca.mcgill.ecse321.petshelter.service.extrafeatures.EmailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class implements the REST controller for the donation related features of the backend.
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/donation")
public class DonationController {

	@Autowired
	private DonationService donationService;

	@Autowired
	private EmailingService emailingService;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Get all the donations in the system.
	 *
	 * @param token The user access token. Need to be an admin.
	 * @return returns all donations if the requester is an admin, if it is a user, only return their donations
	 */
	@GetMapping("/all")

	public ResponseEntity<?> getAllDonations(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null && requester.getUserType().equals(UserType.ADMIN))
			return new ResponseEntity<>(
					donationService.getAllDonations(), HttpStatus.OK);
		
		else if(requester != null && requester.getUserType().equals(UserType.USER)){
			return new ResponseEntity<>(
					donationService.getAllUserDonations(requester.getUserName()), HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}

	/**
	 * Get a user specific donation
	 *
	 * @param user user targeted
	 * @param token The user access token.
	 *
	 * @return all of that user's donations
	 */
	@GetMapping("/{user}")
	public ResponseEntity<?> getUserDonation(@PathVariable String user, @RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		User requestedUser = userRepository.findUserByUserName(user);
		if (requester != null && (requester.getUserType().equals(UserType.ADMIN) || token.equals(requestedUser.getApiToken())))
			return new ResponseEntity<>(donationService.getAllUserDonations(user), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}
    
	/**
	 * Creates a donation
	 *
	 * @param donationDTO DTO passed by the in the request body
	 *
	 * @return Ok if all the fields are satisfied, else Error msg
	 */
	@PostMapping("/")
	public ResponseEntity<?> createDonation(@RequestBody DonationDTO donationDTO, @RequestHeader String token) {
		DonationDTO donation = donationService.createDonation(donationDTO);
		try {
			//if the person is a registered user, we will send him a email
			// and we log that donation under that username
			if (donation.getUsername() != null && userRepository.findUserByApiToken(token).getUserName().equals(donation.getUsername())) {
				donationDTO.setUser(donation.getUsername());
				emailingService.donationConfirmationEmail(donation.getEmail(), donationDTO.getUsername(),
						donationDTO.getAmount(), donationDTO.getTime(), donationDTO.getDate());
				//if there is an email but the token is null, this means that it is a home page donation and we will only send a email to the donator and log is as anonymous
			} else if(donationDTO.getEmail() != null && token == null){
				donationDTO.setUser(null);
				emailingService.donationConfirmationEmail(donation.getEmail(), donationDTO.getUsername(),
						donationDTO.getAmount(), donationDTO.getTime(), donationDTO.getDate());
			}
			return new ResponseEntity<>(donation, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
