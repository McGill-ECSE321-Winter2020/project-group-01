package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.*;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.AdvertisementService;
import ca.mcgill.ecse321.petshelter.service.PetService;
import ca.mcgill.ecse321.petshelter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller that handles requests made to access, modify or delete an
 * advertisement.
 * 
 * @author louis
 *
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PetService petService;
	
	@Autowired
	PetRepository petRepository;

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
			if (pet.getAdvertisement().equals(ad.getId())) {
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
	 * @param adId  Advertisement ID.
	 * @param token The requester's token.
	 * @return The advertisement DTO.
	 */
	@GetMapping("/id/{adId}")
	public ResponseEntity<?> getAdvertisement(@PathVariable(required = true) Long adId, @RequestHeader String token) {
		AdvertisementDTO ad = advertisementService.getAdvertisementById(adId);
		User requester = userRepository.findUserByApiToken(token); // make sure the requester is logged in
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
	 * @param token The requester's token.
	 * @return The list of all advertisements with this title.
	 */
	@GetMapping("/title")
	public ResponseEntity<?> getAdvertisementByTitle(@RequestBody String title, @RequestHeader String token) {
		List<AdvertisementDTO> ads = advertisementService.getAdvertisementByTitle(title);
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) { // make sure the requester is logged in
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
	 * @param token The requester's token.
	 * @return List of all existing advertisements.
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllAdvertisements(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) { // make sure the requester is logged in
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
	 * @param advertisementDTO dto
	 * @param token            The session token of the user.
	 * @return The created advertisement.
	 */
	@PostMapping("/{petId}/newAd")
	public ResponseEntity<?> createAdvertisement(@PathVariable(required = true) long petId, @RequestBody AdvertisementDTO advertisementDTO, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		Pet pet = petRepository.findPetById(petId);
		boolean isOwner = user.getPets().contains(pet);
		if (user != null && isOwner && advertisementDTO.getTitle() != null && !advertisementDTO.getTitle().trim().equals("") && advertisementDTO.getDescription() != null
				&& !advertisementDTO.getDescription().trim().equals("")) {
			Long[] petIds = new Long[1];
			petIds[0] = petId;
			AdvertisementDTO adDto = createAdDto(advertisementDTO.getTitle(), false, petIds, new HashSet<Application>(), advertisementDTO.getDescription());
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
	 * @param advertisementDTO The advertisement DTO containing the information to change.
	 * @param token            The user session token.
	 * @return The modified advertisement as a DTO.
	 */
	@PutMapping("/")
	public ResponseEntity<?> updateAd(@RequestBody AdvertisementDTO advertisementDTO,
									  @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		AdvertisementDTO advertisementOld = advertisementService.getAdvertisementById(advertisementDTO.getAdId());
		if (user != null // Check if user an advert exist and if user can modify it.
				&& advertisementOld != null
				//	&& hasRightsForAd(user, advertisementOld) this needs to be fixed later on THIS ALSO NEEDS TO BE FIXED
				&& advertisementDTO.getTitle() != null // Check if the new title is valid.
				&& !advertisementDTO.getTitle().trim().equals("")) {
			// Converts all the pets of the user to a set of their IDs.
			Set<Long> petsID = petService.getPetsByUser(user.getUserName()).stream()
					.map(PetDTO::getId)
					.collect(Collectors.toSet());
			
			// Then verify if the pets of the new advertisement are contained in that set.
			
			if (petsID.containsAll(Arrays.asList(advertisementDTO.getPetIds()))) { //this doesnt work either, CANT just cast like this
				advertisementDTO.setAdId(advertisementDTO.getAdId()); // Make sure the advertisement ID remains the same.
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
		//user != null && hasRightsForAd(user, ad) && ad != null DOESNT WORK
		if (user != null && ad != null) {
			advertisementService.deleteAdvertisement(ad);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Converts an application into an applicationdto
	 * 
	 * @param application
	 * @return
	 */
	public static ApplicationDTO applicationToDto(Application application) {
		ApplicationDTO applicationDTO = new ApplicationDTO();
		applicationDTO.setDescription(application.getDescription());
		applicationDTO.setUsername(application.getUser().getUserName());
		applicationDTO.setAdvertisementTitle(application.getAdvertisement().getTitle());
		applicationDTO.setIsAccepted(application.isIsAccepted());
		applicationDTO.appId = application.getId();
		
		return applicationDTO;
	}
	
	/**
	 * Creates an advertisement dto.
	 *
	 * @param title
	 * @param isfulfilled
	 * @param aD_PET_IDS
	 * @param adoptionApplication
	 * @param description
	 * @return
	 */
	private AdvertisementDTO createAdDto(String title, boolean isfulfilled, Long[] aD_PET_IDS,
										 Set<Application> adoptionApplication, String description) {
		AdvertisementDTO dto = new AdvertisementDTO();
		dto.setApplication(adoptionApplication);
		dto.setDescription(description);
		dto.setTitle(title);
		dto.setFulfilled(isfulfilled);
		dto.setPetIds(aD_PET_IDS);
		return dto;
	}

	/**
	 * Determines if a user has rights to modify an ad.
	 * 
	 * @param user
	 * @param ad
	 * @return
	 */
	public boolean hasRightsForAd(User user, AdvertisementDTO ad) {
		boolean hasRights = false;
		List<PetDTO> pets = petService.getPetsByAdvertisement(ad.getAdId());
		if (user.getUserType().equals(UserType.USER)) {
			for (PetDTO pet : pets) {
				for (Pet pet1 : user.getPets()) {
					PetDTO pet1Dto = petToPetDTO(pet1);
					if (pet1Dto.equals(pet)) {
						hasRights = true;
					}
				}
			}
		} else {
			hasRights = true;
		}
		return hasRights;
	}

	/**
	 * COnverts a pet into a petdto.
	 * 
	 * @param pet
	 * @return
	 */
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
