package ca.mcgill.ecse321.petshelter.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.service.AdvertisementService;

@CrossOrigin(origins = "*")
@RestController
public class AdvertisementController {

	@Autowired
	private AdvertisementService advertisementService;

	@GetMapping("/all")
	public List<AdvertisementDTO> getAllAdvertisements(){
		List<AdvertisementDTO> adDtos = new ArrayList<>();
		for (Advertisement ad : advertisementService.getAllAdvertisements()) {
			adDtos.add(convertToDto(ad));
		}
		return adDtos;
	}

	@GetMapping("/{pet}")
	public AdvertisementDTO getAdvertisementOfPet(@PathVariable PetDTO pDto) {
		return pDto.getAdvertisementDTO();
	}

	//TODO fix it
	@PostMapping("/advertise")
	public ResponseEntity<?> createAdvertisement(@RequestBody AdvertisementDTO adDTO){

		try {
			if(adDTO == null) {
				throw new IllegalArgumentException("There must be an advertisement.");
			}
			Advertisement ad = advertisementService.createAdvertisement(adDTO);
			if(ad == null) {	
				return new ResponseEntity<>(adDTO, HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(adDTO, HttpStatus.OK);

		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	public AdvertisementDTO convertToDto (Advertisement ad) {
		if (ad == null) {
			throw new IllegalArgumentException("There is no such advertisement.");
		}
		String DtoTitle = ad.getTitle();
		long DtoPetId = advertisementService.getPetByAdvertisement(ad).getId();
		String DtoDescription = ad.getDescription();
		boolean DtoIsFulfilled = ad.isIsFulfilled();
		Set<AdoptionApplication> DtoApplications = new HashSet<AdoptionApplication>();
		DtoApplications.addAll(ad.getAdoptionApplication());
		AdvertisementDTO adDTO = new AdvertisementDTO(DtoDescription, DtoIsFulfilled,DtoPetId, DtoApplications, DtoTitle);
		return adDTO;
	}

	public PetDTO convertToDto (Pet p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such pet.");
		}
		Date DDate = p.getDateOfBirth();
		String DName = p.getName();
		String DSpecies = p.getSpecies();
		String DBreed = p.getBreed();
		String DDescription = p.getDescription();
		byte[] DPicture = p.getPicture();
		long DId = p.getId();
		Gender DGender = p.getGender();
		AdvertisementDTO DAdvertisement = convertToDto(p.getAdvertisement());
		PetDTO pDto = new PetDTO(DDate, DName, DSpecies, DBreed, DDescription, DPicture, DId, DGender, DAdvertisement);
		return pDto;
	}
}
