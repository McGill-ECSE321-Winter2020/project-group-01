package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.PetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdvertisementRepository advertisementRepository;

	@Autowired
	private AdvertisementService advertisementService;

	@Transactional
	public Pet getPet(long petId) {
		Pet pet = petRepository.findPetById(petId);
		if (pet == null) {
			throw new PetException("Pet does not exist.");
		} else {
			return pet;
		}
	}

	@Transactional
	public Set<Pet> getAllPets() {
		return petRepository.findAll();
	}

	/**
	 * Creates a pet and stores it in database
	 *
	 * @param petDTO given by controller
	 * @return pet object
	 */
	@Transactional
	public PetDTO createPet(PetDTO petDTO) {
		User user = validateParametersAdd(petDTO);
		Pet pet = new Pet();
		petSetters(pet, petDTO);
		if (petDTO.getAdvertisement() != null
				&& advertisementRepository.findAdvertisementById(petDTO.getAdvertisement()) != null) {
			pet.setAdvertisement(advertisementRepository.findAdvertisementById(petDTO.getAdvertisement()));
		}
		Set<PetDTO> allUserPets = getPetsByUser(petDTO.getUserName());
		allUserPets.add(petDTO);
		petRepository.save(pet);
		userRepository.save(user);
		petDTO.setId(pet.getId());

		return petDTO;
	}

	/**
	 * finds pet with the DTO
	 *
	 * @param petDTO dto handled by controller
	 * @return pet object
	 */
	@Transactional
	public PetDTO getPet(PetDTO petDTO) {
		if (petDTO.getId() == null) {
			throw new PetException("Pet does not exist.");
		} else {
			return petToPetDTO(petRepository.findPetById(petDTO.getId()));
		}
	}

	/**
	 * Finds all pets owner by a user
	 *
	 * @param userName user's username
	 * @return set of all pet
	 */
	@Transactional
	public Set<PetDTO> getPetsByUser(String userName) {
		User user = userRepository.findUserByUserName(userName);
		if (user == null) {
			throw new PetException("User does not exist.");
		} else {
			return user.getPets().stream().map(this::petToPetDTO).collect(Collectors.toSet());
		}
	}

	/**
	 * Find a pet by advertisement
	 *
	 * @param adId id of advertisement
	 * @return list of all pets in that advertisement
	 */
	@Transactional
	public List<PetDTO> getPetsByAdvertisement(long adId) {
		List<Pet> pets = petRepository.findPetByAdvertisement(advertisementRepository.findAdvertisementById(adId));
		if (pets == null) {
			throw new PetException("Advertisement does not exist.");
		} else {
			return pets.stream().map(this::petToPetDTO).collect(Collectors.toList());
		}
	}

	// todo check if owner of the pet

	/**
	 * Edit a pet
	 *
	 * @param petDTO given by controller
	 * @return edited pet
	 */
	@Transactional
	public PetDTO editPet(PetDTO petDTO) {
		Pet pet = validateParametersEdit(petDTO);
		User oldUser = userRepository.findUserByPets(pet);
		if (oldUser == null) {
			throw new PetException("Cannot edit: User not found.");
		}
		User newUser = userRepository.findUserByUserName(petDTO.getUserName());
		if (newUser == null) {
			throw new PetException("Cannot edit: User not found.");
		} else if (!(newUser.equals(oldUser))) { // change ownership
			Set<Pet> oldUserPets = oldUser.getPets();
			oldUserPets.remove(pet);
			oldUser.setPets(oldUserPets);

			Set<Pet> newUserPets = newUser.getPets();
			newUserPets.add(pet);
			newUser.setPets(newUserPets);

			userRepository.save(oldUser);
			userRepository.save(newUser);
		}
		petSetters(pet, petDTO);
		petRepository.save(pet);

		return petToPetDTO(pet);
	}

	// todo, check in DTO if it is the owner of the pet

	/**
	 * Removes a pet from the database
	 *
	 * @param petDTO   given by dto
	 * @param userName requester
	 * @return ok if deleted
	 */
	@Transactional
	public boolean deletePet(long id, String userName) {
		Pet pet = petRepository.findPetById(id);
		User user = userRepository.findUserByUserName(userName);
		if (user == null) {
			throw new PetException("Cannot delete: User does not exist.");
		} else if (pet == null) {
			throw new PetException("Cannot delete: Pet does not exist.");
		} else if (!(user.getPets().contains(pet))) {
			throw new PetException("Cannot delete: The requester is not the owner of the pet.");
		} else {
			Advertisement petAd = pet.getAdvertisement();
			if (petAd != null) {
				advertisementService.deleteAdvertisement(adToDTO(petAd));
			}
			Set<Pet> userPet = user.getPets();
			userPet.remove(pet);
			user.setPets(userPet);
			userRepository.save(user);
			petRepository.delete(pet);

			return true;
		}
	}

	public PetDTO petToPetDTO(Pet pet) {
		PetDTO petDTO = new PetDTO();
		petDTO.setId(pet.getId());
		petDTO.setDateOfBirth(pet.getDateOfBirth());
		petDTO.setSpecies(pet.getSpecies());
		petDTO.setPicture(pet.getPicture());
		petDTO.setName(pet.getName());
		petDTO.setGender(pet.getGender());
		petDTO.setDescription(pet.getDescription());
		petDTO.setBreed(pet.getBreed());
		if (pet.getAdvertisement() != null)
			petDTO.setAdvertisement(pet.getAdvertisement().getId());
		return petDTO;
	}

	public AdvertisementDTO adToDTO(Advertisement ad) {
		AdvertisementDTO adDTO = new AdvertisementDTO();
		adDTO.setAdId(ad.getId());
		adDTO.setApplication(ad.getApplication());
		adDTO.setDescription(ad.getDescription());
		adDTO.setFulfilled(ad.isIsFulfilled());
		adDTO.setTitle(ad.getTitle());
		return adDTO;
	}

	public User validateParametersAdd(PetDTO petDTO) {
		User user = userRepository.findUserByUserName(petDTO.getUserName());
		if (user == null) {
			throw new PetException("Cannot add: User does not exist.");
		}
		if (petDTO.getName() == null || petDTO.getName().trim() == "") {
			throw new PetException("Cannot add: A pet needs a name.");
		}
		if (petDTO.getSpecies() == null || petDTO.getSpecies().trim() == "") {
			throw new PetException("Cannot add: A pet needs a species.");
		}
		if (petDTO.getBreed() == null || petDTO.getBreed().trim() == "") {
			throw new PetException("Cannot add: A pet needs a breed.");
		}
		if (petDTO.getDescription() == null) {
			// if the description is null we set it to empty
			petDTO.setDescription("");
		}
		if (petDTO.getGender() == null) {
			throw new PetException("Cannot add: A pet needs a gender.");
		}
		return user;
	}

	public Pet validateParametersEdit(PetDTO petDTO) {
		if (petDTO.getId() == null) {
			throw new PetException("Cannot edit: Pet does not exist.");
		}
		Pet pet = petRepository.findPetById(petDTO.getId());

		if (petDTO.getName() == null || petDTO.getName().trim().equals("")) {
			throw new PetException("Cannot edit: A pet needs a name.");
		}
		if (petDTO.getSpecies() == null || petDTO.getSpecies().trim().equals("")) {
			throw new PetException("Cannot edit: A pet needs a species.");
		}
		if (petDTO.getBreed() == null || petDTO.getBreed().trim().equals("")) {
			throw new PetException("Cannot edit: A pet needs a breed.");
		}
		if (petDTO.getDescription() == null) {
			// if the description is null we set it to empty
			petDTO.setDescription("");
		}
		if (petDTO.getGender() == null) {
			throw new PetException("Cannot edit: A pet needs a gender.");
		}
		return pet;
	}

	public void petSetters(Pet pet, PetDTO petDTO) {
		pet.setName(petDTO.getName());
		pet.setDateOfBirth(petDTO.getDateOfBirth());
		pet.setBreed(petDTO.getBreed());
		pet.setSpecies(petDTO.getSpecies());
		pet.setDescription(petDTO.getDescription());
		pet.setGender(petDTO.getGender());
		pet.setPicture(petDTO.getPicture());
	}
}
