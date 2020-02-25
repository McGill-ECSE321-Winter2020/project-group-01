package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.PetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pet")
public class PetController {

	@Autowired
	private PetService petService;

	@Autowired
	private UserRepository userRepository;

	public PetController() {
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllPets() {
		return new ResponseEntity<>(
				petService.getAllPets().stream().map(this::convertToDto).collect(Collectors.toList()),
				HttpStatus.OK);
	}

	/*
	@GetMapping("/{user}")
	public ResponseEntity<?> getUserPets(@PathVariable String user) {
		return new ResponseEntity<>(petService.getAllUserPets(userRepository.findUserByUserName(user))
				.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
	}
	*/
	public PetDTO convertToDto(Pet pet) {
		PetDTO petDTO = new PetDTO();
		petDTO.setDateOfBirth(pet.getDateOfBirth());
		petDTO.setDescription(pet.getDescription());
		petDTO.setGender(pet.getGender());
		petDTO.setName(pet.getName());
		petDTO.setBreed(pet.getBreed());
		petDTO.setSpecies(pet.getSpecies());
		petDTO.setPicture(pet.getPicture());
		try {
			petDTO.setAdvertisementTitle(pet.getAdvertisement().getTitle());
		} catch (NullPointerException e) {
			petDTO.setAdvertisementTitle(null); // Occurs if a pet does not have an advertisement
		}
		return petDTO;
	}

	@PostMapping()
	public ResponseEntity<?> createPet(@RequestBody PetDTO petDTO) {
		Pet pet = petService.createPet(petDTO);
		try {
			petDTO.setDateOfBirth(pet.getDateOfBirth());
			petDTO.setDescription(pet.getDescription());
			petDTO.setGender(pet.getGender());
			petDTO.setName(pet.getName());
			petDTO.setBreed(pet.getBreed());
			petDTO.setSpecies(pet.getSpecies());
			petDTO.setPicture(pet.getPicture());
			try {
				petDTO.setAdvertisementTitle(pet.getAdvertisement().getTitle());
			} catch (NullPointerException e) {
				petDTO.setAdvertisementTitle(null); // Occurs if a pet does not have an advertisement
			}
			
			return new ResponseEntity<>(petDTO, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
