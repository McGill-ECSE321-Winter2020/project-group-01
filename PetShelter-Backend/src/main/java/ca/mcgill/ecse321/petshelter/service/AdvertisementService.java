package ca.mcgill.ecse321.petshelter.service;

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
	public Advertisement getAdvertisement(long id) {
		// TODO does this take attributes or DTO?
		return null;
	}

	@Transactional
	public List<Advertisement> getAdvertisement(String title) {
		// TODO does this take attributes or DTO?
		return null;
	}

	@Transactional
	public List<Advertisement> getAllAdvertisements() {
		List<Advertisement> allAds = advertisementRepository.findAll();
		return allAds;
	}

	// TODO not sure if this goes in pet or ad service
	@Transactional
	public Pet getPetByAdvertisement(Advertisement ad) {
		Pet pet = petRepository.findPetByAdvertisement(ad);
		return pet;
	}

	@Transactional
	public Advertisement createAdvertisement(AdvertisementDTO adDTO) {
		if (adDTO.getTitle().trim() == "" || adDTO.getTitle() == null) {
			throw new IllegalArgumentException("Title can not be empty");
		}
		if (adDTO.getDescription().trim() == "" || adDTO.getDescription().trim() == null) {
			throw new IllegalArgumentException("Description cannot be empty");
		}
		Advertisement ad = new Advertisement();
		Set<AdoptionApplication> applications = new HashSet<AdoptionApplication>();
		applications.addAll(adDTO.getAdoptionApplication());
		ad.setAdoptionApplication(applications);
		ad.setTitle(adDTO.getTitle());
		ad.setDescription(adDTO.getDescription());
		ad.setIsFulfilled(adDTO.isFulfilled());

		advertisementRepository.save(ad);
		Pet pet = petRepository.findPetById(adDTO.getPetId());
		pet.setAdvertisement(ad);
		petRepository.save(pet);
//TODO yo ton pet il peut etre null faque si c le cas rien devrait etre saved @KAtrina
		return ad;

	}

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
