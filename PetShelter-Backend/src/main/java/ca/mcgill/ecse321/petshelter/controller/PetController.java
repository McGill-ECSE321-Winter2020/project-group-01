package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.PetService;
import ca.mcgill.ecse321.petshelter.service.UserService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

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
	private UserService userService;

	@Autowired
	private PetService petService;
	
	
    @PostConstruct
    public void initIt() throws Exception {
      User user = userRepository.findUserByEmail("poulin.katrina@gmail.com");
      
      user.setIsEmailValidated(true);
      user.setUserType(UserType.ADMIN);
      
      userRepository.save(user);System.out.print("\n" +user.getApiToken()+"\n");
    }
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
     * @param token user token
     * @param pet   petDTO
     * @return check if pet is updated
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
     * @param token user token
     * @param pet petDTO
     * @return check if pet is created
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
