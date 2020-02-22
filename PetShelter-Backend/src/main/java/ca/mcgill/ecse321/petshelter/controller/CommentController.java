package ca.mcgill.ecse321.petshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
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

	@GetMapping("/comments/all")
	public List<CommentDTO> getAllComments() {
		return commentService.getComments().stream().map(CommentController::commentToDto).collect(Collectors.toList());
	}

	@GetMapping("/comments/{username}")
	public List<CommentDTO> getUserComments(@PathVariable String username) {
		return commentService.getCommentsByUser(userRepository.findUserByUserName(username).getId()).stream()
				.map(CommentController::commentToDto).collect(Collectors.toList());
	}

	@PostMapping("/{id}")
	public ResponseEntity<?> createComment(@RequestBody CommentDTO comment, @PathVariable long id,
			@RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		Comment commentCreated = commentService.addComment(comment.getText(), id, user.getId());
		return new ResponseEntity<>(commentToDto(commentCreated), HttpStatus.CREATED);
	}

	@PutMapping("/{id}/{commentId}")
	public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO, @PathVariable long id,
			@RequestHeader String token, @PathVariable long commentId) {
		User user = userRepository.findUserByApiToken(token);
		// if the user updating the comment is the comment's author
		if (commentRepository.findCommentById(commentId) != null && user != null
				&& user.getUserName().equals(commentRepository.findCommentById(commentId).getUser().getUserName())) {
			Comment comment = commentService.updateComment(commentId, commentDTO.getText());
			return new ResponseEntity<>(commentToDto(comment), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable long id, @RequestHeader String token,
			@PathVariable long commentId) {
		User user = userRepository.findUserByApiToken(token);
		// if the user updating the comment is the comment's author or an admin
		if (commentRepository.findCommentById(commentId) != null && user != null
				&& (user.getUserName().equals(commentRepository.findCommentById(commentId).getUser().getUserName())
						|| user.getUserType().equals(UserType.ADMIN))) {
			commentService.deleteComment(commentId);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
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
