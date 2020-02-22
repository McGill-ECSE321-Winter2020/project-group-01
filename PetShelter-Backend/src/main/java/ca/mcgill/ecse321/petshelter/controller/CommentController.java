package ca.mcgill.ecse321.petshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.CommentService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/forum")
public class CommentController {

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CommentService commentService;
	
	@GetMapping("/all")
	public List<CommentDTO> getAllComments() {
		return commentService.getComments().stream()
				.map(CommentController::commentToDto)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{username}")
	public List<CommentDTO> getUserComments(@PathVariable String username) {
		return commentService.getCommentsByUser(userRepository.findUserByUserName(username).getId())
				.stream().map(CommentController::commentToDto)
				.collect(Collectors.toList());
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createComment(@PathVariable String username) {
		return null; //TODO
	}
	
	@PutMapping("/")
	public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO) {
		return null; //TODO
	}
	
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
