package ca.mcgill.ecse321.petshelter.service;

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
	public Advertisement createAdvertisement(String title, boolean fulfilled, List<Long> petIds, String description) {
		if (title == "" || title == null) {
			throw new IllegalArgumentException("Title cannot be empty.");
		}
		if (description == "" || description == null) {
			throw new IllegalArgumentException("Description cannot be empty.");
		}
		if (petIds.size() == 0) {
			throw new IllegalArgumentException("There has to be at least one pet.");
		}
		for (Long id : petIds) {
			if (petRepository.findById(id) == null) {
				throw new IllegalArgumentException("At least one of the pets specified do not exist.");
			}
			else if (petRepository.findPetById(id).getAdvertisement()!=null) {
				throw new IllegalArgumentException("At least one of the pets specified already has an advertisement.");
			}
		}
		Advertisement ad = new Advertisement();
		Set<AdoptionApplication> applications = new HashSet<AdoptionApplication>();
		ad.setTitle(title);
		ad.setDescription(description);
		ad.setIsFulfilled(fulfilled);
		ad.setAdoptionApplication(applications);

		advertisementRepository.save(ad);
		for (Long id : petIds) {
			Pet pet = petRepository.findPetById(id);
			pet.setAdvertisement(ad); //TODO test case already has ad
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
