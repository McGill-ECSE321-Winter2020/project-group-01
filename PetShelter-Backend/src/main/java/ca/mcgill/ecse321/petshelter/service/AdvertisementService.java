package ca.mcgill.ecse321.petshelter.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.ApplicationRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

@Service
public class AdvertisementService {
	@Autowired
	private AdvertisementRepository advertisementRepository;
	
	@Autowired
	private PetRepository petRepository;
	
	@Transactional
	public Advertisement createAd (AdvertisementDTO adDTO) {
		if(adDTO.getTitle().trim() == "" || adDTO.getTitle()==null) {
			throw new IllegalArgumentException("Title can not be empty"); 
			}
		if(adDTO.getDescription().trim() == "" || adDTO.getDescription().trim() == null) {
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
		
		return ad;
	
	}
}
//delete
//list
//edit
//list all

