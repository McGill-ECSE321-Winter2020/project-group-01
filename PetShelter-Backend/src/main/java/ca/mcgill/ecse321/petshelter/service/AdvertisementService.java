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
import ca.mcgill.ecse321.petshelter.service.exception.PetException;
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
            throw new AdvertisementException("No advertisement found.");
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

    /**
     * Create an advertisement.
     * @param adDTO The advertisement DTO to create.
     * @return The created advertisement.
     */
    @Transactional
    public AdvertisementDTO createAdvertisement(AdvertisementDTO adDTO) {
        Long adID = (new Advertisement()).getId();
        Advertisement advertisement = convertToEntity(adDTO);
        advertisement.setId(adID);
        validateParameters(advertisement);
        advertisementRepository.save(advertisement);
        PetDTO petDTO = petService.convertToDTO(petRepository.findPetById(advertisement.getPet().getId()));
        petDTO.setAdvertisement(advertisement.getId());
        petService.editPet(petDTO);
        return convertToDTO(advertisement);
    }

    /**
     * Allows user to edit advertisement
     *
     * @param adDTO advertisement DTO
     * @return edited advertisement
     */
    @Transactional
    public AdvertisementDTO editAdvertisement(AdvertisementDTO adDTO) {
        Advertisement advertisement = convertToEntity(adDTO);
        validateParameters(advertisement);
        if (adDTO.getAdId() == null || advertisementRepository.findAdvertisementById(adDTO.getAdId()) == null) {
            throw new AdvertisementException("Advertisement does not exist.");
        }
        advertisementRepository.save(advertisement);
        return convertToDTO(advertisement);
    }
    
    /**
     * Deletes advertisement from the database
     *
     * @param adDTO advertisement DTO
     * @return isDeleted
     */
    @Transactional
    public AdvertisementDTO deleteAdvertisement(AdvertisementDTO adDTO) {
        if (adDTO == null || adDTO.getAdId() == null) {
            throw new AdvertisementException("Advertisement does not exist.");
        }
        Advertisement advertisement = advertisementRepository.findAdvertisementById(adDTO.getAdId());
        if (advertisement == null) {
            throw new AdvertisementException("Advertisement does not exist.");
        }
        Pet pet = advertisement.getPet();
        pet.setAdvertisement(null);
        petRepository.save(pet);
        advertisementRepository.delete(advertisement);
        return convertToDTO(advertisement);
    }

    /**
     * Convert an advertisement entity to an advertisement DTO.
     * @param advertisement An application entity.
     * @return An application DTO.
     */
    public AdvertisementDTO convertToDTO(Advertisement advertisement) {
        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setAdId(advertisement.getId());
        advertisementDTO.setApplication(
                advertisement.getApplication().stream()
                .map(applicationService::convertToDto)
                .collect(Collectors.toSet())
        );
        advertisementDTO.setDescription(advertisement.getDescription());
        advertisementDTO.setFulfilled(advertisement.isIsFulfilled());
        advertisementDTO.setTitle(advertisement.getTitle());
        advertisementDTO.setPet(petService.convertToDTO(advertisement.getPet()));
        return advertisementDTO;
    }

    /**
     * Convert an advertisement DTO to an advertisement entity.
     * @param advertisementDTO The advertisement entity.
     * @return The advertisement entity.
     */
    public Advertisement convertToEntity(AdvertisementDTO advertisementDTO) {
        Advertisement advertisement = new Advertisement();
        if (!advertisementDTO.getApplication().isEmpty()) {
            advertisement.setApplication(
                    advertisementDTO.getApplication().stream()
                            .map(applicationService::convertToEntity)
                            .collect(Collectors.toSet())
            );
        } else {
            advertisement.setApplication(new HashSet<Application>());
        }
        advertisement.setDescription(advertisementDTO.getDescription());
        advertisement.setIsFulfilled(advertisementDTO.isFulfilled());

        Pet pet = new Pet();
        pet.setAdvertisement(advertisement);

        if (advertisementDTO.getPet() == null) {
            throw new AdvertisementException("One or more pets do not exist.");
        }
        Pet databasePet = petRepository.findPetById(advertisementDTO.getPet().getId());
        if (databasePet == null) {
            throw new AdvertisementException("One or more pets do not exist.");
        }
        pet.setGender(databasePet.getGender());
        pet.setId(databasePet.getId());
        pet.setSpecies(databasePet.getSpecies());
        pet.setDescription(databasePet.getDescription());
        pet.setDateOfBirth(databasePet.getDateOfBirth());
        pet.setBreed(databasePet.getBreed());
        pet.setName(databasePet.getName());
        pet.setPicture(databasePet.getPicture());

        advertisement.setPet(pet);
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
        if (advertisement.getPet().getAdvertisement().getId() != advertisement.getId()) {
            throw new AdvertisementException("Pet must not already have an advertisement.");
        }
        if (petRepository.findPetById(advertisement.getPet().getId()) == null) {
            throw new AdvertisementException("Pet doesn't exist.");
        }
    }

}
