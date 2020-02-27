package ca.mcgill.ecse321.petshelter.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;

@Service
public class AdvertisementService {
	@Autowired
	private AdvertisementRepository advertisementRepository;

	@Autowired
	private PetRepository petRepository;
	
	@Transactional
	public Advertisement getAdvertisement(AdvertisementDTO adDTO) {
		Advertisement ad = advertisementRepository.findAdvertisementById(adDTO.getAdId());
		if(ad == null) {
			throw new IllegalArgumentException("Advertisement does not exist.");
		} else {
			return ad;
		}
	}
	
	@Transactional
	public List<Advertisement> getAdvertisementByTitle(String title) {
		List<Advertisement> ads = advertisementRepository.findAdvertisementByTitle(title);
		return ads;
	}

	@Transactional
	public List<Advertisement> getAllAdvertisements() {
		List<Advertisement> allAds = advertisementRepository.findAll();
		return allAds;
	}
	
	@Transactional
	public Advertisement getAdvertisementByPet(long petId) {
		Pet pet = petRepository.findPetById(petId);
		if(pet == null) {
			throw new IllegalArgumentException("Pet does not exist.");
		} else {
			Advertisement ad = pet.getAdvertisement();
			if(ad == null) {
				throw new IllegalArgumentException("Advertisement does not exist.");
			} else {
				return ad;
			}
		}
	}
	
	@Transactional
	public Advertisement createAdvertisement(AdvertisementDTO adDTO) {
		int numOfPets = adDTO.getPetIds().length;
		if(numOfPets == 0) {
			throw new IllegalArgumentException("A pet must be linked to an advertisement.");
		}
		//finding all pets that are on this ad
		List<Pet> petsInAd = new ArrayList<Pet>();
		for(int i = 0; i < numOfPets; i++) {
			Pet pet = petRepository.findPetById(adDTO.getPetIds()[i]);
			if (pet == null) {
				throw new IllegalArgumentException("One or more pets do not exist.");
			}
			else if (pet.getAdvertisement()!=null) {
				throw new IllegalArgumentException("One or more pets already have an advertisement.");
			}
			petsInAd.add(pet);
		}

		if (adDTO.getTitle() == "" || adDTO.getTitle() == null) {
			throw new IllegalArgumentException("An advertisement needs a title");
		}
		if (adDTO.getDescription().trim() == "" || adDTO.getDescription() == null) {
			throw new IllegalArgumentException("An advertisement needs a description");
		}
		Set<AdoptionApplication> applications = new HashSet<AdoptionApplication>();
		applications.addAll(adDTO.getAdoptionApplication());
		Advertisement ad = new Advertisement();
		ad.setTitle(adDTO.getTitle());
		ad.setIsFulfilled(adDTO.isFulfilled());
		ad.setDescription(adDTO.getDescription());
		ad.setAdoptionApplication(applications);
		advertisementRepository.save(ad);
		for (Pet pet : petsInAd) {
			pet.setAdvertisement(ad);
			petRepository.save(pet);
		}
		adDTO.setAdId((long)ad.getId());
		return ad;		
	}
	
	//TODO change
	@Transactional
	public Advertisement editAdvertisement(AdvertisementDTO adDTO) {
		Advertisement ad = advertisementRepository.findAdvertisementById(adDTO.getAdId());
		if (ad == null) {
			throw new IllegalArgumentException("Advertisement does not exist");
		}
		if (adDTO.getTitle().trim() == "" || adDTO.getTitle() == null) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		if (adDTO.getDescription().trim() == "" || adDTO.getDescription().trim() == null) {
			throw new IllegalArgumentException("Description cannot be empty");
		}
		Pet pet0 = petRepository.findPetById(adDTO.getPetIds()[0]);
		if (pet0 == null) {
			throw new IllegalArgumentException("One or more pets do not exist.");
		}
		int numOfPets = adDTO.getPetIds().length;
		Set<Pet> petsInAd = new HashSet<Pet>();
		petsInAd.add(pet0);
		for (int i = 1; i < numOfPets; i++) {
			Pet petI = petRepository.findPetById(adDTO.getPetIds()[i]);
			if((petI.getAdvertisement() != ad) && (petI.getAdvertisement() != null)) {
				throw new IllegalArgumentException("One or more pets have a different advertisement.");
			} else {
				petsInAd.add(petI);
			}
		}
		Set<AdoptionApplication> applications = new HashSet<AdoptionApplication>();
		applications.addAll(adDTO.getAdoptionApplication());
		ad.setAdoptionApplication(applications);
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

	@Transactional 
	Advertisement deleteAdvertisement(AdvertisementDTO adDTO) {
		Advertisement ad = getAdvertisement(adDTO);
		advertisementRepository.delete(ad);
		return ad;
	}
}
