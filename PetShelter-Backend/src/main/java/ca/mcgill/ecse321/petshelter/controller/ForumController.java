package ca.mcgill.ecse321.petshelter.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.dto.ForumDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/forum")
public class ForumController {

	@Autowired
	ForumRepository forumRepository;

	@Autowired
	UserRepository userRepository;

	/**
	 * Gets the desired forum and its associated comments.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getForum(@PathVariable(required = true) Long id) {
		Forum forum = forumRepository.findForumById(id);
		if (forum == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else
			return new ResponseEntity<>(forumToDto(forum), HttpStatus.OK);
	}

	/**
	 * Gets all existing forums.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllForums() {
		List<Forum> forums = new ArrayList<>();
		List<ForumDTO> forumsDto = new ArrayList<>();
		Iterable<Forum> usersIterable = forumRepository.findAll();
		usersIterable.forEach(forums::add);
		// Convert the forums into forumdtos
		for (Forum f : forums) {
			forumsDto.add(forumToDto(f));
		}
		return new ResponseEntity<>(forumsDto, HttpStatus.OK);
	}
	
	//TODO
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteForum(@PathVariable long forumId, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		return null; //TODO
	}

	/**
	 * Convert a forum thread to a forum DTO.
	 * 
	 * @param forum The forum to convert.
	 * @return A forum DTO.
	 */

	static ForumDTO forumToDto(Forum forum) {
		ForumDTO forumDTO = new ForumDTO();
		Set<UserDTO> subscribers = new HashSet<UserDTO>();
		Set<CommentDTO> comments = new HashSet<CommentDTO>();
		forum.getSubscribers().forEach(u -> subscribers.add(UserController.userToDto(u)));
		forum.getComments().forEach(c -> comments.add(CommentController.commentToDto(c)));
		forumDTO.setId(forum.getId());
		forumDTO.setTitle(forum.getTitle());
		forumDTO.setComments(comments);
		forumDTO.setSubscribers(subscribers);
		return forumDTO;
	}
}
