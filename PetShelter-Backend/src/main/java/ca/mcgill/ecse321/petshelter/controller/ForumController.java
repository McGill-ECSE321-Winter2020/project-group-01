package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.ForumDTO;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.ForumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/forum")
public class ForumController {

	@Autowired
	private ForumService forumService;
	
	@Autowired UserRepository userRepository;

	public ForumController() {
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllAdvertisements() {
		return new ResponseEntity<>(
				forumService.getAllForums().stream().map(this::convertToDto).collect(Collectors.toList()),
				HttpStatus.OK);
	}
	
	@GetMapping("/{user}")
	public ResponseEntity<?> getUserForums(@PathVariable String user) {
		return new ResponseEntity<>(forumService.getAllUserForums(userRepository.findUserByUserName(user))
				.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
	}

	public ForumDTO convertToDto(Forum forum) {
		ForumDTO forumDTO = new ForumDTO();
		forumDTO.setComments(forum.getComments());
		forumDTO.setSubscribers(forum.getSubscribers());
		forumDTO.setTitle(forum.getTitle());

		return forumDTO;
	}

	@PostMapping()
	public ResponseEntity<?> createForum(@RequestBody ForumDTO forumDTO) {
		Forum forum = forumService.createForum(forumDTO);
		try {
			forumDTO.setComments(forum.getComments());
			forumDTO.setSubscribers(forum.getSubscribers());
			forumDTO.setTitle(forum.getTitle());
			
			return new ResponseEntity<>(forumDTO, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
