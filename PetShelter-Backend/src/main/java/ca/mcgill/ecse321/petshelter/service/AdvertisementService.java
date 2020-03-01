package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Application;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.service.exception.AdvertisementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdvertisementService {
	@Autowired
	private AdvertisementRepository advertisementRepository;
	
	@Autowired
	private PetRepository petRepository;
	
	/**
	 * Finds an advertisement by the DTO
	 *
	 * @param adDTO advertisement DTO
	 * @return advertisement object
	 */
	@Transactional
	public Advertisement getAdvertisement(AdvertisementDTO adDTO) {
		Advertisement ad = advertisementRepository.findAdvertisementById(adDTO.getAdId());
		if (ad == null) {
			throw new AdvertisementException("Advertisement does not exist.");
		} else {
			return ad;
		}
	}
	
	/**
	 * Finds advertisement by title
	 *
	 * @param title title of advertisement
	 * @return List of advertisement that matches that title
	 */
	@Transactional
	public List<Advertisement> getAdvertisementByTitle(String title) {
		return advertisementRepository.findAdvertisementByTitle(title);
	}
	
	/**
	 * Gets all the created advertisement in the database
	 *
	 * @return List of all the advertisement
	 */
	@Transactional
	public List<Advertisement> getAllAdvertisements() {
		return advertisementRepository.findAll();
	}
	
	/**
	 * Finds advertisement by a PetID
	 *
	 * @param petId unique identifier of the pet
	 * @return unique advertisement linked to that pet
	 */
	@Transactional
	public Advertisement getAdvertisementByPet(long petId) {
		Pet pet = petRepository.findPetById(petId);
		if (pet == null) {
			throw new AdvertisementException("Pet does not exist.");
		} else {
			Advertisement ad = pet.getAdvertisement();
			if (ad == null) {
				throw new AdvertisementException("Advertisement does not exist.");
			} else {
				return ad;
			}
		}
	}
	
	/**
	 * Creates and advertisement and stores it in the database
	 *
	 * @param adDTO advertisement DTO (JSON format)
	 * @return advertisement object
	 */
	@Transactional
	public Advertisement createAdvertisement(AdvertisementDTO adDTO) {
		int numOfPets = adDTO.getPetIds().length;
		if (numOfPets == 0) {
			throw new AdvertisementException("A pet must be linked to an advertisement.");
		}
		//finding all pets that are on this ad
		List<Pet> petsInAd = new ArrayList<>();
		for (int i = 0; i < numOfPets; i++) {
			Pet pet = petRepository.findPetById(adDTO.getPetIds()[i]);
			if (pet == null) {
				throw new AdvertisementException("One or more pets do not exist.");
			} else if (pet.getAdvertisement() != null) {
				throw new AdvertisementException("One or more pets already have an advertisement.");
			}
			petsInAd.add(pet);
		}
		
		if (adDTO.getTitle().equals("") || adDTO.getTitle() == null) {
			throw new AdvertisementException("An advertisement needs a title");
		}
		if (adDTO.getDescription().trim().equals("") || adDTO.getDescription() == null) {
			throw new AdvertisementException("An advertisement needs a description");
		}
		Set<Application> applications = new HashSet<>();
		applications.addAll(adDTO.getApplication());
		Advertisement ad = new Advertisement();
		ad.setTitle(adDTO.getTitle());
		ad.setIsFulfilled(adDTO.isFulfilled());
		ad.setDescription(adDTO.getDescription());
		ad.setApplication(applications);
		advertisementRepository.save(ad);
		for (Pet pet : petsInAd) {
			pet.setAdvertisement(ad);
			petRepository.save(pet);
		}
		adDTO.setAdId(ad.getId());
		return ad;
	}
	
	
	//todo: check if edit and delete is the original user in order to delete
	
	
	/**
	 * Allows user to edit advertisement
	 *
	 * @param adDTO advertisement DTO
	 * @return edited advertisement
	 */
	@Transactional
	public Advertisement editAdvertisement(AdvertisementDTO adDTO) {
		Advertisement ad = advertisementRepository.findAdvertisementById(adDTO.getAdId());
		if (ad == null) {
			throw new AdvertisementException("Advertisement does not exist");
		}
		if (adDTO.getTitle().trim().equals("") || adDTO.getTitle() == null) {
			throw new AdvertisementException("Title cannot be empty");
		}
		if (adDTO.getDescription().trim().equals("") || adDTO.getDescription().trim() == null) {
			throw new AdvertisementException("Description cannot be empty");
		}
		Pet pet0 = petRepository.findPetById(adDTO.getPetIds()[0]);
		if (pet0 == null) {
			throw new AdvertisementException("One or more pets do not exist.");
		}
		int numOfPets = adDTO.getPetIds().length;
		Set<Pet> petsInAd = new HashSet<Pet>();
		petsInAd.add(pet0);
		for (int i = 1; i < numOfPets; i++) {
			Pet petI = petRepository.findPetById(adDTO.getPetIds()[i]);
			if ((petI.getAdvertisement() != ad) && (petI.getAdvertisement() != null)) {
				throw new AdvertisementException("One or more pets have a different advertisement.");
			} else {
				petsInAd.add(petI);
			}
		}
		Set<Application> applications = new HashSet<Application>();
		applications.addAll(adDTO.getApplication());
		ad.setApplication(applications);
		ad.setTitle(adDTO.getTitle());
		ad.setDescription(adDTO.getDescription());
		ad.setIsFulfilled(adDTO.isFulfilled());
		advertisementRepository.save(ad);
		for (Pet pet : petsInAd) {
			pet.setAdvertisement(ad);
			petRepository.save(pet);
		}
		return ad;
	}
	
	/**
	 * Deletes advertisement from the database
	 *
	 * @param adDTO advertisement DTO
	 * @return
	 */
	@Transactional
	public void deleteAdvertisement(AdvertisementDTO adDTO) {
		Advertisement ad = getAdvertisement(adDTO);
		advertisementRepository.delete(ad);
	}
}
