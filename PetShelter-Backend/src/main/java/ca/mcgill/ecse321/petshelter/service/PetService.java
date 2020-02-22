package ca.mcgill.ecse321.petshelter.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public Pet getPet(long petId) {
		Pet pet = petRepository.findPetById(petId);
		return pet;
	}
	
	@Transactional
	public List<Pet> getPetsByUser(String userName) {
		User user = userRepository.findUserByUserName(userName);
		return toList(user.getPets());
	}
	
	@Transactional
	public List<Pet> getAllPets() {
		return toList(petRepository.findAll());
	}
	
	@Transactional
	public Pet createPet(Date dateOfBirth, String name, String species, String breed, String description,
			byte[] picture, String userName, Gender gender) {
		User user = userRepository.findUserByUserName(userName);
		if (user == null) {
			throw new IllegalArgumentException("User does not exist: a pet needs a user");
		}
		if (name.trim() == "") {
			throw new IllegalArgumentException("A pet needs a name");
		}
		if (species.trim() == "") {
			throw new IllegalArgumentException("A pet needs a species");
		}
		if (breed.trim() == "") {
			throw new IllegalArgumentException("A pet needs a breed");
		}
		if (description.trim() == "") {
			//if the description is null we set it to empty
			description = description.trim();
		}
		if(gender == null) {
			throw new IllegalArgumentException("A pet needs a gender");
		}
		Pet pet = new Pet();
		pet.setName(name);
		pet.setDateOfBirth(dateOfBirth);
		pet.setBreed(breed);
		pet.setSpecies(species);
		pet.setDescription(description);
		pet.setGender(gender);
		pet.setPicture(picture);
		Set <Pet> pets = user.getPets();
		pets.add(pet);
		user.setPets(pets);
		petRepository.save(pet);
		userRepository.save(user);
	
		return pet;		
	}
	
	@Transactional
	public boolean deletePet (long petId) {
		Pet pet = petRepository.findPetById(petId);
		if (pet == null) {
			throw new IllegalArgumentException("Pet does not exist");
		}
		else {
			petRepository.delete(pet);
			return true;
		}
	}
	
	@Transactional
	public Pet editPet (Date dateOfBirth, String name, String species, String breed, String description,
			byte[] picture, Gender gender, long petId) {

		Pet pet = petRepository.findPetById(petId);
		pet.setName(name);
		pet.setDateOfBirth(dateOfBirth);
		pet.setBreed(breed);
		pet.setSpecies(species);
		pet.setDescription(description);
		pet.setGender(gender);
		pet.setPicture(picture);
		petRepository.save(pet);
	
		return pet;
	}
	
	/**
	 * From ECSE321 Tutorial
	 * @param <T>
	 * @param iterable
	 * @return
	 */
	private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

}
