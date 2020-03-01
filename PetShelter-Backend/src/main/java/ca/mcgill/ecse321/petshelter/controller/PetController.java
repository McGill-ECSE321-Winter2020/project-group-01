package ca.mcgill.ecse321.petshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.PetService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pet")
public class PetController {

	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PetService petService;
	
	//TODO
	// NEEDS  TO RETURN A DTO
	@GetMapping("/{id}")
	public ResponseEntity<?> getPet(@RequestHeader String token, @PathVariable long id){
		User requester = userRepository.findUserByApiToken(token);
		if(requester != null) {
			return new ResponseEntity<>(petService.getPet(id),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	//TODO NEEDS TO RETURN LIST OF DTOS
	@GetMapping("/all")
	public ResponseEntity<?> getAllPets(@RequestHeader String token){
		User requester = userRepository.findUserByApiToken(token);
		if(requester != null) {
			return new ResponseEntity<>(petService.getAllPets(),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	//TODO NEEDS TO RETURN DTOS
	@GetMapping("/all/{user}")
	public ResponseEntity<?> getAllPetsFromUser(@RequestHeader String token, @PathVariable String user){
		User requester = userRepository.findUserByApiToken(token);
		if(requester != null) {
			return new ResponseEntity<>(petService.getPetsByUser(user),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	//TODO NEEDS TO RETURN DTOS
	@GetMapping("/all/{advertisement}")
	public ResponseEntity<?> getAllPetsFromAdvertisement(@RequestHeader String token, @PathVariable long advertisement){
		User requester = userRepository.findUserByApiToken(token);
		if(requester != null) {
			return new ResponseEntity<>(petService.getPetsByAdvertisement(advertisement),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
