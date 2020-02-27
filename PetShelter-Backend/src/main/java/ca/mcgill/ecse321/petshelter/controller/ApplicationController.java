package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/application")
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private UserRepository userRepository;

	public ApplicationController() {
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllApplications() {
		return new ResponseEntity<>(
				applicationService.getAllApplications().stream().map(this::convertToDto).collect(Collectors.toList()),
				HttpStatus.OK);
	}

	@GetMapping("/{user}")
	public ResponseEntity<?> getUserApplication(@PathVariable String user) {
		return new ResponseEntity<>(applicationService.getAllUserApplications(userRepository.findUserByUserName(user))
				.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
	}

	public ApplicationDTO convertToDto(AdoptionApplication application) {
		ApplicationDTO applicationDTO = new ApplicationDTO();
		applicationDTO.setDescription(application.getDescription());
		applicationDTO.setUsername(application.getUser().getUserName());
		applicationDTO.setAdvertisementTitle(application.getAdvertisement().getTitle());
		applicationDTO.setIsAccepted(application.isIsAccepted());

		return applicationDTO;
	}

	@PostMapping()
	public ResponseEntity<?> createApplication(@RequestBody ApplicationDTO applicationDTO) {
		AdoptionApplication application = applicationService.createApplication(applicationDTO);
		try {
			applicationDTO.setDescription(application.getDescription());
			applicationDTO.setUsername(application.getUser().getUserName());
			applicationDTO.setAdvertisementTitle(application.getAdvertisement().getTitle());
			applicationDTO.setIsAccepted(application.isIsAccepted());
			return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
