package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.DonationService;
import ca.mcgill.ecse321.petshelter.service.EmailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/donation")
public class DonationController {

	@Autowired
	private DonationService donationService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailingService emailingService;
	

	@GetMapping("/all")
	public ResponseEntity<?> getAllDonations() {
		return new ResponseEntity<>(
				donationService.getAllDonations().stream().map(this::convertToDto).collect(Collectors.toList()),
				HttpStatus.OK);
	}

	@GetMapping("/{user}")
	public ResponseEntity<?> getUserDonation(@PathVariable String user) {
		return new ResponseEntity<>(donationService.getAllUserDonations(userRepository.findUserByUserName(user))
				.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
	}

	public DonationDTO convertToDto(Donation donation) {
		DonationDTO donationDTO = new DonationDTO();
		donationDTO.setDate(donation.getDate());
		donationDTO.setTime(donation.getTime());
		donationDTO.setAmount(donation.getAmount());
		try {
			donationDTO.setUser(donation.getUser().getUserName());
		} catch (NullPointerException e) {
			donationDTO.setUser(null); // occurs when it is an anonymous donor, no account.
		}
		return donationDTO;
	}

	@PostMapping()
	public ResponseEntity<?> createDonation(@RequestBody DonationDTO donationDTO) {
		Donation donation = donationService.createDonation(donationDTO);
		try {
			if (donation.getUser() != null) {
				donationDTO.setUser(donation.getUser().getUserName());
				emailingService.donationConfirmationEmail(donation.getUser().getEmail(), donationDTO.getUsername(),
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
