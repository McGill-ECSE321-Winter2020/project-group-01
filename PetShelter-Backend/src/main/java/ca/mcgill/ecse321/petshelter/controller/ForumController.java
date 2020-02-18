package ca.mcgill.ecse321.petshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.dto.ForumDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

import java.util.Set;
import java.util.HashSet;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/forum")
public class ForumController {

	@Autowired
	ForumRepository forumRepository;
	
	@Autowired
	UserRepository userRepository;
	
	/**
	 * Convert a forum thread to a forum DTO.
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
