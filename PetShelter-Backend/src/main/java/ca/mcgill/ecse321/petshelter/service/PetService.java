package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {
    
    @Autowired
    PetRepository petRepository;
    
    @Autowired
    AdvertisementRepository advertisementRepository;
    
    @Transactional
    public List<Pet> getAllPets() {
        return toList(petRepository.findAll());
    }
    
    @Transactional
    public Pet getPet(String name, Advertisement advertisement) {
        return petRepository.findPetByNameAndAdvertisement(name, advertisement);
    }
    
    /*
    @Transactional
    public List<Pet> getAllUserPets(User user) {
    	return petRepository.findAllByUser(user);
    }
    */
    
    //From tutorial
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
    
    @Transactional
    public Pet createPet(PetDTO petDTO) {
        //condition checks
        if (petDTO.getName() == null) {
            throw new PetException("Name can't be null!");
        }
        if (petDTO.getSpecies() == null) {
            throw new PetException("Species can't be null!");
        }
        if (petDTO.getBreed() == null) {
            throw new PetException("Breed can't be null!");
        }
        if (petDTO.getDescription() == null) {
            throw new PetException("Description can't be null!");
        }
    
        Pet pet = new Pet();
        pet.setDateOfBirth(petDTO.getDateOfBirth());
        pet.setName(petDTO.getName());
        pet.setSpecies(petDTO.getSpecies());
        pet.setBreed(petDTO.getBreed());
        pet.setDescription(petDTO.getDescription());
        try {
        	pet.setAdvertisement(advertisementRepository.findAdvertisementByTitle(petDTO.getAdvertisementTitle()));
        } catch (NullPointerException e) { 
        	pet.setAdvertisement(null); // Occurs if a pet does not have an advertisement
        }
        pet.setPicture(petDTO.getPicture());
        petRepository.save(pet);
        return pet;
    }
}
