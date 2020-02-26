package ca.mcgill.ecse321.petshelter.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;

@Service
public class AdvertisementService {
	@Autowired
	private AdvertisementRepository advertisementRepository;

	@Autowired
	private PetRepository petRepository;

	@Transactional
	public Advertisement getAdvertisement(long id) {
		// TODO
		return null;
	}

	@Transactional
	public List<Advertisement> getAdvertisement(String title) {
		//TODO
		return null;
	}

	@Transactional
	public List<Advertisement> getAllAdvertisements() {
		List<Advertisement> allAds = advertisementRepository.findAll();
		return allAds;
	}
	
	@Transactional
	public Advertisement createAdvertisement( String title, boolean isFulfilled, String description, long[] petIds) {
		int numOfPets = petIds.length;
		if(numOfPets == 0) {
			throw new IllegalArgumentException("A pet must be linked to an advertisement");
		}
		//finding all pets that are on this ad
		List<Pet> petsInAd = new ArrayList<Pet>();
		for(int i = 0; i < numOfPets; i++) {
			Pet pet = petRepository.findPetById(petIds[i]);
			if (pet == null) {
				throw new IllegalArgumentException("One or more pets do not exist.");

			}
			else if (pet.getAdvertisement()!=null) {
				throw new IllegalArgumentException("One or more pets already have an advertisement.");
			}
			petsInAd.add(pet);
		}

		if (title.trim() == "" || title == null) {
			throw new IllegalArgumentException("An advertisement needs a title");
		}
		if (description.trim() == "" || description == null) {
			throw new IllegalArgumentException("An advertisement needs a description");
		}
		Set<AdoptionApplication> applications = new HashSet<AdoptionApplication>();
		Advertisement ad = new Advertisement();
		ad.setTitle(title);
		ad.setIsFulfilled(isFulfilled);
		ad.setDescription(description);
		ad.setAdoptionApplication(applications);
		advertisementRepository.save(ad);
		for (Pet pet : petsInAd) {
			pet.setAdvertisement(ad);
			petRepository.save(pet);
		}
		return ad;		
	}
	//TODO change
	@Transactional
	public Advertisement editAdvertisement(AdvertisementDTO adDTO) {
		if (adDTO.getTitle().trim() == "" || adDTO.getTitle() == null) {
			throw new IllegalArgumentException("Title can not be empty");
		}
		if (adDTO.getDescription().trim() == "" || adDTO.getDescription().trim() == null) {
			throw new IllegalArgumentException("Description cannot be empty");
		}
		// TODO cannot find by title. find by pet?
		Advertisement ad = advertisementRepository.findAdvertisementByTitle(adDTO.getTitle());
		Set<AdoptionApplication> applications = new HashSet<AdoptionApplication>();
		// TODO new hash set or existing hash set?
		applications.addAll(adDTO.getAdoptionApplication());
		ad.setAdoptionApplication(applications);
		ad.setTitle(adDTO.getTitle());
		ad.setDescription(adDTO.getDescription());
		ad.setIsFulfilled(adDTO.isFulfilled());

		advertisementRepository.save(ad);
		Pet pet = petRepository.findPetById(adDTO.getPetId());
		pet.setAdvertisement(ad);
		petRepository.save(pet);

		return ad;
	}

	@Transactional
	public Advertisement deleteAdvertisement(AdvertisementDTO adDTO) {
		Advertisement ad;
		// TODO check out tutorial
		return null;
	}
}
