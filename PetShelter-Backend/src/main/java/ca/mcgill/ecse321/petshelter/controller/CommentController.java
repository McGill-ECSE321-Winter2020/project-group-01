package ca.mcgill.ecse321.petshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/forum")
public class CommentController {

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	UserRepository userRepository;
	
	// The getting of comments is handled by the forum controller.

	/**
	 * Convert a comment entity to a comment DTO.
	 * 
	 * @param comment The comment to convert.
	 * @return A comment DTO.
	 */
	static CommentDTO commentToDto(Comment comment) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setDatePosted(comment.getDatePosted());
		commentDTO.setId(comment.getId());
		commentDTO.setText(comment.getText());
		commentDTO.setUser(UserController.userToDto(comment.getUser()));
		return commentDTO;
	}

}
