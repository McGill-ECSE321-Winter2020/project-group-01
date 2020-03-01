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
import java.util.stream.Collectors;

@Service
public class AdvertisementService {
    @Autowired
    private AdvertisementRepository advertisementRepository;
    
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private PetService petService;
    
    /**
     * Finds an advertisement by the DTO
     *
     * @param adDTO advertisement DTO
     * @return advertisement object
     */
    @Transactional
    public AdvertisementDTO getAdvertisement(AdvertisementDTO adDTO) {
        if (adDTO.getAdId() == null || advertisementRepository.findAdvertisementById(adDTO.getAdId()) == null) {
            throw new AdvertisementException("Advertisement does not exist.");
        }
        Advertisement ad = advertisementRepository.findAdvertisementById(adDTO.getAdId());
        return convertToDTO(ad);
    }
    
    /**
     * Finds advertisement by title
     *
     * @param title title of advertisement
     * @return List of advertisement that matches that title
     */
    @Transactional
    public List<AdvertisementDTO> getAdvertisementByTitle(String title) {
        if (title == null || advertisementRepository.findAdvertisementByTitle(title) == null) {
            throw new AdvertisementException("No advertisement found");
        }
        return advertisementRepository.findAdvertisementByTitle(title).stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * Gets all the created advertisement in the database
     *
     * @return List of all the advertisement
     */
    @Transactional
    public List<AdvertisementDTO> getAllAdvertisements() {
        return advertisementRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * Finds advertisement by a PetID
     *
     * @param petId unique identifier of the pet
     * @return unique advertisement linked to that pet
     */
    @Transactional
    public AdvertisementDTO getAdvertisementByPet(long petId) {
        Pet pet = petRepository.findPetById(petId);
        if (pet == null) {
            throw new AdvertisementException("Pet does not exist.");
        } else {
            Advertisement ad = pet.getAdvertisement();
            if (ad == null) {
                throw new AdvertisementException("Advertisement does not exist.");
            } else {
                return convertToDTO(ad);
            }
        }
    }
    
    /**
     * Creates and advertisement and stores it in the database
     *
     * @param id advertisement DTO (JSON format)
     * @return advertisement object
     */
    @Transactional
    public AdvertisementDTO getAdvertisementById(long id) {
        Advertisement ad = advertisementRepository.findAdvertisementById(id);
        if (ad == null) {
            throw new AdvertisementException("Advertisement does not exist.");
        } else {
            return convertToDTO(ad);
        }
    }
    
    @Transactional
    public AdvertisementDTO createAdvertisement(AdvertisementDTO adDTO) {
        List<Pet> petsInAd = validateParametersAdd(adDTO);
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
        return convertToDTO(ad);
    }
    
    
    //todo: check if edit and delete is the original user in order to delete
    
    
    /**
     * Allows user to edit advertisement
     *
     * @param adDTO advertisement DTO
     * @return edited advertisement
     */
    @Transactional
    public AdvertisementDTO editAdvertisement(AdvertisementDTO adDTO) {
        if (adDTO.getAdId() == null) {
            throw new AdvertisementException("Advertisement does not exist");
        }
        Advertisement ad = advertisementRepository.findAdvertisementById(adDTO.getAdId());
        Set<Pet> newPets = validateParametersEdit(adDTO);
        Set<Application> applications = new HashSet<Application>();
        //TODO change with fixed master
        //applications.addAll(adDTO.getAdoptionApplication());
        applications.addAll(adDTO.getApplication());
        ad.setApplication(applications);
        ad.setApplication(applications);
    
        ad.setTitle(adDTO.getTitle());
        ad.setDescription(adDTO.getDescription());
        ad.setIsFulfilled(adDTO.isFulfilled());
        advertisementRepository.save(ad);
        for (Pet pet : newPets) {
            pet.setAdvertisement(ad);
            petRepository.save(pet);
        }
        return convertToDTO(ad);
    }
    
    /**
     * Deletes advertisement from the database
     *
     * @param adDTO advertisement DTO
     * @return
     */
    @Transactional
    public boolean deleteAdvertisement(AdvertisementDTO adDTO) {
        if (adDTO.getAdId() == null) {
            throw new AdvertisementException("Advertisement not found");
        }
        AdvertisementDTO ad = getAdvertisementById(adDTO.getAdId());
        Set<Application> apps = ad.getApplication();
        if (apps != null) {
            for (Application app : apps) {
                applicationService.deleteApplication(app.getId());
            }
        }
        advertisementRepository.deleteById(ad.getAdId());
        return true;
    
    }
    
    public AdvertisementDTO convertToDTO(Advertisement advertisement) {
        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setAdId(advertisement.getId());
        advertisementDTO.setApplication(advertisement.getApplication());
        advertisementDTO.setDescription(advertisement.getDescription());
        advertisementDTO.setFulfilled(advertisement.isIsFulfilled());
        advertisementDTO.setTitle(advertisement.getTitle());
        //	advertisementDTO.setPetIds(advertisement.get); hmmm this doesnt exist
        return advertisementDTO;
    }
    
    public List<Pet> validateParametersAdd(AdvertisementDTO adDTO) {
        if (adDTO.getPetIds() == null || adDTO.getPetIds().length == 0) {
            throw new AdvertisementException("A pet must be linked to an advertisement.");
    
        }
        //finding all pets that are on this ad
        int numOfPets = adDTO.getPetIds().length;
        
        List<Pet> petsInAd = new ArrayList<>();
        for (int i = 0; i < numOfPets; i++) {
            Pet pet = petRepository.findPetById(adDTO.getPetIds()[i]);
            //    System.out.println(pet);
            if (pet == null) {
                throw new AdvertisementException("One or more pets do not exist.");
            } else if (pet.getAdvertisement() != null) {
                throw new AdvertisementException("One or more pets already have an advertisement.");
            }
            petsInAd.add(pet);
        }
        
        if (adDTO.getTitle() == null || adDTO.getTitle().equals("")) {
            throw new AdvertisementException("An advertisement needs a title");
        }
        if (adDTO.getDescription() == null || adDTO.getDescription().trim().equals("")) {
            throw new AdvertisementException("An advertisement needs a description");
        }
        return petsInAd;
    }
    
    public Set<Pet> validateParametersEdit(AdvertisementDTO adDTO) {
        Advertisement ad = advertisementRepository.findAdvertisementById(adDTO.getAdId());
    
        if (adDTO.getTitle() == null || adDTO.getTitle().trim().equals("")) {
            throw new AdvertisementException("Title cannot be empty");
        }
        if (adDTO.getDescription() == null || adDTO.getDescription().trim().equals("")) {
            throw new AdvertisementException("Description cannot be empty");
        }
        //what if the first one doesnt but the rest exists?
        if (adDTO.getPetIds() == null || petRepository.findPetById(adDTO.getPetIds()[0]) == null) {
            throw new AdvertisementException("One or more pets do not exist.");
        }
        Pet pet0 = petRepository.findPetById(adDTO.getPetIds()[0]);
    
        int numOfPets = adDTO.getPetIds().length;
        Set<Pet> newPets = new HashSet<Pet>();
        newPets.add(pet0);
        for (int i = 1; i < numOfPets; i++) {
            Pet petI = petRepository.findPetById(adDTO.getPetIds()[i]);
            if ((petI.getAdvertisement() != ad) && (petI.getAdvertisement() != null)) {
                throw new AdvertisementException("One or more pets have a different advertisement.");
            } else {
                newPets.add(petI);
            }
        }
        // System.out.println(petRepository.findAll());
        for (Pet pet : petRepository.findAll()) {
            if (pet.getAdvertisement().equals(ad)) {
                if (!(newPets.contains(pet))) {
                    pet.setAdvertisement(null);
                }
            }
        }
        return newPets;
    }
}
