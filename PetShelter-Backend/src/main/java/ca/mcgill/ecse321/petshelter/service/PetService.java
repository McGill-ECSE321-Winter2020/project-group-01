package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

//TODO Javadoc
@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AdvertisementRepository advertisementRepository;
	
	@Transactional
	public Pet getPet(long petId) {
		Pet pet = petRepository.findPetById(petId);
		if(pet == null) {
			throw new PetException("Pet does not exist.");
		} else {
			return pet;
		}
	}
	
	@Transactional
	public Pet getPet(PetDTO petDTO) {
		Pet pet = petRepository.findPetById(petDTO.getId());
		if(pet == null) {
			throw new PetException("Pet does not exist.");
		} else {
			return pet;
		}
	}
	
	@Transactional
	public Set<Pet> getPetsByUser(String userName) {
		User user = userRepository.findUserByUserName(userName);
		if (user == null) {
			throw new PetException("User does not exist.");
		}
		else {
			return user.getPets();
		}
	}
	@Transactional
	public List<Pet> getPetsByAdvertisement(long adId) {
		List<Pet> pets = petRepository.findPetByAdvertisement(advertisementRepository.findAdvertisementById(adId));
		if (pets == null) {
			throw new PetException("Advertisement does not exist.");
		}
		else {
			return pets;
		}
	}
	
	@Transactional
	public Set<Pet> getAllPets() {
		return petRepository.findAll();
	}
	
	@Transactional
	public Pet createPet(PetDTO petDTO) {
		User user = userRepository.findUserByUserName(petDTO.getUserName());
		if (user == null) {
			throw new PetException("Cannot add: User does not exist.");
		}
		if (petDTO.getName().trim() == "" || petDTO.getName() == null) {
			throw new PetException("Cannot add: A pet needs a name.");
		}
		if (petDTO.getSpecies().trim() == "" || petDTO.getSpecies() == null) {
			throw new PetException("Cannot add: A pet needs a species.");
		}
		if (petDTO.getBreed().trim() == "" || petDTO.getBreed() == null) {
			throw new PetException("Cannot add: A pet needs a breed.");
		}
		if (petDTO.getDescription() == null) {
			//if the description is null we set it to empty
			petDTO.setDescription("");
		}
		if(petDTO.getGender() == null) {
			throw new PetException("Cannot add: A pet needs a gender.");
		}
		Pet pet = new Pet();
		pet.setName(petDTO.getName());
		pet.setDateOfBirth(petDTO.getDateOfBirth());
		pet.setBreed(petDTO.getBreed());
		pet.setSpecies(petDTO.getSpecies());
		pet.setDescription(petDTO.getDescription());
		pet.setGender(petDTO.getGender());
		pet.setPicture(petDTO.getPicture());
		pet.setAdvertisement(petDTO.getAdvertisement());
		Set<Pet> allUserPets = getPetsByUser(petDTO.getUserName());
		allUserPets.add(pet);
		petRepository.save(pet);
		userRepository.save(user);
		petDTO.setId(pet.getId());
	
		return pet;		
	}
	
	@Transactional
	public Pet editPet (PetDTO petDTO ) {
		Pet pet = petRepository.findPetById(petDTO.getId());
		if(pet == null) {
			throw new PetException("Cannot edit: Pet does not exist.");
		}
		if (petDTO.getName().trim() == "" || petDTO.getName() == null) {
			throw new PetException("Cannot edit: A pet needs a name.");
		}
		if (petDTO.getSpecies().trim() == "" || petDTO.getSpecies() == null) {
			throw new PetException("Cannot edit: A pet needs a species.");
		}
		if (petDTO.getBreed().trim() == "" || petDTO.getBreed() == null) {
			throw new PetException("Cannot edit: A pet needs a breed.");
		}
		if (petDTO.getDescription() == null) {
			//if the description is null we set it to empty
			petDTO.setDescription("");
		}
		if (petDTO.getGender() == null) {
			throw new PetException("Cannot edit: A pet needs a gender.");
		}
		User oldUser = userRepository.findUserByPets(pet);
		if (oldUser == null) {
			throw new PetException("Cannot edit: User not found.");
		}
		User newUser = userRepository.findUserByUserName(petDTO.getUserName());
		if (newUser == null) {
			throw new PetException("Cannot edit: User not found.");
		} else if (!(newUser.equals(oldUser))) {    //change ownership
			Set<Pet> oldUserPets = oldUser.getPets();
			oldUserPets.remove(pet);
			oldUser.setPets(oldUserPets);
			
			Set<Pet> newUserPets = newUser.getPets();
			newUserPets.add(pet);
			newUser.setPets(newUserPets);
			
			userRepository.save(oldUser);
			userRepository.save(newUser);	
		}
		pet.setName(petDTO.getName());
		pet.setDateOfBirth(petDTO.getDateOfBirth());
		pet.setBreed(petDTO.getBreed());
		pet.setSpecies(petDTO.getSpecies());
		pet.setDescription(petDTO.getDescription());
		pet.setGender(petDTO.getGender());
		pet.setPicture(petDTO.getPicture());
		petRepository.save(pet);
	
		return pet;
	}
	
	@Transactional
	public boolean deletePet (PetDTO petDTO) {
		Pet pet = petRepository.findPetById(petDTO.getId());
		if (pet == null) {
			throw new PetException("Cannot delete: Pet does not exist.");
		}
		else {
			petRepository.delete(pet);
			return true;
		}
	}
}
