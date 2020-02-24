package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController {

	@Autowired
	private AdvertisementService advertisementService;

	public AdvertisementController() {
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllAdvertisements() {
		return new ResponseEntity<>(
				advertisementService.getAllAdvertisements().stream().map(this::convertToDto).collect(Collectors.toList()),
				HttpStatus.OK);
	}

	public AdvertisementDTO convertToDto(Advertisement advertisement) {
		AdvertisementDTO advertisementDTO = new AdvertisementDTO();
		advertisementDTO.setApplications(advertisement.getAdoptionApplication());
		advertisementDTO.setDescription(advertisement.getDescription());
		advertisementDTO.setTitle(advertisement.getTitle());
		advertisementDTO.setIsFulfilled(advertisement.isIsFulfilled());

		return advertisementDTO;
	}

	@PostMapping()
	public ResponseEntity<?> createAdvertisement(@RequestBody AdvertisementDTO advertisementDTO) {
		Advertisement advertisement = advertisementService.createAdvertisement(advertisementDTO);
		try {
			advertisementDTO.setApplications(advertisement.getAdoptionApplication());
			advertisementDTO.setDescription(advertisement.getDescription());
			advertisementDTO.setTitle(advertisement.getTitle());
			advertisementDTO.setIsFulfilled(advertisement.isIsFulfilled());
			return new ResponseEntity<>(advertisementDTO, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
