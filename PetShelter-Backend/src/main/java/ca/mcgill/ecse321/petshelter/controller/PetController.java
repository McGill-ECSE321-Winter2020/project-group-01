package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pet")
public class PetController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PetService petService;
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getPet(@RequestHeader String token, @PathVariable long id) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			return new ResponseEntity<>(petService.getPet(id), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllPets(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			return new ResponseEntity<>(petService.getAllPets(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/all/user/{user}")
	public ResponseEntity<?> getAllPetsFromUser(@RequestHeader String token, @PathVariable String user) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			return new ResponseEntity<>(petService.getPetsByUser(user), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/all/ad/{advertisement}")
	public ResponseEntity<?> getAllPetsFromAdvertisement(@RequestHeader String token,
														 @PathVariable long advertisement) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			return new ResponseEntity<>(petService.getPetsByAdvertisement(advertisement), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePet(@RequestHeader String token, @PathVariable long id) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			petService.deletePet(id, requester.getUserName());
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Changes the ownership of a pet
	 *
	 * @param token
	 * @param pet
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<?> updatePet(@RequestHeader String token, @RequestBody PetDTO pet) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			PetDTO pet2 = petService.editPet(pet);
			return new ResponseEntity<>(pet2, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/changeOwner")
	public ResponseEntity<?> changeOwner(@RequestHeader String token, @RequestBody PetDTO pet) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			PetDTO petDTO = petService.changeOwner(pet, token);
			return new ResponseEntity<>(petDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	/**
	 * Creates a pet
	 *
	 * @param token
	 * @param pet
	 * @return
	 */
	@PostMapping("/")
	public ResponseEntity<?> createPet(@RequestHeader String token, @RequestBody PetDTO pet) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null && requester.getUserName().equals(pet.getUserName())) {
			PetDTO createdPet = petService.createPet(pet);
			return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
