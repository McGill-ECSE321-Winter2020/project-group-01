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
	 * @return returns all donations if the requester is an admin
	 */
	@GetMapping("/all")

	public ResponseEntity<?> getAllDonations(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null && requester.getUserType().equals(UserType.ADMIN))
			return new ResponseEntity<>(
					donationService.getAllDonations(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Get a user specific donation
	 *
	 * @param user user targeted
	 * @return all of that user's donations
	 */
	@GetMapping("/{user}")
	public ResponseEntity<?> getUserDonation(@PathVariable String user, @RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		User requestedUser = userRepository.findUserByUserName(user);
		if (requester != null
				&& (requester.getUserType().equals(UserType.ADMIN) || token.equals(requestedUser.getApiToken())))
			return new ResponseEntity<>(donationService.getAllUserDonations(user), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}
    
	/**
	 * Creates a donation
	 *
	 * @param donationDTO DTO passed by the in the request body
	 * @return Ok if all the fields are satisfied, else Error msg
	 */
	@PostMapping()
	public ResponseEntity<?> createDonation(@RequestBody DonationDTO donationDTO) {
		DonationDTO donation = donationService.createDonation(donationDTO);
		try {
			if (donation.getUsername() != null) {
				donationDTO.setUser(donation.getUsername());
				emailingService.donationConfirmationEmail(donation.getEmail(), donationDTO.getUsername(),
						donationDTO.getAmount(), donationDTO.getTime(), donationDTO.getDate());
			}
			donationDTO.setTime(donation.getTime());
			donationDTO.setDate(donation.getDate());
			donationDTO.setAmount(donation.getAmount());
			return new ResponseEntity<>(donationDTO, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
