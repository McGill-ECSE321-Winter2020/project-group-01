package ca.mcgill.ecse321.petshelter.controller;

import java.util.*;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Application;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.AdvertisementService;
import ca.mcgill.ecse321.petshelter.service.PetService;
import ca.mcgill.ecse321.petshelter.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PetService petService;

	@Autowired
	AdvertisementService advertisementService;

	@Autowired
	UserService userService;

	/**
	 * Converts an advertisement to an advertisement DTO.
	 *
	 * @param ad The advertisement to convert.
	 * @return An advertisement DTO.
	 */

	AdvertisementDTO advertisementToDto(Advertisement ad) {
		AdvertisementDTO adDto = new AdvertisementDTO();
		Set<Application> applications = new HashSet<Application>();
		ad.getApplication().forEach(u -> applications.add(u));
		adDto.setAdId(ad.getId());
		adDto.setApplication(applications);
		adDto.setDescription(ad.getDescription());
		adDto.setTitle(ad.getTitle());
		adDto.setDescription(ad.getDescription());
		adDto.setFulfilled(ad.isIsFulfilled());
		List<Long> petIds = new ArrayList<Long>();
		Set<PetDTO> allPets = petService.getAllPets();
		for (PetDTO pet : allPets) {
			if (pet.getAdvertisement().equals(ad)) {
				petIds.add(pet.getId());
			}
		}
		Long[] ids = (Long[]) petIds.toArray();
		adDto.setPetIds(ids);
		return adDto;
	}

	/**
	 * Gets the desired advertisement.
	 *
	 * @param id Advertisement ID.
	 * @return The advertisement DTO.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getAdvertisement(@PathVariable(required = true) Long id, @RequestHeader String token) {
		AdvertisementDTO ad = advertisementService.getAdvertisementById(id);
		User requester = userRepository.findUserByApiToken(token);
		if (ad != null && requester != null) {
			return new ResponseEntity<>(ad, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get all advertisements with a specific title.
	 *
	 * @param title The title for which you want to find the advertisements.
	 * @return The list of all advertisements with this title.
	 */
	@GetMapping("/advertisements/{title}")
	public ResponseEntity<?> getAdvertisementByTitle(@PathVariable String title, @RequestHeader String token) {
		List<AdvertisementDTO> ads = advertisementService.getAdvertisementByTitle(title);
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			List<AdvertisementDTO> adDtos = new ArrayList<AdvertisementDTO>();
			for (AdvertisementDTO ad : ads) {
				adDtos.add(ad);
			}
			return new ResponseEntity<>(adDtos, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Gets all existing advertisements.
	 *
	 * @return List of all existing advertisements.
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllAdvertisements(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			List<AdvertisementDTO> ads = new ArrayList<>();
			List<AdvertisementDTO> adsDto = new ArrayList<>();
			Iterable<AdvertisementDTO> adsIterable = advertisementService.getAllAdvertisements();
			adsIterable.forEach(ads::add);
			for (AdvertisementDTO ad : ads) {
				adsDto.add(ad);
			}
			return new ResponseEntity<>(adsDto, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Create new advertisement.
	 *
	 * @param title The title of the advertisement to create.
	 * @param token The session token of the user.
	 * @return The created advertisement.
	 */
	@PostMapping("/{petId}/newAd")
	public ResponseEntity<?> createAdvertisement(@PathVariable(required = true) long petId, @RequestBody String title,
												 String description, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		PetDTO pet = petService.getPet(petId);
		boolean isOwner = user.getPets().contains(pet);

		if (user != null && isOwner && title != null && !title.trim().equals("") && description != null
				&& !description.trim().equals("")) {
			List<Long> petIds = new ArrayList<Long>();
			petIds.add(petId);
			AdvertisementDTO adDto = createAdDto(title, false, petIds, new HashSet<Application>(), description);
			adDto = advertisementService.createAdvertisement(adDto);
			if (adDto != null) {
				return new ResponseEntity<>(adDto, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update an advertisement.
	 *
	 * @param adID The ID of the advertisement to update.
	 * @param advertisementDTO The advertisement DTO containing the information to change.
	 * @param token The user session token.
	 * @return The modified advertisement as a DTO.
	 */
	@PutMapping
	public ResponseEntity<?> updateAd(@PathVariable long adID,
									  @RequestBody AdvertisementDTO advertisementDTO,
									  @RequestBody String token) {
		User user = userRepository.findUserByApiToken(token);
		AdvertisementDTO advertisementOld = advertisementService.getAdvertisementById(adID);
		if ( user != null // Check if user an advert exist and if user can modify it.
				&& advertisementOld != null
				&& hasRightsForAd(user, advertisementOld)
				&& advertisementDTO.getTitle() != null // Check if the new title is valid.
				&& !advertisementDTO.getTitle().trim().equals("") )
		{
			// Converts all the pets of the user to a set of their IDs.
			Set<Long> petsID = petService.getPetsByUser(user.getUserName()).stream()
					.map(PetDTO::getId)
					.collect(Collectors.toSet());
			// Then verify if the pets of the new advertisement are contained in that set.
			if (petsID.containsAll(Arrays.asList(advertisementDTO.getPetIds()))) {
				advertisementDTO.setAdId(adID); // Make sure the advertisement ID remains the same.
				return new ResponseEntity<AdvertisementDTO>(
						advertisementService.editAdvertisement(advertisementDTO),
						HttpStatus.OK
				);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete an advertisement from the database. By design, only an admin or the
	 * owner may delete an advertisement.
	 *
	 * @param adId  ID of the advertisement to delete.
	 * @param token Session token of the user.
	 * @return The deleted ad.
	 */
	@DeleteMapping("/{adId}")
	public ResponseEntity<?> deleteAd(@PathVariable long adId, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		AdvertisementDTO ad = advertisementService.getAdvertisementById(adId);
		if (user != null && hasRightsForAd(user, ad) && ad != null) {
			advertisementService.deleteAdvertisement(ad);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	public static ApplicationDTO applicationToDto(Application application) {
		ApplicationDTO applicationDTO = new ApplicationDTO();
		applicationDTO.setDescription(application.getDescription());
		applicationDTO.setUsername(application.getUser().getUserName());
		applicationDTO.setAdvertisementTitle(application.getAdvertisement().getTitle());
		applicationDTO.setIsAccepted(application.isIsAccepted());
		applicationDTO.appId = application.getId();

		return applicationDTO;
	}

	private AdvertisementDTO createAdDto(String title, boolean isfulfilled, List<Long> aD_PET_IDS,
			Set<Application> adoptionApplication, String description) {
		AdvertisementDTO dto = new AdvertisementDTO();
		dto.setApplication(adoptionApplication);
		dto.setDescription(description);
		dto.setTitle(title);
		dto.setFulfilled(isfulfilled);
		dto.setPetIds((Long[]) aD_PET_IDS.toArray()); 
		return dto;
	}

	public boolean hasRightsForAd(User user, AdvertisementDTO ad) {
		boolean hasRights = false;
		List<PetDTO> pets = petService.getPetsByAdvertisement(ad.getAdId());
		if (user.getUserType().equals(UserType.USER)) {
			for (PetDTO pet : pets) {
			    for (Pet pet1 : user.getPets()) {
			        PetDTO pet1Dto = petToPetDTO(pet1);
			        if(pet1Dto.equals(pet)) {
			            hasRights = true;
			        }
			    }
			}
		} else {
			hasRights = true;
		}
		return hasRights;
	}
	
    public PetDTO petToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setDateOfBirth(pet.getDateOfBirth());
        petDTO.setSpecies(pet.getSpecies());
        petDTO.setPicture(pet.getPicture());
        petDTO.setName(pet.getName());
        petDTO.setGender(pet.getGender());
        petDTO.setDescription(pet.getDescription());
        petDTO.setBreed(pet.getBreed());
        petDTO.setAdvertisement(pet.getAdvertisement().getId());
        return petDTO;
    }
}
