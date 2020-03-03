package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Application;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.service.exception.AdvertisementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;
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
        Set<Application> applications = new HashSet<>(adDTO.getApplication());
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
        Set<Application> applications = new HashSet<>(adDTO.getApplication());
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
        System.out.println(convertToDTO(ad));
        return convertToDTO(ad);
    }
    
    /**
     * Deletes advertisement from the database
     *
     * @param adDTO advertisement DTO
     * @return isDeleted
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
        // Remove reference to advertisement from pets.
        if (ad.getPetIds() != null) {
            Arrays.stream(ad.getPetIds())
                    .forEach(petID -> {
                        Pet pet = petRepository.findPetById(petID);
                        pet.setAdvertisement(null);
                        petRepository.save(pet);
                    });
        }
        return true;
    
    }
    
    public AdvertisementDTO convertToDTO(Advertisement advertisement) {
        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setAdId(advertisement.getId());
        advertisementDTO.setApplication(advertisement.getApplication());
        advertisementDTO.setDescription(advertisement.getDescription());
        advertisementDTO.setFulfilled(advertisement.isIsFulfilled());
        advertisementDTO.setTitle(advertisement.getTitle());

        List<Long> petIds = new ArrayList<>();
        petRepository.findAll().forEach(pet -> {
            if (pet.getAdvertisement().equals(advertisement.getId())) {
                petIds.add(pet.getId());
            }
        });

        advertisementDTO.setPetIds(petIds.toArray(new Long[petIds.size()]));
        return advertisementDTO;
    }

    /**
     * Convert an advertisement DTO to an advertisement entity.
     * @param advertisementDTO The advertisement entity.
     * @return The advertisement entity.
     */
    public Advertisement convertToEntity(AdvertisementDTO advertisementDTO) {
        Advertisement advertisement = new Advertisement();
        advertisement.setId(advertisementDTO.getAdId());
        advertisement.setApplication(
                advertisementDTO.getApplication().stream()
                .map(applicationService::convertToEntity)
                .collect(Collectors.toSet())
        );
        advertisement.setDescription(advertisementDTO.getDescription());
        advertisement.setIsFulfilled(advertisementDTO.isFulfilled());
        advertisement.setPet(petService.convertToEntity(advertisementDTO.getPet()));
        advertisement.setTitle(advertisementDTO.getTitle());
        return advertisement;
    }

    /**
     * Validate the parameters of an advertisement entity object.
     * @param advertisement An advertisement entity object.
     * @return If the advertisement is valid.
     */
    public void validateParameters(Advertisement advertisement) {
        if (advertisement.getTitle() == null || advertisement.getTitle().trim().equals("")) {
            throw new AdvertisementException("Title must not be null or empty.");
        }
        if (advertisement.getDescription() == null || advertisement.getDescription().trim().equals("")) {
            throw new AdvertisementException("Description must not be null or empty.");
        }
        if (advertisement.getPet() == null) {
            throw new AdvertisementException("Pet must not be null.");
        }
        if (advertisement.getPet().getAdvertisement() != null) {
            throw new AdvertisementException("Pet must not already have an advertisement.");
        }
        if (!petRepository.findById(advertisement.getPet().getId()).isPresent()) {
            throw new AdvertisementException("Pet doesn't exist.");
        }
    }

}
